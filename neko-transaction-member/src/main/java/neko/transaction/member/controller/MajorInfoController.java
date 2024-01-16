package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.service.MajorInfoService;
import neko.transaction.member.vo.NewMajorInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 学生专业信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("major_info")
public class MajorInfoController {
    @Resource
    private MajorInfoService majorInfoService;

    /**
     * 添加专业信息
     * @param vo 添加专业信息vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("new_major_info")
    public ResultObject<Object> newMajorInfo(@Validated @RequestBody NewMajorInfoVo vo){
        majorInfoService.newMajorInfo(vo);

        return ResultObject.ok();
    }
}
