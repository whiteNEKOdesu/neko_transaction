package neko.transaction.product.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import neko.transaction.commonbase.utils.entity.OrderDetailInfoStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ReturnApplyStatus;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.product.entity.OrderDetailInfo;
import neko.transaction.product.entity.ReturnApplyInfo;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.mapper.OrderDetailInfoMapper;
import neko.transaction.product.service.OrderDetailInfoService;
import neko.transaction.product.service.ReturnApplyInfoService;
import neko.transaction.product.to.AddMemberBalanceTo;
import neko.transaction.product.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Service
public class OrderDetailInfoServiceImpl extends ServiceImpl<OrderDetailInfoMapper, OrderDetailInfo> implements OrderDetailInfoService {
    @Resource
    private MemberInfoFeignService memberInfoFeignService;

    @Resource
    ReturnApplyInfoService returnApplyInfoService;

    /**
     * 学生确认收货
     * @param orderDetailId 订单详情id
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void confirmReceived(String orderDetailId) {
        String uid = StpUtil.getLoginId().toString();
        OrderDetailInfo orderDetailInfo = this.baseMapper.getByIdUid(orderDetailId, uid);
        if(orderDetailInfo == null){
            throw new NoSuchResultException("用户无此订单详情id对应的订单详情信息");
        }

        OrderDetailInfo todoUpdate = new OrderDetailInfo();
        todoUpdate.setStatus(OrderDetailInfoStatus.RECEIVED);
        QueryWrapper<OrderDetailInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetailInfo::getOrderDetailId, orderDetailId)
                //订单详情必须为未收货状态
                .eq(OrderDetailInfo::getStatus, OrderDetailInfoStatus.WAITING_FOR_RECEIVE);

        //step1 -> 修改订单详情信息为已收货
        this.baseMapper.update(todoUpdate, queryWrapper);

        //step2 -> 远程调用用户微服务添加卖家余额
        AddMemberBalanceTo to = new AddMemberBalanceTo();
        to.setUid(orderDetailInfo.getSellerUid())
                .setAddNumber(orderDetailInfo.getCost());

        ResultObject<Object> r = memberInfoFeignService.addBalance(to);
        if(!r.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常");
        }
    }

    /**
     * 分页查询卖家自身的订单详情信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @Override
    public Page<OrderDetailInfoVo> sellerSelfPageQuery(QueryVo vo) {
        Page<OrderDetailInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = StpUtil.getLoginId().toString();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.sellerSelfPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.sellerSelfPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }

    /**
     * 用户已收货的订单详情信息是否存在
     * @param orderId 订单号
     * @param productId 商品id
     * @param uid 学号
     * @return 用户已收货的订单详情信息是否存在
     */
    @Override
    public boolean isReceivedOrderDetailInfoExist(String orderId, String productId, String uid) {
        return this.baseMapper.isReceivedOrderDetailInfoExist(orderId, productId, uid);
    }

    /**
     * 获取订单详情按照状态聚合信息
     * @return 订单详情按照状态聚合信息
     */
    @Override
    public List<OrderDetailStatusAggVo> statusAggCount() {
        return this.baseMapper.statusAggCount();
    }

    /**
     * 添加退货申请
     * @param vo 添加退货申请vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newReturnApplyInfo(NewReturnApplyVo vo) {
        if(vo == null){
            return;
        }

        //step1 -> 修改订单详情状态为 申请退货中
        OrderDetailInfo todoUpdate = new OrderDetailInfo();
        todoUpdate.setStatus(OrderDetailInfoStatus.APPLYING_SEND_BACK);

        this.baseMapper.update(todoUpdate, new QueryWrapper<OrderDetailInfo>().lambda()
                .eq(OrderDetailInfo::getOrderDetailId, vo.getOrderDetailId())
                .ne(OrderDetailInfo::getStatus, OrderDetailInfoStatus.SENT_BACK));

        //step2 -> 添加退货申请
        returnApplyInfoService.newReturnApplyInfo(vo);
    }

    /**
     * 卖家审核退货申请
     * @param vo 提交审核退货申请vo
     */
    @Override
    public void sellerCensorReturnApply(CensorReturnApplyVo vo) {
        Long applyId = vo.getApplyId();
        ReturnApplyInfo returnApplyInfo = returnApplyInfoService.getById(applyId);
        if(returnApplyInfo == null){
            throw new NoSuchResultException("无此退货申请信息");
        }
        String orderDetailId = returnApplyInfo.getOrderDetailId();
        if(!this.baseMapper.isOrderDetailInfoProductBelongsToSellerUid(orderDetailId, StpUtil.getLoginId().toString())){
            throw new NotPermissionException("商品卖家不属于此用户");
        }
        //已经审核，直接返回
        if(returnApplyInfo.getIsSellerPass() != null){
            return;
        }

        ReturnApplyInfo todoUpdate = new ReturnApplyInfo();
        todoUpdate.setSellerResponse(vo.getResponse())
                .setIsSellerPass(vo.getIsPass());
        if(vo.getIsPass()){
            //卖家通过退货申请，则进入商品退还流程
            todoUpdate.setStatus(ReturnApplyStatus.CARGO_RETURNING);
        }else{
            //卖家未通过退货申请，则进入管理员审核流程
            todoUpdate.setStatus(ReturnApplyStatus.ADMIN_CENSORING);
        }

        //记录卖家审核记录
        returnApplyInfoService.update(todoUpdate, new QueryWrapper<ReturnApplyInfo>().lambda()
                .eq(ReturnApplyInfo::getApplyId, applyId)
                //判断是否已经审核
                .isNull(ReturnApplyInfo::getIsSellerPass));
    }

    /**
     * 管理员审核退货申请
     * @param vo 提交审核退货申请vo
     */
    @Override
    public void adminCensorReturnApply(CensorReturnApplyVo vo) {
        Long applyId = vo.getApplyId();
        ReturnApplyInfo returnApplyInfo = returnApplyInfoService.getById(applyId);
        if(returnApplyInfo == null){
            throw new NoSuchResultException("无此退货申请信息");
        }
        //已经审核，直接返回
        if(returnApplyInfo.getIsAdminPass() != null){
            return;
        }

        ReturnApplyInfo todoUpdate = new ReturnApplyInfo();
        todoUpdate.setAdminResponse(vo.getResponse())
                .setIsAdminPass(vo.getIsPass())
                .setOperateAdminId(StpUtil.getLoginId().toString());
        if(vo.getIsPass()){
            //管理员通过退货申请，则进入商品退还流程
            todoUpdate.setStatus(ReturnApplyStatus.CARGO_RETURNING);
        }else{
            //管理员未通过退货申请，则进入驳回退货申请
            todoUpdate.setStatus(ReturnApplyStatus.REJECTED);
        }

        //记录管理员审核记录
        returnApplyInfoService.update(todoUpdate, new QueryWrapper<ReturnApplyInfo>().lambda()
                .eq(ReturnApplyInfo::getApplyId, applyId)
                .eq(ReturnApplyInfo::getStatus, ReturnApplyStatus.ADMIN_CENSORING)
                //判断是否已经审核
                .isNull(ReturnApplyInfo::getIsAdminPass));
    }

    /**
     * 确认退货货物送达
     * @param applyId 申请id
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void confirmReturnCargoSentBack(Long applyId) {
        ReturnApplyInfo returnApplyInfo = returnApplyInfoService.getById(applyId);
        if(returnApplyInfo == null){
            throw new NoSuchResultException("无此退货申请信息");
        }
        String orderDetailId = returnApplyInfo.getOrderDetailId();
        if(!this.baseMapper.isOrderDetailInfoProductBelongsToSellerUid(orderDetailId, StpUtil.getLoginId().toString())){
            throw new NotPermissionException("商品卖家不属于此用户");
        }
        //状态不为货物退还中，直接返回
        if(!returnApplyInfo.getStatus().equals(ReturnApplyStatus.CARGO_RETURNING)){
            return;
        }

        //step1 -> 修改申请状态为 退款成功
        ReturnApplyInfo todoUpdate = new ReturnApplyInfo();
        todoUpdate.setStatus(ReturnApplyStatus.RETURN_COMPLETED);

        returnApplyInfoService.update(todoUpdate, new QueryWrapper<ReturnApplyInfo>().lambda()
                .eq(ReturnApplyInfo::getApplyId, applyId)
                .eq(ReturnApplyInfo::getStatus, ReturnApplyStatus.CARGO_RETURNING));

        //step2 -> 修改订单详情状态为 已退货
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        orderDetailInfo.setOrderDetailId(orderDetailId)
                .setStatus(OrderDetailInfoStatus.SENT_BACK);

        this.baseMapper.updateById(orderDetailInfo);

        //step3 -> 远程调用用户微服务退款
        ReturnApplyInfoVo returnApplyInfoVo = returnApplyInfoService.getReturnApplyInfoByOrderDetailId(orderDetailId);
        AddMemberBalanceTo addTo = new AddMemberBalanceTo();
        addTo.setUid(returnApplyInfoVo.getUid())
                .setAddNumber(returnApplyInfoVo.getActualCost());

        ResultObject<Object> addResult = memberInfoFeignService.addBalance(addTo);
        if(!addResult.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常");
        }

        //step4 -> 远程调用用户微服务扣除卖家余额
        AddMemberBalanceTo decreaseTo = new AddMemberBalanceTo();
        decreaseTo.setUid(StpUtil.getLoginId().toString())
                .setAddNumber(returnApplyInfoVo.getActualCost().negate());

        ResultObject<Object> decreaseResult = memberInfoFeignService.addBalance(decreaseTo);
        if(!decreaseResult.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常");
        }
    }
}
