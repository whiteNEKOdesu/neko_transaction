package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.service.ClassInfoService;
import neko.transaction.member.vo.ClassInfoVo;
import neko.transaction.member.vo.NewClassInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 学生班级信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("class_info")
public class ClassInfoController {
    @Resource
    private ClassInfoService classInfoService;

    /**
     * 分页查询班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @PostMapping("class_info_page_query")
    public ResultObject<Page<ClassInfoVo>> pageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(classInfoService.pageQuery(vo));
    }

    /**
     * 添加班级信息
     * @param vo 添加班级信息的vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PutMapping("new_class_info")
    public ResultObject<Object> newClassInfo(@Validated @RequestBody NewClassInfoVo vo){
        classInfoService.newClassInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 根据班级id删除班级信息
     * @param classId 班级id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @DeleteMapping("delete_by_id")
    public ResultObject<Object> deleteById(@RequestParam String classId){
        classInfoService.deleteById(classId);

        return ResultObject.ok();
    }
}
