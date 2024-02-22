package neko.transaction.product.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.ProductStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.elasticsearch.service.ProductInfoESService;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.service.OrderDetailInfoService;
import neko.transaction.product.service.ProductCommentService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.to.MemberInfoTo;
import neko.transaction.product.vo.NewProductCommentVo;
import neko.transaction.product.vo.ProductDetailInfoVo;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Service
@Slf4j
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private ProductCommentService productCommentService;

    @Resource
    private OrderDetailInfoService orderDetailInfoService;

    @Resource
    private ProductInfoESService productInfoESService;

    @Resource
    private OSSFeignService ossFeignService;

    @Resource
    private MemberInfoFeignService memberInfoFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询学生自身的商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductInfoVo> userSelfPageQuery(QueryVo vo) {
        Page<ProductInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = (String) StpUtil.getLoginId();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.userSelfPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.userSelfPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    @Override
    public ProductInfoVo getUserSelfProductInfoById(String productId) {
        return this.baseMapper.getUserSelfProductInfoById(productId, (String) StpUtil.getLoginId());
    }

    /**
     * 修改商品信息
     * @param vo 修改商品信息vo
     */
    @Override
    public void updateProductInfo(UpdateProductInfoVo vo) {
        if(vo.getProductName() == null && vo.getDescription() == null &&
                vo.getCategoryId() == null && vo.getPrice() == null && vo.getDisplayImage() == null){
            return;
        }

        String productId = vo.getProductId();
        ProductInfo productInfo = this.baseMapper.selectById(productId);
        if(productInfo == null){
            throw new IllegalArgumentException("productId对应商品信息不存在");
        }
        String uid = StpUtil.getLoginId().toString();
        //商品不属于该用户
        if(!productInfo.getUid().equals(uid)){
            throw new NotPermissionException("访问商品信息权限不足");
        }

        if(vo.getCategoryId() != null){
            CategoryInfo categoryInfo = categoryInfoService.getById(vo.getCategoryId());
            if(categoryInfo != null && !categoryInfo.getLevel().equals(1)){
                throw new IllegalArgumentException("分类id对应的分类不为叶节点");
            }
        }

        //上传图片的url
        String uploadUrl = null;
        //图片文件
        MultipartFile image = vo.getDisplayImage();

        //step1 -> 上传图片到 oss
        if(image != null){
            if(image.getSize() > Constant.IMAGE_MAX_SIZE){
                throw new IllegalArgumentException("图片超出上传大小限制");
            }

            ResultObject<String> r = ossFeignService.uploadImage(image);
            if(!r.getResponseCode().equals(200)){
                throw new ThirdPartyServiceException("third_party微服务远程调用异常");
            }

            uploadUrl = r.getResult();
        }

        //step2 -> 修改商品信息表
        ProductInfo todoUpdate = new ProductInfo();
        BeanUtil.copyProperties(vo, todoUpdate);
        //设置新商品展示图片
        todoUpdate.setDisplayImage(uploadUrl);
        this.baseMapper.updateById(todoUpdate);

        //step3 -> 删除 oss 原商品展示图片
        if(uploadUrl != null){
            ossFeignService.deleteFile(productInfo.getDisplayImage());
        }

        //step4 -> 删除商品详细信息缓存
        String key = Constant.PRODUCT_REDIS_PREFIX + "product_detail_info:" + productId;
        stringRedisTemplate.delete(key);

        //step5 -> 修改 elasticsearch 商品信息
        ProductInfoES productInfoES = new ProductInfoES();
        ProductInfoVo productInfoVo = this.baseMapper.getUserSelfProductInfoById(productId, uid);
        //将商品信息复制到 elasticsearch 实体类
        BeanUtil.copyProperties(productInfoVo, productInfoES);
        productInfoES.setUpTime(productInfoVo.getUpTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //修改 elasticsearch 商品信息
        productInfoESService.updateProductInfo(productInfoES);
    }

    /**
     * 上架商品
     * @param productId 商品id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upProduct(String productId) {
        String uid = StpUtil.getLoginId().toString();
        //step1 -> 获取商品信息
        ProductInfoVo productInfo = this.baseMapper.getUserSelfProductInfoById(productId, uid);
        if(productInfo == null){
            throw new NoSuchResultException("无此productId商品信息");
        }

        //step2 -> 修改商品信息状态为上架状态
        ProductInfo todoUpdate = new ProductInfo();
        //上架时间
        LocalDateTime now = LocalDateTime.now();
        todoUpdate.setProductId(productId)
                .setStatus(ProductStatus.UP)
                .setUpTime(now);
        //修改商品信息表
        this.baseMapper.updateById(todoUpdate);

        //step3 -> 远程调用用户微服务获取用户信息
        ResultObject<MemberInfoTo> r = memberInfoFeignService.userSelfInfo(StpUtil.getTokenValue());
        if(!r.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常，code: " + r.getResponseCode());
        }

        //获取远程调用用户信息结果
        MemberInfoTo memberInfoTo = r.getResult();

        //step4 -> 向 elasticsearch 添加商品信息
        ProductInfoES productInfoES = new ProductInfoES();
        //将商品信息复制到 elasticsearch 实体类
        BeanUtil.copyProperties(productInfo, productInfoES);
        productInfoES.setUserName(memberInfoTo.getUserName())
                .setRealName(memberInfoTo.getRealName())
                .setUpTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        //elasticsearch 添加商品信息
        productInfoESService.newProductInfoToES(productInfoES);

        log.info("商品上架成功，productId: " + productId);
    }

    /**
     * 下架商品
     * @param productId 商品id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downProduct(String productId) {
        //step1 -> 获取商品信息，校验权限
        ProductInfo productInfo = this.baseMapper.selectById(productId);
        String uid = StpUtil.getLoginId().toString();
        if(productInfo == null || !productInfo.getUid().equals(uid)){
            throw new NotPermissionException("商品不属于此用户");
        }

        //step2 -> 修改商品状态为下架状态
        this.baseMapper.downProduct(productId, uid);

        //step3 -> 删除 elasticsearch 数据
        productInfoESService.deleteByProductId(productId);

        log.info("商品下架成功，productId: " + productId);
    }

    /**
     * 获取上架的商品详情信息
     * @param productId 商品id
     * @return 商品详情信息
     */
    @Override
    public ProductDetailInfoVo getUpProductDetailInfo(String productId) {
        String key = Constant.PRODUCT_REDIS_PREFIX + "product_detail_info:" + productId;
        //获取 redis 缓存
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return JSONUtil.toBean(cache, ProductDetailInfoVo.class);
        }

        //获取商品详情信息
        ProductDetailInfoVo productDetailInfoVo = this.baseMapper.getUpProductDetailInfo(productId);
        if(productDetailInfoVo == null){
            throw new NoSuchResultException("无此商品信息");
        }

        //缓存无数据，将查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(productDetailInfoVo),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return productDetailInfoVo;
    }

    /**
     * 根据商品id集合获取商品详情信息
     * @param productIds 商品id集合
     * @return 商品id集合对应的商品详情信息
     */
    @Override
    public List<ProductDetailInfoVo> getProductDetailInfoByIds(List<String> productIds) {
        return this.baseMapper.getProductDetailInfoByIds(productIds);
    }

    /**
     * 添加销量
     * @param productId 商品id
     * @param increase 要添加的数量
     */
    @Override
    public void increaseSaleNumber(String productId, Integer increase) {
        String key = Constant.PRODUCT_REDIS_PREFIX + "product_sale_number:" + productId;
        //添加 redis 中缓冲的销量
        Long saleNumber = stringRedisTemplate.opsForValue().increment(key, increase);

        if(saleNumber == null){
            stringRedisTemplate.opsForValue().setIfAbsent(key,
                    increase.toString());

            return;
        }

        if(saleNumber >= 10){
            //修改商品信息表销量信息
            this.baseMapper.increaseSaleNumber(productId, saleNumber);

            //将redis中销量设为 0
            stringRedisTemplate.opsForValue().set(key,
                    "0");

            //修改elasticsearch中商品信息数据
            ProductInfo productInfo = this.baseMapper.selectById(productId);
            ProductInfoES productInfoES = new ProductInfoES();
            productInfoES.setProductId(productId)
                    .setSaleNumber(productInfo.getSaleNumber());

            productInfoESService.updateProductInfo(productInfoES);
        }
    }

    /**
     * 添加商品评论
     * @param vo 添加商品评论vo
     */
    @Override
    public void newProductComment(NewProductCommentVo vo) {
        if(!orderDetailInfoService.isReceivedOrderDetailInfoExist(vo.getOrderId(),
                vo.getProductId(),
                StpUtil.getLoginId().toString())){
            throw new NotPermissionException("用户不存在此已收货的商品信息");
        }

        productCommentService.newProductComment(vo);
    }

    /**
     * 获取销量前 8 的商品信息
     * @return 销量前 8 的商品信息
     */
    @Override
    public List<ProductInfo> getTop8SaleNumberProductInfos() {
        String key = Constant.PRODUCT_REDIS_PREFIX + "top_8_sale_number_product_infos";
        //获取 redis 缓存
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return JSONUtil.toList(cache, ProductInfo.class);
        }

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductInfo::getStatus, ProductStatus.UP)
                .eq(ProductInfo::getIsBan, false)
                .orderByDesc(ProductInfo::getSaleNumber)
                .last("limit 8");
        //获取销量前 8 的销量信息
        List<ProductInfo> productInfos = this.baseMapper.selectList(queryWrapper);
        if(productInfos == null){
            return null;
        }

        //缓存无数据，将查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(productInfos),
                1,
                TimeUnit.HOURS);

        return productInfos;
    }
}
