package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.entity.CollegeInfo;
import neko.transaction.member.service.CollegeInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 二级学院信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("college_info")
public class CollegeInfoController {
    @Resource
    private CollegeInfoService collegeInfoService;

    /**
     * 添加二级学院
     * @param collegeName 二级学院名
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("new_college_info")
    public ResultObject<Object> newCollegeInfo(@RequestParam String collegeName){
        collegeInfoService.newCollege(collegeName);

        return ResultObject.ok();
    }

    /**
     * 分页查询二级学院信息
     * @param vo 分页查询vo
     * @return 二级学院分页信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("college_info_page_query")
    public ResultObject<Page<CollegeInfo>> collegePageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(collegeInfoService.collegeInfoPageQuery(vo));
    }
}
