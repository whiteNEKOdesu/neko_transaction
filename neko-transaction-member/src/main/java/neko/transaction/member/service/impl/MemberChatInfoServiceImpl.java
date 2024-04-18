package neko.transaction.member.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.ChatWebSocketMessageType;
import neko.transaction.commonbase.utils.entity.MQMessageType;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.RabbitMqConstant;
import neko.transaction.member.config.WebSocket;
import neko.transaction.member.entity.MemberChatInfo;
import neko.transaction.member.mapper.MemberChatInfoMapper;
import neko.transaction.member.service.MemberChatInfoReadLogService;
import neko.transaction.member.service.MemberChatInfoService;
import neko.transaction.member.to.RabbitMQChatPubTo;
import neko.transaction.member.to.RabbitMQMessageTo;
import neko.transaction.member.vo.ChatWebSocketVo;
import neko.transaction.member.vo.MemberChatInfoLogVo;
import neko.transaction.member.vo.NewMemberChatVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <p>
 * 聊天消息信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
@Service
public class MemberChatInfoServiceImpl extends ServiceImpl<MemberChatInfoMapper, MemberChatInfo> implements MemberChatInfoService {
    @Resource
    private MemberChatInfoReadLogService memberChatInfoReadLogService;

    @Resource
    private WebSocket webSocket;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "threadPoolExecutor")
    private Executor threadPool;

    /**
     * 添加聊天消息
     * @param vo 添加聊天消息的vo
     */
    @Override
    public void newMemberChatInfo(NewMemberChatVo vo) {
        String uid = StpUtil.getLoginId().toString();
        String toId = vo.getToId();
        String body = vo.getBody();
        MemberChatInfo memberChatInfo = new MemberChatInfo();
        memberChatInfo.setFromId(uid)
                .setToId(toId)
                .setBody(body);

        //向聊天消息信息表添加聊天消息
        this.baseMapper.insert(memberChatInfo);

        //异步向聊天消息发布队列发送聊天消息，用于聊天消息同步发送
        CompletableFuture.runAsync(() -> {
            //构造消息
            ChatWebSocketVo chatWebSocketVo = new ChatWebSocketVo();
            LocalDateTime now = LocalDateTime.now();
            memberChatInfo.setCreateTime(now)
                            .setUpdateTime(now);
            chatWebSocketVo.setType(ChatWebSocketMessageType.CHAT)
                    .setMessage(JSONUtil.toJsonStr(memberChatInfo));

            //构造 rabbitmq 聊天消息发布to
            RabbitMQChatPubTo rabbitMQChatPubTo = new RabbitMQChatPubTo();
            rabbitMQChatPubTo.setChatId(memberChatInfo.getChatId())
                    .setFromId(uid)
                    .setToId(vo.getToId())
                    .setMessage(JSONUtil.toJsonStr(chatWebSocketVo));

            RabbitMQMessageTo<RabbitMQChatPubTo> rabbitMQMessageTo = RabbitMQMessageTo.generateMessage(rabbitMQChatPubTo, MQMessageType.CHAT_PUB_TYPE);
            //在CorrelationData中设置回退消息
            CorrelationData correlationData = new CorrelationData(MQMessageType.CHAT_PUB_TYPE.toString());
            String jsonMessage = JSONUtil.toJsonStr(rabbitMQMessageTo);
            String notAvailable = "not available";
            correlationData.setReturned(new ReturnedMessage(new Message(jsonMessage.getBytes(StandardCharsets.UTF_8)),
                    0,
                    notAvailable,
                    notAvailable,
                    notAvailable));
            //向聊天消息发布队列发送聊天消息，用于聊天消息同步发送
            rabbitTemplate.convertAndSend(RabbitMqConstant.CHAT_PUB_EXCHANGE_NAME,
                    "",
                    jsonMessage,
                    correlationData);
        }, threadPool).exceptionallyAsync(e -> {
            e.printStackTrace();

            return null;
        }, threadPool);
    }

    /**
     * 分页查询指定的聊天对象学号的聊天信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<MemberChatInfo> pageQueryByToId(QueryVo vo) {
        if(vo.getObjectId() == null){
            throw new IllegalArgumentException("缺少聊天对象uid");
        }
        Page<MemberChatInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String chatId = vo.getObjectId().toString();
        String uid = StpUtil.getLoginId().toString();
        if(vo.getCurrentPage().equals(1)){
            //异步添加已读消息记录，将消息标记为已读
            CompletableFuture.runAsync(() -> {
                //获取未读消息id
                List<Long> chatIds = this.baseMapper.getUnreadChatIdByFromIdToId(chatId, uid);
                //将消息标记为已读
                memberChatInfoReadLogService.newMemberChatInfoReadLog(chatIds, chatId, uid);
            }, threadPool).exceptionallyAsync(e -> {
                e.printStackTrace();

                return null;
            }, threadPool);
        }

        QueryWrapper<MemberChatInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberChatInfo::getToId, chatId)
                .eq(MemberChatInfo::getFromId, uid)
                .or()
                .eq(MemberChatInfo::getToId, uid)
                .eq(MemberChatInfo::getFromId, chatId)
                .orderByDesc(MemberChatInfo::getChatId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 分页查询用户自身的聊天对象信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<MemberChatInfoLogVo> memberChattingWithPageQuery(QueryVo vo) {
        Page<MemberChatInfoLogVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = StpUtil.getLoginId().toString();
        //设置分页查询结果
        page.setRecords(this.baseMapper.memberChattingWithPageQuery(vo.getLimited(),
                vo.daoPage(),
                uid));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.memberChattingWithPageQueryNumber(uid));

        return page;
    }

    /**
     * 根据发送人学号，接收人学号获取未读聊天id
     * @param fromId 送人学号
     * @param toId 接收人学号
     * @return 未读聊天id
     */
    @Override
    public List<Long> getUnreadChatIdByFromIdToId(String fromId, String toId) {
        return this.baseMapper.getUnreadChatIdByFromIdToId(fromId, toId);
    }
}
