package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加用户余额vo
 */
@Data
@Accessors(chain = true)
public class AddMemberBalanceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @NotBlank
    private String uid;

    /**
     * 添加的余额
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal addNumber;
}
