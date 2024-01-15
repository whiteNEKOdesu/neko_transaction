package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.AdminInfo;
import neko.transaction.member.vo.AdminInfoVo;
import neko.transaction.member.vo.LogInVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface AdminInfoService extends IService<AdminInfo> {
    /**
     * 管理员登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 管理员信息vo
     */
    ResultObject<AdminInfoVo> login(LogInVo vo, HttpServletRequest request);
}
