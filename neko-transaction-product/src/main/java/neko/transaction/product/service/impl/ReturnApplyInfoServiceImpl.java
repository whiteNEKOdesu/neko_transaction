package neko.transaction.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.entity.ReturnApplyImage;
import neko.transaction.product.entity.ReturnApplyInfo;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ReturnApplyInfoMapper;
import neko.transaction.product.service.ReturnApplyImageService;
import neko.transaction.product.service.ReturnApplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewReturnApplyVo;
import neko.transaction.product.vo.ReturnApplyInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 退货申请表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Service
public class ReturnApplyInfoServiceImpl extends ServiceImpl<ReturnApplyInfoMapper, ReturnApplyInfo> implements ReturnApplyInfoService {
    @Resource
    private ReturnApplyImageService returnApplyImageService;

    @Resource
    private OSSFeignService ossFeignService;

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

        //step1 -> 添加退货申请信息
        ReturnApplyInfo returnApplyInfo = new ReturnApplyInfo();
        BeanUtil.copyProperties(vo, returnApplyInfo);
        this.baseMapper.insert(returnApplyInfo);

        //step2 -> 上传图片到oss
        ResultObject<List<String>> r = ossFeignService.uploadImages(vo.getFiles());
        if(!r.getResponseCode().equals(200)){
            throw new ThirdPartyServiceException("thirdparty微服务远程调用异常");
        }

        //step3 -> 添加退货申请图片信息
        List<ReturnApplyImage> returnApplyImages = new ArrayList<>();
        Long applyId = returnApplyInfo.getApplyId();
        for(String url : r.getResult()){
            ReturnApplyImage returnApplyImage = new ReturnApplyImage();
            returnApplyImage.setApplyId(applyId)
                    .setApplyImage(url);

            returnApplyImages.add(returnApplyImage);
        }

        returnApplyImageService.insertBatch(returnApplyImages);
    }

    /**
     * 根据 订单详情id 获取申请退货信息
     * @param orderDetailId 订单详情id
     * @return 申请退货信息
     */
    @Override
    public ReturnApplyInfoVo getReturnApplyInfoByOrderDetailId(String orderDetailId) {
        ReturnApplyInfoVo returnApplyInfo = this.baseMapper.getReturnApplyInfoByOrderDetailId(orderDetailId);
        if(returnApplyInfo == null){
            return null;
        }

        return returnApplyInfo.setReturnApplyImages(returnApplyImageService.list(new QueryWrapper<ReturnApplyImage>().lambda()
                .eq(ReturnApplyImage::getApplyId, returnApplyInfo.getApplyId())));
    }
}
