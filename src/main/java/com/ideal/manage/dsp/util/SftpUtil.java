package com.ideal.manage.dsp.util;


import com.jcraft.jsch.*;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * Created by dell on 15-2-4.
 */
public class SftpUtil {
    private static String _root = "/";
    private Session sshSession;

    public ChannelSftp connect(SftpConfig sftpConfig) {
        ChannelSftp sftp = null;
        JSch jsch = new JSch();
        try {
            sshSession = jsch.getSession(sftpConfig.getUser(), sftpConfig.getHost(), sftpConfig.getPort());
            System.out.println("Session created.");
            sshSession.setPassword(sftpConfig.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            System.out.println("start connected Session");
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            System.out.println("start connected Channel");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + sftpConfig.getHost() + ".");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return sftp;
    }

    public void disconnect(ChannelSftp sftp) {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                System.out.println("sftp关闭连接！！！！！====" + sftp);
            } else if (sftp.isClosed()) {
            }
        }
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
        }
        System.out.println("sftp 已经关闭");
    }

    public static Vector<ChannelSftp.LsEntry> getChkFile(String path, ChannelSftp sftp) {
        Vector<ChannelSftp.LsEntry> vector = null;
        try {
            vector = sftp.ls(path);
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            return vector;
        }
    }

    public static boolean cd(String dirs, ChannelSftp sftp) throws Exception {
        try {
            String path = dirs;
//            if (path.indexOf("\\") != -1) {
//                path = dirs.replaceAll("\\", "/");
//            }
            String pwd = sftp.pwd();
            if (pwd.equals(path))
                return true;

            sftp.cd(_root);

            if (_root.equals(dirs)) return true;

            String[] paths = path.split("/");
            for (int i = 0; i < paths.length; i++) {
                String dir = paths[i];
                if (isEmpty(dir)) continue;
                sftp.cd(dir);
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean isExist(String dir, String fileOrDir,
                                  ChannelSftp sftp) throws Exception {
        try {
            boolean exist = false;
            boolean cdflg = false;
            String pwd = sftp.pwd();
            if (pwd.indexOf(dir) == -1) {
                cdflg = true;
                sftp.cd(dir);
            }
            Vector<ChannelSftp.LsEntry> ls = getChkFile(dir, sftp);
            if (ls != null || ls.size() >= 0) {
                for (int i = 0; i < ls.size(); i++) {
                    ChannelSftp.LsEntry f = ls.get(i);
                    String nm = f.getFilename();
                    if (nm.equals(fileOrDir)) {
                        exist = true;
                        break;
                    }
                }
            }
            if (cdflg) {
                sftp.cd("..");
            }
            return exist;
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static boolean mkDir(String filepath, ChannelSftp sftp)
            throws Exception {
        try {
            String path = filepath;
//            if (path.indexOf("\\") != -1) {
//                path = filepath.replaceAll("\\", "/");
//            }
            String[] paths = path.split("/");
            for (int i = 0; i < paths.length; i++) {
                String dir = paths[i];
                Vector ls = getChkFile(dir, sftp);
                if (ls == null || ls.size() <= 0) {
                    sftp.mkdir(dir);
                }
                sftp.cd(dir);
            }
        } catch (Exception e1) {
            throw e1;
        }
        return true;
    }

    public static boolean rm(String deleteFile, ChannelSftp sftp)
            throws Exception {
        try {
            Vector ls = _list(sftp);
            if (ls != null && ls.size() > 0) {
                for (int i = 0; i < ls.size(); i++) {
                    ChannelSftp.LsEntry f = (ChannelSftp.LsEntry) ls.get(i);
                    String nm = f.getFilename();
                    if (!nm.equals(deleteFile)) {
                        continue;
                    }
                    SftpATTRS attr = f.getAttrs();
                    if (attr.isDir()) {
                        if (rmdir(nm, sftp)) {
                            sftp.rmdir(nm);
                        }
                    } else {
                        sftp.rm(nm);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<String> list(ChannelSftp sftp) {
        List<String> resultList = null;
        try {
            Vector<ChannelSftp.LsEntry> files = sftp.ls(sftp.pwd());
            if(files!=null){
                resultList = new ArrayList<String>();
                for(int i = 0 ; i < files.size() ; i++){
                    ChannelSftp.LsEntry lsEntry = files.get(i);
                    if(!".".equals(lsEntry.getFilename())&&!"..".equals(lsEntry.getFilename())){
                        resultList.add(lsEntry.getFilename());
                    }
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            return resultList;
        }
    }

    public static Vector _list(ChannelSftp sftp) {
        try {
            return sftp.ls(sftp.pwd());
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean rmdir(String dir, ChannelSftp sftp) throws Exception {
        try {
            sftp.cd(dir);
            Vector ls = _list(sftp);
            if (ls != null && ls.size() > 0) {
                for (int i = 0; i < ls.size(); i++) {
                    ChannelSftp.LsEntry f = (ChannelSftp.LsEntry) ls.get(i);
                    String nm = f.getFilename();
                    if (nm.equals(".") || nm.equals(".."))
                        continue;
                    SftpATTRS attr = f.getAttrs();
                    if (attr.isDir()) {
                        if (rmdir(nm, sftp)) {
                            sftp.rmdir(nm);
                        }
                    } else {
                        sftp.rm(nm);
                    }
                }
            }
            sftp.cd("..");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean upload(String uploadFile, ChannelSftp sftp)
            throws Exception {
        InputStream is = null;
        try {
            File file = new File(uploadFile);
            is = new FileInputStream(file);
            sftp.put(is, file.getName());
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    public static boolean upload(MultipartFile multipartFile, ChannelSftp sftp, String fileName)
            throws Exception {
        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
            sftp.put(is, fileName);
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean download(String dir, String downloadFile,
                                String saveFile, ChannelSftp sftp) {
        try {
            boolean isCd = false;
            if (!isEmpty(dir)) {
                sftp.cd(dir);
                isCd = true;
            }
            // File file = new File(saveFile);
            // sftp.get(downloadFile, new FileOutputStream(file));
            sftp.get(downloadFile, saveFile);

            if (isCd)
                sftp.cd("..");

            System.out.println("下载文件成功！！！！！====" + sftp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载文件失败！！！！！=*******" + sftp);
            return false;
        }
    }

    public static void main(String[] args) {
        SftpConfig sftpConfig = new SftpConfig();
        sftpConfig.setHost("180.96.27.112");
//        sftpConfig.setHost("122.225.103.118");
        sftpConfig.setPort(22);
        sftpConfig.setUser("root");
//        sftpConfig.setPassword("h2ALxYYxoy7Hknfn");
        sftpConfig.setPassword("ideal123q");
        sftpConfig.setPath("/home/qianjunjie/file/");
        SftpUtil sftpUtil = new SftpUtil();
        ChannelSftp sftp = sftpUtil.connect(sftpConfig);

        sftpUtil.disconnect(sftp);
    }

    private static List<String> getDataFileByChkFile(String file) {
        List<String> dataFiles = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(new File(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                dataFiles.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return dataFiles;
        }
    }

    public static boolean unzip(String zipFilePath, String targetPath){
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        boolean flag = true;
        try {
            zipFile = new ZipFile(zipFilePath);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf("."));
            } else {
                directoryPath = targetPath;
            }
            Enumeration entryEnum = zipFile.getEntries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                        directoryPath = directoryPath + File.separator
                                + zipEntry.getName();
                        System.out.println(directoryPath);
                        continue;
                    }
                    if (zipEntry.getSize() >= 0) {
                        // 文件
                        File targetFile = FileUtil.buildFile(directoryPath
                                + File.separator + zipEntry.getName(), false);
                        os = new BufferedOutputStream(new FileOutputStream(
                                targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[10240];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 10240)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }
                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        FileUtil.buildFile(directoryPath + File.separator
                                + zipEntry.getName(), true);
                    }
                }
            }
            flag = true;
        } catch (IOException ex) {
            ex.printStackTrace();
            flag = false;
        } finally {
            if (null != zipFile) {
                try {
                    zipFile.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flag;
        }
    }

    public static boolean rename(String oldPath, String newPath,ChannelSftp sftp) {

        try {
            sftp.rename(oldPath,newPath);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 压缩文件
     */
    public static void compress(File srcdir,String destPathName){
        File zipFile = new File(destPathName);
        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setFile(srcdir);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
        //fileSet.setExcludes(...); 排除哪些文件或文件夹
        zip.addFileset(fileSet);

        zip.execute();
    }

   /* public static void main(String[] args) {
        compress(new File("C:\\Users\\Administrator\\Desktop\\201506301547080554_pushPolicy_combine.txt"),
                "C:\\Users\\Administrator\\Desktop\\201506301547080554_pushPolicy_combine.zip");
    }*/


}
