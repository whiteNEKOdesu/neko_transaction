package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import neko.transaction.commonbase.utils.entity.OrderDetailInfoStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.product.entity.OrderDetailInfo;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.mapper.OrderDetailInfoMapper;
import neko.transaction.product.service.OrderDetailInfoService;
import neko.transaction.product.to.AddMemberBalanceTo;
import neko.transaction.product.vo.OrderDetailInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
