package rainbow.kuzwlu.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.IOException;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class PathUtil {

    /**
     * 全部文件放入fileName文件夹中，和项目同级
     * 以/结尾是文件夹，如果不以/结尾，则会生成文件
     *
     * @param fileName  文件夹名称
     * @param fileUrl 文件需要存放的路径
     * PathUtils.getPath("file","wenjianjia/1.txt");
     * @return
     */
    public static String getPath(String fileName,String fileUrl) {
        ApplicationHome h = new ApplicationHome(PathUtil.class);
        String dirPath = h.getSource().getParent().toString();
        dirPath = dirPath.replaceAll("\\\\","/");
        fileUrl = fileName+"/"+fileUrl;
        String[] str = fileUrl.split("/");
        String fileDirectoty = "";
        File directory = null;
        StringBuffer stringBuffer = new StringBuffer();
        boolean b = fileUrl.substring(fileUrl.length() - 1).endsWith("/");
        if(!b){
            for(String s:str){
                stringBuffer = stringBuffer.append(s+"/");
            }
            fileDirectoty = stringBuffer.toString();
            fileDirectoty = fileDirectoty.substring(0,fileUrl.length()-str[str.length-1].length()-1);
            dirPath = dirPath+"/"+fileDirectoty;
            directory = new File(dirPath);
            if(!directory.exists()) {
                directory.mkdirs();
            }
            dirPath = dirPath+"/"+str[str.length-1];
            File file = new File(dirPath);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("文件创建失败");
                }
            }
        } else if (b) {
            for(String s:str){
                stringBuffer = stringBuffer.append(s+"/");
            }
            fileDirectoty = stringBuffer.toString();
            dirPath = dirPath+"/"+fileDirectoty;
            directory = new File(dirPath);
            if(!directory.exists()) {
                directory.mkdirs();
            }
        } else {
            dirPath = dirPath +"/"+fileUrl;
            directory= new File(dirPath);
            if(!directory.exists()){
                try {
                    directory.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("文件创建失败");
                }
            }
        }
        //System.out.println("获得路径："+dirPath+"");
        //System.out.println(">>=================================================================================================<<");
        return dirPath;
    }
}
