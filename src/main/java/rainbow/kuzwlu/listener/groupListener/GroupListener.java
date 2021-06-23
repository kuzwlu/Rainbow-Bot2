package rainbow.kuzwlu.listener.groupListener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import rainbow.kuzwlu.config.UserConfig;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/6 09:02
 * @Version 1.0
 */

@Beans
public class GroupListener {

    private UserConfig userConfig = UserConfig.getInstance();

    @OnGroup
    public void groupMessage(GroupMsg groupMsg , MsgSender sender, CatCodeUtil catCodeUtil) {
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        if ("955877318".equals(groupCode)) {
            if("2601029774".equals(accountInfo.getAccountCode())) {
                //sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"lsp");
            }
            //System.out.println("群聊："+groupMsg.getGroupInfo().getGroupName()+"("+groupMsg.getGroupInfo().getGroupCode()+")\t 发信人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t 消息内容："+groupMsg.getMsg());
        }
        if ("967627696".equals(groupCode) || "955877318".equals(groupCode)) {

        }
        //System.out.println("群聊："+groupMsg.getGroupInfo().getGroupName()+"("+groupMsg.getGroupInfo().getGroupCode()+")\t 发信人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t 消息内容："+groupMsg.getText());
    }

    private boolean flag = false;
    @OnGroup
    @Filter(groups = {"955877318"})
    public void yeLing(GroupMsg groupMsg, MsgSender sender, CatCodeUtil catCodeUtil){
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        if ("1726539504".equals(accountInfo.getAccountCode())) {
            if (".".equals(groupMsg.getText())) {
                flag = true;
            }else if ("。".equals(groupMsg.getText())) {
                flag = false;
            }
        }
        if ("3501891984".equals(accountInfo.getAccountCode())) {
            if (flag) {
                sender.SENDER.sendGroupMsg(groupInfo.getGroupCode(), groupMsg.getMsgContent());
            }
        }
    }

}
