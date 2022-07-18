package rainbow.kuzwlu.pluginsBot;

import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.component.mirai.message.event.MiraiNudgedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rainbow.kuzwlu.core.annotation.OnNudged;
import rainbow.kuzwlu.core.plugins.compiler.java.InvokeObject;

import java.security.Permission;
import java.util.Map;
/**
 * @Author kuzwlu
 * @Description 根据MsgGet选择对应执行的方法
 * @Date 2021/11/19 14:38
 * @Version 1.0
 */
public class SwitchListener {

    private static Logger logger = LogManager.getLogger(SwitchListener.class);

    public static void choose(MsgGet msgGet, Map<Class,Object> invokeParams){
        invokeParams.put(MsgGet.class,msgGet);
        if (msgGet instanceof PrivateMsgRecall){
            invokeParams.put(PrivateMsgRecall.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnPrivateMsgRecall.class,invokeParams);
            logger.debug("SwitchListener：私聊消息撤回事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMsgRecall){
            invokeParams.put(GroupMsgRecall.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMsgRecall.class,invokeParams);
            logger.debug("SwitchListener：群聊消息撤回事件-"+msgGet);
            return;
        }else if (msgGet instanceof FriendAddRequest){
            invokeParams.put(FriendAddRequest.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnFriendAddRequest.class,invokeParams);
            logger.debug("SwitchListener：监听好友请求事件-"+msgGet);
            return;
        }else if (msgGet instanceof FriendAvatarChanged){
            invokeParams.put(FriendAvatarChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnFriendAvatarChanged.class,invokeParams);
            logger.debug("SwitchListener：监听好友头像变动事件-"+msgGet);
            return;
        }else if (msgGet instanceof FriendIncrease){
            invokeParams.put(FriendIncrease.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnFriendIncrease.class,invokeParams);
            logger.debug("SwitchListener：监听好友增加事件-"+msgGet);
            return;
        }else if (msgGet instanceof FriendNicknameChanged){
            invokeParams.put(FriendNicknameChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnFriendNicknameChanged.class,invokeParams);
            logger.debug("SwitchListener：监听好友昵称变动事件-"+msgGet);
            return;
        }else if (msgGet instanceof FriendReduce){
            invokeParams.put(FriendReduce.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnFriendReduce.class,invokeParams);
            logger.debug("SwitchListener：监听好友减少事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMemberIncrease){
            invokeParams.put(GroupMemberIncrease.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMemberIncrease.class,invokeParams);
            logger.debug("SwitchListener：监听群友增加事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMemberReduce){
            invokeParams.put(GroupMemberReduce.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMemberReduce.class,invokeParams);
            logger.debug("SwitchListener：监听群友减少事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupAddRequest) {
            invokeParams.put(GroupAddRequest.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupAddRequest.class,invokeParams);
            logger.debug("SwitchListener：监听群添加请求事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMemberPermissionChanged){
            invokeParams.put(GroupMemberPermissionChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMemberPermissionChanged.class,invokeParams);
            logger.debug("SwitchListener：监听群成员权限变动事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupNameChanged){
            invokeParams.put(GroupNameChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupNameChanged.class,invokeParams);
            logger.debug("SwitchListener：监听群名称变动事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMemberRemarkChanged){
            invokeParams.put(GroupMemberRemarkChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMemberRemarkChanged.class,invokeParams);
            logger.debug("SwitchListener：监听群友群名片变动事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMemberSpecialChanged){
            invokeParams.put(GroupMemberSpecialChanged.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMemberSpecialChanged.class,invokeParams);
            logger.debug("SwitchListener：监听群友头衔变动事件-"+msgGet);
            return;
        }else if(msgGet instanceof MiraiNudgedEvent.ByFriend){
            invokeParams.put(MiraiNudgedEvent.ByFriend.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnNudged.ByFriend.class,invokeParams);
            logger.debug("SwitchListener：监听好友戳一戳事件-"+msgGet);
            return;
        }else if(msgGet instanceof MiraiNudgedEvent.ByMember){
            invokeParams.put(MiraiNudgedEvent.ByMember.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnNudged.ByMember.class,invokeParams);
            logger.debug("SwitchListener：监听群聊戳一戳事件-"+msgGet);
            return;
        }else if(msgGet instanceof GroupMute){
            invokeParams.put(GroupMute.class,msgGet);
            InvokeObject.invokeAllByAnnotation(OnGroupMute.class,invokeParams);
            logger.debug("SwitchListener：监听群禁言事件-"+msgGet);
            return;
        }else if (msgGet instanceof GroupMsg) {
            invokeParams.put(GroupMsg.class,msgGet);
            Menu.adminMenu(msgGet,invokeParams);
            InvokeObject.invokeAllByAnnotation(OnGroup.class, invokeParams);
            logger.debug("SwitchListener：群聊消息-"+msgGet);
            return;
        }else if (msgGet instanceof PrivateMsg) {
            invokeParams.put(PrivateMsg.class,msgGet);
            Menu.adminMenu(msgGet,invokeParams);
            InvokeObject.invokeAllByAnnotation(OnPrivate.class,invokeParams);
            logger.debug("SwitchListener：私聊消息-"+msgGet);
            return;
        }
    }

}
