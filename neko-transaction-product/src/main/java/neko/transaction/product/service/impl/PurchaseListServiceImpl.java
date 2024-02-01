package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.exception.OutOfLimitationException;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.service.PurchaseListService;
import neko.transaction.product.vo.AddToPurchaseListVo;
import neko.transaction.product.vo.ProductDetailInfoVo;
import neko.transaction.product.vo.PurchaseListRedisTo;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Service
public class PurchaseListServiceImpl implements PurchaseListService {
    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加商品到购物车
     * @param vo 添加商品到购物车的vo
     */
    @Override
    public void addToPurchaseList(AddToPurchaseListVo vo) {
        String key = Constant.PRODUCT_REDIS_PREFIX + "purchase_list:" + StpUtil.getLoginId().toString();
        BoundHashOperations<String, Object, Object> boundHashOperations = stringRedisTemplate.boundHashOps(key);
        //要添加的商品信息
        List<AddToPurchaseListVo.PurchaseListProductInfo> todoAddProductInfos = vo.getProductInfos();
        Long size = boundHashOperations.size();
        //购物车最大存放 50 件商品
        if(size != null && size + todoAddProductInfos.size() > 50){
            throw new OutOfLimitationException("购物车数量超过限制");
        }

        //使用 stream 流收集商品id
        List<String> productIds = todoAddProductInfos.stream().map(AddToPurchaseListVo.PurchaseListProductInfo::getProductId)
                .collect(Collectors.toList());

        //获取商品详情信息
        List<ProductDetailInfoVo> productDetailInfo = productInfoService.getProductDetailInfoByIds(productIds);

        //map 映射关系: productId -> AddToPurchaseListVo.PurchaseListProductInfo
        Map<String,AddToPurchaseListVo.PurchaseListProductInfo> productMap = new HashMap<>();
        for(AddToPurchaseListVo.PurchaseListProductInfo todoAdd : todoAddProductInfos){
            productMap.put(todoAdd.getProductId(), todoAdd);
        }

        for(ProductDetailInfoVo productDetailInfoVo : productDetailInfo){
            String productId = productDetailInfoVo.getProductId();
            AddToPurchaseListVo.PurchaseListProductInfo purchaseListProductInfo = productMap.get(productId);
            PurchaseListRedisTo todoAdd;

            Boolean isAppend = boundHashOperations.hasKey(productId);
            //如果购物车中已经存在此商品，追加购物车商品数量
            if(isAppend != null && isAppend){
                String value = (String) boundHashOperations.get(productId);
                todoAdd = JSONUtil.toBean(value, PurchaseListRedisTo.class);
                //修改购买数量
                todoAdd.setNumber(todoAdd.getNumber() + purchaseListProductInfo.getNumber())
                        //修改总价
                        .setTotalPrice(todoAdd.getPrice()
                                .multiply(new BigDecimal(todoAdd.getNumber().toString()))
                                .setScale(2, BigDecimal.ROUND_DOWN));
            }else{
                todoAdd = new PurchaseListRedisTo();
                todoAdd.setProductId(productId)
                        .setUid(productDetailInfoVo.getUid())
                        .setCategoryId(productDetailInfoVo.getCategoryId())
                        .setFullCategoryName(productDetailInfoVo.getFullCategoryName())
                        .setProductName(productDetailInfoVo.getProductName())
                        .setDescription(productDetailInfoVo.getDescription())
                        .setDisplayImage(productDetailInfoVo.getDisplayImage())
                        .setPrice(productDetailInfoVo.getPrice())
                        .setUpTime(productDetailInfoVo.getUpTime())
                        .setSaleNumber(productDetailInfoVo.getSaleNumber())
                        .setNumber(purchaseListProductInfo.getNumber())
                        .setTotalPrice(productDetailInfoVo.getPrice()
                                .multiply(new BigDecimal(purchaseListProductInfo.getNumber().toString()))
                                .setScale(2, BigDecimal.ROUND_DOWN));
            }

            //添加到 redis 中
            boundHashOperations.put(productId, JSONUtil.toJsonStr(todoAdd));
        }
    }

    /**
     * 获取用户自身的购物车全部商品信息
     * @return 用户自身的购物车全部商品信息
     */
    @Override
    public List<PurchaseListRedisTo> userSelfPurchaseListInfos() {
        String uid = StpUtil.getLoginId().toString();
        String key = Constant.PRODUCT_REDIS_PREFIX + "purchase_list:" + uid;
        BoundHashOperations<String, Object, Object> boundHashOperations = stringRedisTemplate.boundHashOps(key);
        List<Object> values = boundHashOperations.values();
        if(values == null || values.isEmpty()){
            return null;
        }

        List<PurchaseListRedisTo> result = new ArrayList<>();
        for(Object value : values){
            result.add(JSONUtil.toBean(value.toString(), PurchaseListRedisTo.class));
        }

        return result;
    }
}
