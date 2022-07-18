package rainbow.kuzwlu.core.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.*;
import love.forte.simbot.component.mirai.message.event.*;
import love.forte.simbot.constant.PriorityConstant;
import net.mamoe.mirai.event.events.NudgeEvent;

@Beans
@Listener
@ListenGroup("Plugins")
public class PluginsListener {

    /**
     * bot被戳一戳
     */

    @Listen(MiraiNudgedEvent.ByFriend.class)
    @Priority(PriorityConstant.FIRST)
    @ListenBreak
    public void nudgeEventByFriend(){}

    /**
     * bot被戳一戳
     */

    @Listen(MiraiNudgedEvent.ByMember.class)
    @Priority(PriorityConstant.SECOND)
    @ListenBreak
    public void nudgeEventByMember(){}

    /**
     * ● 私聊消息撤回事件
     */

    @OnPrivateMsgRecall
    @Priority(PriorityConstant.THIRD)
    @ListenBreak
    public void privateMsgRecall(){}

    /**
     * ● 群聊消息撤回事件
     */

    @OnGroupMsgRecall
    @Priority(PriorityConstant.FOURTH)
    @ListenBreak
    public void groupMsgRecall(){}

    /*
     * ● 监听好友请求事件
     */

    @OnFriendAddRequest
    @Priority(PriorityConstant.FIFTH)
    @ListenBreak
    public void friendAddRequest(){}

    /**
     * ● 监听好友头像变动事件
     */

    @OnFriendAvatarChanged
    @Priority(PriorityConstant.SIXTH)
    @ListenBreak
    public void friendAvatarChanged(){}

    /**
     * ● 监听好友增加事件
     */

    @OnFriendIncrease
    @Priority(PriorityConstant.SEVENTH)
    @ListenBreak
    public void friendIncrease(){}

    /**
     * ● 监听好友昵称变动事件
     */

    @OnFriendNicknameChanged
    @Priority(PriorityConstant.EIGHTH)
    @ListenBreak
    public void friendNicknameChanged(){}

    /**
     * ● 监听好友减少事件
     */

    @OnFriendReduce
    @Priority(PriorityConstant.NINTH)
    @ListenBreak
    public void friendReduce(){}

    /**
     * ● 监听群友增加事件
     */

    @OnGroupMemberIncrease
    @Priority(PriorityConstant.TENTH)
    @ListenBreak
    public void groupMemberIncrease(){}

    /**
     * ● 监听群友减少事件
     */

    @OnGroupMemberReduce
    @Priority(PriorityConstant.MODULE_FIRST)
    @ListenBreak
    public void groupMemberReduce(){}

    /**
     * ● 监听群添加请求事件
     */

    @OnGroupAddRequest
    @Priority(PriorityConstant.MODULE_SECOND)
    @ListenBreak
    public void groupAddRequest(){}

    /**
     * ● 监听群成员权限变动事件
     */

    @OnGroupMemberPermissionChanged
    @Priority(PriorityConstant.MODULE_THIRD)
    @ListenBreak
    public void groupMemberPermissionChanged(){}

    /**
     * ● 监听群名称变动事件
     */
    @OnGroupNameChanged
    @Priority(PriorityConstant.MODULE_FOURTH)
    @ListenBreak
    public void groupNameChanged(){}

    /**
     * ● 监听群友群名片变动事件
     */
    @OnGroupMemberRemarkChanged
    @Priority(PriorityConstant.MODULE_FIFTH)
    @ListenBreak
    public void groupMemberRemarkChanged(){}

    /**
     * ● 监听群友头衔变动事件
     */
    @OnGroupMemberSpecialChanged
    @Priority(PriorityConstant.MODULE_SIXTH)
    @ListenBreak
    public void groupMemberSpecialChanged(){}

    /**
     * bot被禁言事件
     */
    @OnGroupMute
    @Priority(PriorityConstant.MODULE_SEVENTH)
    @ListenBreak
    public void botMute(){}

    /**
     * ● 监听私聊消息
     */

    @OnGroup
    @Priority(PriorityConstant.FIRST)
    public void groupMsg(){}

    /**
     * ● 监听群聊消息
     */

    @OnPrivate
    @Priority(PriorityConstant.FIRST)
    public void privateMsg(){}

}
