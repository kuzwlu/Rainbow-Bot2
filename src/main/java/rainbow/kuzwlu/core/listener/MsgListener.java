package rainbow.kuzwlu.core.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.*;
import love.forte.simbot.component.mirai.message.event.MiraiGroupMsg;

@Beans
public class MsgListener {

    /**
     * ● 监听私聊消息
     */
    @OnGroup
    public void groupMsg(){}

    /**
     * ● 监听群聊消息
     */
    @OnPrivate
    public void privateMsg(){}

    /**
     * ● 私聊消息撤回事件
     */
    @OnPrivateMsgRecall
    public void privateMsgRecall(){}

    /**
     * ● 群聊消息撤回事件
     */
    @OnGroupMsgRecall
    public void groupMsgRecall(){}

    /*
     * ● 监听好友请求事件
     */
    @OnFriendAddRequest
    public void friendAddRequest(){}

    /**
     * ● 监听好友头像变动事件
     */
    @OnFriendAvatarChanged
    public void friendAvatarChanged(){}

    /**
     * ● 监听好友增加事件
     */
    @OnFriendIncrease
    public void friendIncrease(){}

    /**
     * ● 监听好友昵称变动事件
     */
    @OnFriendNicknameChanged
    public void friendNicknameChanged(){}

    /**
     * ● 监听好友减少事件
     */
    @OnFriendReduce
    public void friendReduce(){}

    /**
     * ● 监听群友增加事件
     */
    @OnGroupMemberIncrease
    public void groupMemberIncrease(){}

    /**
     * ● 监听群友减少事件
     */
    @OnGroupMemberReduce
    public void groupMemberReduce(){}

    /**
     * ● 监听群添加请求事件
     */
    @OnGroupAddRequest
    public void groupAddRequest(){}

    /**
     * ● 监听群成员权限变动事件
     */
    @OnGroupMemberPermissionChanged
    public void groupMemberPermissionChanged(){}

    /**
     * ● 监听群名称变动事件
     */
    @OnGroupNameChanged
    public void groupNameChanged(){}

    /**
     * ● 监听群友群名片变动事件
     */
    @OnGroupMemberRemarkChanged
    public void groupMemberRemarkChanged(){}

    /**
     * ● 监听群友头衔变动事件
     */
    @OnGroupMemberSpecialChanged
    public void groupMemberSpecialChanged(){}

}
