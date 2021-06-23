package rainbow.kuzwlu.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class FileTXTUtil {

    public static List<String> loadFileList(String fileTxtUrl) {
        List<String> data = new ArrayList<>();
        BufferedReader brname;
        try {
            brname = new BufferedReader(new InputStreamReader(new FileInputStream(fileTxtUrl),"UTF-8"));
            String sname = null;
            while ((sname = brname.readLine()) != null) {
                if(sname.equals("") || sname.equals(null)) {

                }else{
                    data.add(sname);
                }
            }
            brname.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String loadFileString(String fileUrl){
            StringBuilder str = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileUrl),"UTF-8"));
                String str2 = "";
                while ((str2 = bufferedReader.readLine()) != null) {
                    str.append(str2+"\n");
                }
                bufferedReader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return str.toString();
        }

    public static String add(String groupNumber,String fileTxtUrl){
        String msg = "";
        BufferedWriter bufferedWriter = null;
        List openGroupList = FileTXTUtil.loadFileList(fileTxtUrl);
        if(openGroupList.contains(groupNumber)){
            msg = "error";
        }else {
            openGroupList.add(groupNumber);
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTxtUrl), "UTF-8"));
                for (Object arr : openGroupList) {
                    bufferedWriter.write(arr + "\n");
                }
                bufferedWriter.close();
                msg = "success";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    public static String del(String groupNumber,String fileTxtUrl){
        String msg = "";
        BufferedWriter bufferedWriter = null;
        List openGroupList = FileTXTUtil.loadFileList(fileTxtUrl);
        if(openGroupList.contains(groupNumber)){
            openGroupList.remove(groupNumber);
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTxtUrl), "UTF-8"));
                for (Object arr : openGroupList) {
                    bufferedWriter.write(arr + "\n");
                }
                bufferedWriter.close();
                msg = "success";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            msg = "error";
        }
        return msg;
    }

}
