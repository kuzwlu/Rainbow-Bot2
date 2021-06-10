package rainbow.kuzwlu.util;

import java.io.*;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class LogUtil {

    public static String exceptionLog(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    public static void saveLogToFile(String fileName,String str,String fileUrl) {
        BufferedWriter bufferedWriter = null;
        String logs = FileTXTUtil.loadFileString(PathUtil.getPath(fileName, fileUrl));
        str = TimeUtil.getNowtime()+":\n" +str;
        if (logs != null || logs != "") {
            str = logs +"\n\n\n"+str;
        }
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(PathUtil.getPath(fileName,fileUrl)));
            bufferedWriter.write(str + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
