package neko.transaction.product.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.exception.ElasticSearchUpdateException;
import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.elasticsearch.service.ProductInfoESService;
import neko.transaction.product.vo.ProductInfoESQueryVo;
import neko.transaction.product.vo.ProductInfoESVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品信息 elasticsearch 查询服务实现类
 */
@Service
@Slf4j
public class ProductInfoESServiceImpl implements ProductInfoESService {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * 商品信息 elasticsearch 查询
     * @param vo 查询vo
     * @return 查询结果
     */
    @Override
    public ProductInfoESVo productInfoPageQuery(ProductInfoESQueryVo vo) throws IOException {
        //构建查询请求
        SearchRequest request = buildSearchRequest(vo);
        log.info("elasticsearch语句: " + request.toString());

        //获取查询结果
        SearchResponse<ProductInfoES> response = elasticsearchClient.search(buildSearchRequest(vo), ProductInfoES.class);

        //拆解查询响应结果为vo
        ProductInfoESVo searchVo = getSearchVo(response);

        return searchVo.setSize(vo.getLimited())
                .setCurrent(vo.getCurrentPage());
    }

    /**
     * 添加商品信息到 elasticsearch中
     */
    @Override
    public void newProductInfoToES(ProductInfoES productInfoES) {
        BulkRequest.Builder builder = new BulkRequest.Builder();
        //设置 elasticsearch 索引
        builder.operations(operation->operation.index(idx->idx.index(Constant.ELASTIC_SEARCH_INDEX)
                //设置文档id
                .id(productInfoES.getProductId())
                //设置文档数据
                .document(productInfoES)));

        BulkResponse bulkResponse;
        try {
            //添加文档到 elasticsearch
            bulkResponse = elasticsearchClient.bulk(builder.build());
        }catch (Exception e){
            throw new ElasticSearchUpdateException("elasticsearch添加错误");
        }
        if(bulkResponse.errors()){
            throw new ElasticSearchUpdateException("elasticsearch添加错误");
        }
    }

    /**
     * 构建商品查询请求
     */
    private SearchRequest buildSearchRequest(ProductInfoESQueryVo vo){
        SearchRequest.Builder builder = new SearchRequest.Builder();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if(StringUtils.hasText(vo.getUid())){
            //按照商品售卖者的学号筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("uid")
                                    .value(vo.getUid())));
        }

        if(vo.getCategoryId() != null){
            //按照分类id筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("categoryId")
                                    .value(vo.getCategoryId())));
        }

        LocalDateTime minTime = vo.getMinTime(), maxTime = vo.getMaxTime();
        if(minTime != null && maxTime != null && minTime.compareTo(maxTime) < 0){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //按照上架时间范围筛选
            boolBuilder.filter(f ->
                    f.range(r ->
                            r.field("upTime")
                                    .gte(JsonData.of(minTime.format(dateTimeFormatter)))
                                    .lte(JsonData.of(maxTime.format(dateTimeFormatter)))));
        }

        BigDecimal minPrice = vo.getMinPrice(), maxPrice = vo.getMaxPrice();
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) < 0){
            //按照价格范围筛选
            boolBuilder.filter(f ->
                    f.range(r ->
                            r.field("price")
                                    .gte(JsonData.of(minPrice))
                                    .lte(JsonData.of(maxPrice))));
        }

        String queryWords = vo.getQueryWords();
        if(StringUtils.hasText(queryWords)){
            //关键字查询
            boolBuilder.must(m ->
                            m.match(mt ->
                                    mt.field("description")
                                            .query(queryWords)))
                    .should(s -> s.match(m -> m.field("productName").query(queryWords)))
                    .should(s -> s.match(m -> m.field("categoryName").query(queryWords)))
                    .should(s -> s.match(m -> m.field("userName").query(queryWords)))
                    .should(s -> s.match(m -> m.field("realName").query(queryWords)));
        }

        return builder.index(Constant.ELASTIC_SEARCH_INDEX)
                .query(q ->
                        q.bool(boolBuilder.build()))
                //设置起始页
                .from(vo.getFrom())
                //设置每页大小
                .size(vo.getLimited())
                .sort(s -> s.field(f ->
                        //价格排序
                        f.field("price")
                                .order(vo.getIsPriceAscSort() ? SortOrder.Asc : SortOrder.Desc)
                                //上架时间排序
                                .field("upTime")
                                .order(vo.getIsUpTimeAscSort() ? SortOrder.Asc : SortOrder.Desc)))
                //设置高亮
                .highlight(h ->
                        h.fields("description", hf -> hf)
                                .preTags("<b style='color:red'>")
                                .postTags("</b>"))
                .build();
    }

    /**
     * 拆解查询响应结果为vo
     */
    private ProductInfoESVo getSearchVo(SearchResponse<ProductInfoES> response){
        ProductInfoESVo productInfoESVo = new ProductInfoESVo();
        List<ProductInfoES> result = new ArrayList<>();

        List<Hit<ProductInfoES>> hits = response.hits().hits();
        if(hits != null && !hits.isEmpty()){
            for(Hit<ProductInfoES> hit : hits){
                //获取结果
                ProductInfoES productInfoES = hit.source();
                if(productInfoES == null){
                    continue;
                }

                //获取结果高亮 map
                Map<String, List<String>> highlightMap = hit.highlight();
                ProductInfoES.HighLight highLight = new ProductInfoES.HighLight();
                //设置 商品名，商品描述 的高亮属性
                highLight.setProductName(highlightMap.get("productName"))
                                .setDescription(highlightMap.get("description"));
                productInfoES.setHighLight(highLight);

                //添加到查询结果 list
                result.add(productInfoES);
            }
        }

        return productInfoESVo.setRecords(result)
                .setTotal(response.hits().total() != null ? Integer.parseInt(response.hits().total().value() + "") : 0);
    }
}
