package neko.transaction.product.service;

import neko.transaction.product.vo.AddToPurchaseListVo;
import neko.transaction.product.vo.PurchaseListRedisTo;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
public interface PurchaseListService {
    /**
     * 添加商品到购物车
     * @param vo 添加商品到购物车的vo
     */
    void addToPurchaseList(AddToPurchaseListVo vo);

    /**
     * 获取用户自身的购物车全部商品信息
     * @return 用户自身的购物车全部商品信息
     */
    List<PurchaseListRedisTo> userSelfPurchaseListInfos();
}
