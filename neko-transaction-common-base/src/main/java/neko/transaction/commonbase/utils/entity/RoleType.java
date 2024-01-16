package neko.transaction.commonbase.utils.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限类型常量
 */
@Data
@Accessors(chain = true)
public class RoleType {
    public static final String ROOT = "root";

    public static final String ADMIN = "admin";
}
