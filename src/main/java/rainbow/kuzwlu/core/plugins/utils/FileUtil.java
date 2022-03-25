package rainbow.kuzwlu.core.plugins.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtil {

    /**
     * 数组转字符串
     * @param openGroup
     * @return String
     */
    public static String list2String(List<String> openGroup){
        Iterator iterator = openGroup.iterator();
        StringBuffer sb = new StringBuffer();
        while(iterator.hasNext()) {
            Object arr = iterator.next();
            sb.append(arr + "\n");
        }
        return sb.toString();
    }

    /**
     * 从文件一行一行读取成List
     * @param pluginsFile
     * @param path
     * @param charset
     * @return List<String>
     */
    public static List<String> read2List(PluginsFile pluginsFile, String path, String charset){
        List<String> temp = new ArrayList<>();
        try {
            FileInputStream fileInputStream = pluginsFile.fileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,charset));
            String sname = null;
            while((sname = bufferedReader.readLine()) != null) {
                if (!sname.equals("") && !sname.equals((Object)null)) {
                    temp.add(sname);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 从文件一行一行读取成String
     * @param pluginsFile
     * @param path
     * @param charset
     * @return String
     */
    public static String read2String(PluginsFile pluginsFile,String path,String charset){
        StringBuilder str = new StringBuilder();
        try {
            FileInputStream fileInputStream = pluginsFile.fileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, charset));
            String str2 = "";
            while((str2 = bufferedReader.readLine()) != null) {
                str.append(str2 + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * 保存文件
     * @param pluginsFile
     * @param path
     * @param content
     * @param charset
     * @return boolean
     */
    public static boolean contentSave(PluginsFile pluginsFile,String path,String content,String charset){
        try {
            FileOutputStream fileOutputStream = pluginsFile.fileOutputStream(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, charset));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException var7) {
            return false;
        }
        return true;
    }

}
