package com.ideal.manage.dsp.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static final int BUFSIZE = 1024 * 8;

    /**
     * 生产文件 如果文件所在路径不存在则生成路径
     *
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author dell
     * @date 2008-8-27
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1000];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }catch (Exception e) {
            e.printStackTrace();

        }

    }
    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param one 文件1
     * @param two   文件2
     * @param combineFilePath  合并后文件路径
     * @return 返回合并后的文件
     */
    public static void combineFile(File one,File two,String combineFilePath){
        FileInputStream fisOne = null;
        FileInputStream fisTwo = null;
        FileOutputStream dos=null;
        try{
            fisOne = new FileInputStream(one);
            fisTwo = new FileInputStream(two);
            dos =new FileOutputStream(new File(combineFilePath)) ;

            BufferedReader brOne = new BufferedReader(new InputStreamReader(fisOne, "UTF-8"));
            BufferedReader brTwo = new BufferedReader(new InputStreamReader(fisTwo, "UTF-8"));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(dos, "UTF-8"));
            String line ;
            while ((line = brOne.readLine()) != null) {
                wr.write(line);
                wr.newLine();
            }
            wr.flush();

            while ((line = brTwo.readLine()) != null) {
                wr.write(line);
                wr.newLine();
            }
            wr.flush();

            fisOne.close()  ;
            fisTwo.close() ;
            dos.close()   ;
            brOne.close();
            brTwo.close();
            wr.close();
//            one.delete();
//            two.delete();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String compress(File srcFile, String desFile) {
        //File file = new File(srcFile);
        try {
            File zipFile = new File(desFile);
            InputStream input = new FileInputStream(srcFile);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(srcFile.getName()));
            byte[] buffer = new byte[1024];
            int length = 0;
            while (-1 != (length = input.read(buffer))) {
                zipOut.write(buffer, 0, length);
            }
            input.close();
            zipOut.close();
            srcFile.delete();
            return "OK!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error:" + e;
        }

    }

    /**
     * 拷贝文件    删除原文件
     */
    public static void copyFileDel(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1000];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();

                /*boolean result = oldfile.delete();
                if(!result)
                {
                    System.gc();
                    oldfile.delete();

                }*/
                boolean flag = oldfile.delete();
                System.out.println("filePath:"+oldPath+"|"+flag);
            }
        }catch (Exception e) {
            e.printStackTrace();

        }

    }


    /**
     * 获取指定目录下特定文件后缀名的文件列表(不包括子文件夹)
     *
     * @param dirPath
     *            目录路径
     * @param suffix
     *            文件后缀
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath,
                                              final String suffix) {
        File path = new File(dirPath);
        File[] fileArr = path.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowerName = name.toLowerCase();
                String lowerSuffix = suffix.toLowerCase();
                if (lowerName.endsWith(lowerSuffix)) {
                    return true;
                }
                return false;
            }

        });
        ArrayList<File> files = new ArrayList<File>();

        for (File f : fileArr) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        return files;
    }

}
