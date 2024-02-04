package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.MemberChatInfo;
import neko.transaction.member.service.MemberChatInfoService;
import neko.transaction.member.vo.NewMemberChatVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 聊天消息信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
@RestController
@RequestMapping("member_chat_info")
public class MemberChatInfoController {
    @Resource
    private MemberChatInfoService memberChatInfoService;

    /**
     * 添加聊天消息
     * @param vo 添加聊天消息的vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("new_chat_info")
    public ResultObject<Object> newChatInfo(@Validated @RequestBody NewMemberChatVo vo){
        memberChatInfoService.newMemberChatInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询指定的聊天对象学号的聊天信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckLogin
    @PostMapping("page_query_by_chat_id")
    public ResultObject<Page<MemberChatInfo>> pageQueryByChatId(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(memberChatInfoService.pageQueryByToId(vo));
    }
}
