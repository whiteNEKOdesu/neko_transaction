package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.UpdateApiAuthInfoVo;

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

    /**
     * 分页查询 api 鉴权信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ApiAuthInfo> pageQuery(QueryVo vo);

    /**
     * 根据当前请求路径获取 api 鉴权信息
     * @return api 鉴权信息
     */
    ApiAuthInfo getApiAuthInfoByCurrentRequest();

    /**
     * 修改 api 鉴权信息
     * @param vo 修改 api 鉴权信息vo
     */
    void updateApiAuthInfo(UpdateApiAuthInfoVo vo);
}
