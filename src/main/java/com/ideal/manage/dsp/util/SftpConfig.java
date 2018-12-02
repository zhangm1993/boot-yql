package com.ideal.manage.dsp.util;

/**
 * Created by dell on 15-2-4.
 */
public class SftpConfig {
    //主机ip
    private String host = "";
    //端口号
    private int port;
    //ftp用户名
    private String user = "";
    //ftp密码
    private String password = "";
    //ftp中的目录
    private String path = "";
    // 备份目录
    private String backup;
    //接口文件导出目录
    private String export;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public SftpConfig(){}

    public SftpConfig(String host, int port, String user, String password, String path){
        this.setHost(host);
        this.setPort(port);
        this.setUser(user);
        this.setPassword(password);
        this.setPath(path);
    }
}
