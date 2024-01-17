package neko.transaction.member.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.service.ClassInfoService;
import neko.transaction.member.vo.ClassInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
