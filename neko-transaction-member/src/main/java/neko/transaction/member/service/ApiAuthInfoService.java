package neko.transaction.member.service;

import neko.transaction.member.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * api鉴权信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
public interface ApiAuthInfoService extends IService<ApiAuthInfo> {
    /**
     * 将 api 信息同步到数据库
     */
    void synchronizeApiToDB();
}
