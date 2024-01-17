package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 完整专业名vo
 */
@Data
@Accessors(chain = true)
public class FullMajorNameVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 专业id
     */
    private Integer majorId;

    /**
     * 完整专业名，由 二级学院-专业名 组成
     */
    private String fullMajorName;
}
