package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.vo.LogInVo;
import neko.transaction.member.vo.MemberInfoVo;
import neko.transaction.member.vo.MemberWithSchoolInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 学生信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface MemberInfoService extends IService<MemberInfo> {
    /**
     * 用户登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    ResultObject<MemberInfoVo> login(LogInVo vo, HttpServletRequest request);

    /**
     * 分页查询学生及所属二级学院，专业，班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<MemberWithSchoolInfoVo> memberWithSchoolInfoPageQuery(QueryVo vo);
}
