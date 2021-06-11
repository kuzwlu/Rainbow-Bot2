package rainbow.kuzwlu.util;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class Aria2Util extends WebSocketClient {

    public Aria2Util(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("aria2已经连接...");
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }

    private static Aria2Util client;

    public static void aria2(String ariaUrl,String sendStr){
        try {
            // 创建 WebSocket 客户端
            client = new Aria2Util(ariaUrl);

            // 连接 WebSocket 服务器
            client.connect();
            client.setConnectionLostTimeout(10);
            System.out.println(client.getReadyState());

            // 当服务器连接上时，发送 Json 数据
            while (!client.getReadyState().equals(ReadyState.OPEN)) {
                System.out.println("等待连接...");
            }
            client.send(sendStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("aria2程序出错");
        } finally {
            client.close();
        }
    }

    //下载
    public static void download(String ariaUrl,String downloadUrl,String filePath){
        String sendStr = "{\n" +
                "    \"jsonrpc\":\"2.0\",\n" +
                "    \"id\":\"qwer\",\n" +
                "    \"method\":\"aria2.addUri\",\n" +
                "    \"params\":[\n" +
                "        [\n" +
                "            \"" + "" + downloadUrl + "\"\n" +
                "        ],\n" +
                "        {\n" +
                "            \"dir\":\"" + "" + filePath + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        aria2(ariaUrl,sendStr);
    }

    //删除下载的链接
    public static void remove(String gid){

    }

}