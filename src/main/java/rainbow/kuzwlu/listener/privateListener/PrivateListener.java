package rainbow.kuzwlu.listener.privateListener;

import catcode.CatCodeUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.stereotype.Component;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.core.annotation.Cron;


/**
 * @author kuzwlu
 */
@Beans
@Component
public class PrivateListener {

    private UserConfig userConfig = UserConfig.getInstance();

    @OnPrivate
    public void privateMessage(PrivateMsg privateMsg , MsgSender sender, CatCodeUtil catCodeUtil) throws Exception {
        //必须通过bean才能切入
        Object privateListener = SpringUtil.getBean("privateListener");
        ReflectUtil.invoke(privateListener,"cornTest");
        //可以通过动态注入bean实现切面，

        AccountInfo accountInfo = privateMsg.getAccountInfo();
        if (accountInfo.getAccountCode().equals("1552000861")) {
            //sender.SENDER.sendPrivateMsg(accountInfo.getAccountCode(),"竹竹竹");
            if (privateMsg.getMsg().startsWith("发送")) {
                String code = privateMsg.getMsg().substring(2);
                sender.SENDER.sendPrivateMsg(accountInfo.getAccountCode(),code);
            }
            if (privateMsg.getMsg().startsWith("给群发送")) {
                String code = privateMsg.getMsg().substring(5);
                sender.SENDER.sendGroupMsg(955877318,code);
            }

        }
        System.out.println("私聊消息[发送人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t消息内容："+privateMsg.getMsg()+"]");

    }

    @Cron(schedulingPattern = "*/2 * * * * *",title = "cron测试",description = "cron描述测试")
    public void cornTest(){
        System.out.println("cron测试");
    }

}
