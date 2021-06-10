package rainbow.kuzwlu.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class MsgUtil {

//    //获取at列表
//    public static List<String> atList(GroupMsg msg){
//        List<String> atList = new ArrayList<String>();
//        boolean isat = Pattern.matches(".*[CQ:at,qq=].*",msg.getMsg());
//        if(isat){
//            List<String> s = KQCodeUtils.getInstance().getCqs(msg.getMsg(), "at");
//            for(int i =0;i<s.size();i++){
////                String str = s.get(i).replace("[CQ:at,qq=","");
////                str = str.replaceFirst("(?<=\\]).*","");
////                if(s.get(i).contains("display=")){
////                    str = str.replaceAll("(,display=\\S*,target=\\d*)","");
////                }
////                str = str.replace("]","");
////                atList.add(str);
//                String str = KQCodeUtils.getInstance().getParam(s.get(i), "qq", "at");
//                atList.add(str);
//            }
//            String temp = "";
//            for (int i = 0; i < atList.size() - 1; i++) {
//                temp = atList.get(i);
//                for (int j = i + 1; j < atList.size(); j++) {
//                    if (temp.equals(atList.get(j))) {
//                        atList.remove(j);
//                    }
//                }
//            }
//        }
//        return atList;
//    }

    //删除HTML标签
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/script>";
    private static final String regEx_html = "<[^>]+>";
    private static final String regEx_space = "\\s*|\t|\r|\n";
    private static final String regEx_w = "<w[^>]*?>[\\s\\S]*?<\\/W[^>]*?";
    private static final String regEx_special = "\\&[a-zA-Z]{1,10};";
    public static String delHTMLTag(String htmlStr){
        Pattern p_w = Pattern.compile(regEx_w, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll("");
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(htmlStr);
        htmlStr = m_special.replaceAll("");
        htmlStr = htmlStr.replaceAll(" ", "");
        return htmlStr.trim();
    }

    /*
        格式化时间,用于禁言等
     */
    public static int formatTime(String content){
        int time = 0;
        String day = content.replaceAll("\\d*","");
        String numbers = content.substring(0,content.length()-day.length());
        int number = Integer.parseInt(numbers);
        if(day.equals("秒") || day.equals("s")){
            time = 1000*number;
        }else if(day.equals("分钟")|| day.equals("m")){
            time = 1000*60*number;
        }else if(day.equals("小时")|| day.equals("h")){
            time = 1000*60*60*number;
        }else if(day.equals("天")|| day.equals("d")){
            if(number <= 30){
                time = 1000*60*60*60*number;
            }else{
                //send(data,"时间太长了！");
            }
        }else{
            System.out.println("时间格式出错！");
        }
        return time;
    }

}
