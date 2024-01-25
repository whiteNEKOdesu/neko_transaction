package neko.transaction.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息 elasticsearch 查询vo
 */
@Data
@Accessors(chain = true)
public class ProductInfoESQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品售卖者的学号
     */
    private String uid;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 查询关键字
     */
    private String queryWords;

    /**
     * 是否按价格升序排序
     */
    private Boolean isPriceAscSort = true;

    /**
     * 是否按上架时间升序排序
     */
    private Boolean isUpTimeAscSort = false;

    /**
     * 最小价格
     */
    @Min(value = 0)
    private BigDecimal minPrice;

    /**
     * 最大价格
     */
    @Min(value = 0)
    private BigDecimal maxPrice;

    /**
     * 最早上架时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime minTime;

    /**
     * 最晚上架时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime maxTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 50)
    private Integer currentPage;

    @NotNull
    @Min(value = 3)
    @Max(value = 50)
    private Integer limited;

    public Integer getFrom(){
        return (currentPage - 1) * limited;
    }
}
