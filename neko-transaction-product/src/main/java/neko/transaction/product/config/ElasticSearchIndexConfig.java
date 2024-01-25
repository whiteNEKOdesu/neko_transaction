package neko.transaction.product.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.Constant;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * elasticsearch索引初始化
 */
@Component
@Slf4j
public class ElasticSearchIndexConfig {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void init() throws IOException {
        BooleanResponse response = elasticsearchClient.indices().exists(query -> query.index(Constant.ELASTIC_SEARCH_INDEX));

        if(!response.value()){
            log.warn("elasticsearch索引: " + Constant.ELASTIC_SEARCH_INDEX + " 不存在，正在尝试创建索引");

            Assert.isTrue(createIndex(), "elasticsearch索引: " + Constant.ELASTIC_SEARCH_INDEX + " 创建失败");

            log.info("elasticsearch索引: " + Constant.ELASTIC_SEARCH_INDEX + " 创建成功");
        }
    }

    /**
     * 创建索引
     */
    private boolean createIndex() throws IOException {
        CreateIndexResponse response = elasticsearchClient.indices().create(builder -> builder.index(Constant.ELASTIC_SEARCH_INDEX)
                .mappings(map -> map
                        .properties("productId", propertyBuilder -> propertyBuilder.long_(longProperty -> longProperty))
                        .properties("uid", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("userName", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("realName", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("categoryId", propertyBuilder -> propertyBuilder.integer(integerProperty -> integerProperty))
                        .properties("fullCategoryName", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("productName", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("description", propertyBuilder -> propertyBuilder.text(textProperty ->
                                textProperty.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("displayImage", propertyBuilder -> propertyBuilder.keyword(keyWordProperty -> keyWordProperty))
                        .properties("price", propertyBuilder -> propertyBuilder.scaledFloat(scaledFloatProperty ->
                                scaledFloatProperty.scalingFactor(100D)))
                        .properties("upTime", propertyBuilder -> propertyBuilder.date(dateProperty -> dateProperty.format("yyyy-MM-dd HH:mm:ss")))
                        .properties("saleNumber", propertyBuilder -> propertyBuilder.integer(integerProperty -> integerProperty))));

        return response.acknowledged();
    }
}
