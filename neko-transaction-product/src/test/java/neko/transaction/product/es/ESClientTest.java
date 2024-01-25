package neko.transaction.product.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.service.ProductInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ESClientTest {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Resource
    private ProductInfoService productInfoService;

    @Test
    public void upProduct() throws IOException {
        List<ProductInfoES> productInfoESs = new ArrayList<>();
        productInfoESs.add(new ProductInfoES().setProductId("1750067945863770113")
                .setUid("1642067605873348610")
                .setUserName("NEKO")
                .setRealName("NEKO")
                .setCategoryId(17)
                .setFullCategoryName("电子产品-数据资源")
                .setProductName("插画")
                .setDescription("插画，云盘链接发货")
                .setDisplayImage("https://neko-bucket.oss-cn-shanghai.aliyuncs.com/neko/neko_transaction/2024-01-19/c3a80147-b81d-4991-90da-fceb7d1543d8_1ad3ffe0-e81f-4439-8937-2fc22e2045ba_A400388D-AE79-4F23-8390-7D549A78D795.jpeg")
                .setPrice(new BigDecimal("15"))
                .setUpTime("2023-07-17 11:29:49"));
        productInfoESs.add(new ProductInfoES().setProductId("1750068049572130817")
                .setUid("1642067605873348610")
                .setUserName("NEKO")
                .setRealName("NEKO")
                .setCategoryId(17)
                .setFullCategoryName("电子产品-数据资源")
                .setProductName("插画")
                .setDescription("插画，云盘链接发货")
                .setDisplayImage("https://neko-bucket.oss-cn-shanghai.aliyuncs.com/neko/neko_transaction/2024-01-19/c3a80147-b81d-4991-90da-fceb7d1543d8_1ad3ffe0-e81f-4439-8937-2fc22e2045ba_A400388D-AE79-4F23-8390-7D549A78D795.jpeg")
                .setPrice(new BigDecimal("15"))
                .setUpTime("2023-07-17 11:29:49"));
        BulkRequest.Builder builder = new BulkRequest.Builder();
        for(ProductInfoES productInfoES : productInfoESs){
            builder.operations(operation->operation.index(idx->idx.index(Constant.ELASTIC_SEARCH_INDEX)
                    .id(productInfoES.getProductId())
                    .document(productInfoES)));
        }
        BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
        System.out.println(bulkResponse);
    }

    @Test
    public void downSingleVideo() throws IOException {
        DeleteResponse response = elasticsearchClient.delete(builder ->
                builder.index("neko_movie")
                        .id("5"));
        System.out.println(response.shards().successful().intValue());
    }

    @Test
    public void downMultiplyVideo() throws IOException {
        DeleteByQueryResponse response = elasticsearchClient.deleteByQuery(builder ->
                builder.index("neko_movie")
                        .query(q ->
                                q.term(t ->
                                        t.field("videoInfoId")
                                                .value(5))));
        System.out.println(response.deleted());
    }

    @Test
    public void getIndex() throws IOException {
        BooleanResponse response = elasticsearchClient.indices().exists(query -> query.index(Constant.ELASTIC_SEARCH_INDEX));
        System.out.println(response.value());
    }

    @Test
    public void createIndex() throws IOException {
        CreateIndexResponse response = elasticsearchClient.indices().create(builder -> builder.index(Constant.ELASTIC_SEARCH_INDEX)
                .mappings(map -> map
                        .properties("videoInfoId", propertyBuilder -> propertyBuilder.long_(longProperty -> longProperty))
                        .properties("videoName", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("videoDescription", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("videoImage", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("categoryId", propertyBuilder -> propertyBuilder.long_(longProperty -> longProperty))
                        .properties("categoryName", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("videoProducer", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("videoActors", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("upTime", propertyBuilder -> propertyBuilder.date(dateProperty -> dateProperty.format("yyyy-MM-dd HH:mm:ss")))));
        System.out.println(response.acknowledged());
    }

    @Test
    public void videoCategoryAggTest() throws IOException {
        Query query = MatchAllQuery.of(m -> m)._toQuery();

        SearchResponse<Void> response = elasticsearchClient.search(b -> b
                        .index(Constant.ELASTIC_SEARCH_INDEX)
                        .size(0)
                        .query(query)
                        .aggregations("categoryTermsAgg", a -> a.terms(h -> h
                                .field("categoryName"))
                                .aggregations("categoryAvgAgg", categoryAvgAgg -> categoryAvgAgg.avg(h -> h
                                        .field("playNumber"))
                                )
                        ),
                Void.class
        );

        List<StringTermsBucket> ageTermsAgg = response.aggregations()
                .get("categoryTermsAgg")
                .sterms()
                .buckets()
                .array();

        for (StringTermsBucket bucket: ageTermsAgg) {
            System.out.println("分类: " + bucket.key().stringValue() + " 有" + bucket.docCount() +
                    " 个影视视频，平均播放量为: " + bucket.aggregations().get("categoryAvgAgg").avg().value());
        }
    }

//    @Test
//    public void upAll() throws IOException {
//        List<VideoInfo> videoInfos = videoInfoService.getBaseMapper().selectList(new QueryWrapper<VideoInfo>().lambda()
//                .ne(VideoInfo::getStatus, VideoStatus.LOGIC_DELETE)
//                .ne(VideoInfo::getStatus, VideoStatus.DELETED));
//        for(VideoInfo videoInfo : videoInfos){
//            videoInfoService.upVideo(videoInfo.getVideoInfoId());
//        }
//    }
}
