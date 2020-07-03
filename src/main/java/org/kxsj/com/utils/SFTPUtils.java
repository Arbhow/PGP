package org.kxsj.com.utils;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class SFTPUtils {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(SFTPUtils.class);

    private static final String host = "";
    private static final int port = 22;
    private static final String username = "sftpuser";
    private static final String password = "";
    public static final String updirectory = "/data/send";
    public static final String downdirectory = "/data/rece";
    public static final String directory = "/folder";

    private static ChannelSftp sftp;

    private static SFTPUtils instance = null;

    private SFTPUtils() {
    }

    public static SFTPUtils getInstance() throws SftpException {
        if (instance == null) {
            if (instance == null) {
                instance = new SFTPUtils();
                instance.
                sftp = instance.connect(host, port, username, password);   //获取连接
                System.out.println(sftp.getHome());
                System.out.println("SFTP服务器连接成功！！！");
            }
        }
        return instance;
    }

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            LOG.info("SFTP Session connected.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            LOG.info("Connected to " + host);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     */
    public boolean upload(String directory, String uploadFile) {
        try {
            if (directory != null) {
                sftp.cd(directory);
                File file = new File(uploadFile);
                FileInputStream fileInputStream = new FileInputStream(file);
                sftp.put(fileInputStream, file.getName());
                fileInputStream.close();

            } else {
                System.out.println("没有此上传路径");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
        return true;
    }
//     * 下载文件
//     *
//     * @param downdirectory    下载目录
//     * @param downloadFile 下载的文件
//     * @param saveFile     存在本地的路径
//     */
    public File download(String downdirectory, String downloadFile, String saveFile) {
        try {
            sftp.cd(downdirectory);
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFile, fileOutputStream);
            fileOutputStream.close();
            return file;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param downloadFilePath 下载的文件完整目录
     * @param saveFile         存在本地的路径
     */
    public File download(String downloadFilePath, String saveFile) {
        try {
            int i = downloadFilePath.lastIndexOf('/');
            if (i == -1)
                return null;
            sftp.cd(downloadFilePath.substring(0, i));
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFilePath.substring(i + 1), fileOutputStream);
            fileOutputStream.close();
            return file;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }



    public void disconnect() {
        try {
            sftp.getSession().disconnect();
        } catch (JSchException e) {
            LOG.error(e.getMessage());
        }
        sftp.quit();
        sftp.disconnect();
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @throws SftpException
     */
    public Vector<LsEntry> listFiles(String directory) {
        Vector ls = null;
        try {
            ls = sftp.ls(directory);
        } catch (NullPointerException e) {
            System.out.println("空指针异常！！");
            e.printStackTrace();
        }catch (SftpException e){
            System.out.println("sftp异常");
            e.printStackTrace();
        }
        return ls;
    }

    public static void main(String[] args) throws IOException ,SftpException{
        SFTPUtils sf = SFTPUtils.getInstance();
        sf.upload(directory, "E:\\edge浏览器下载\\jenkins.war");    //上传文件

//        sf.download(downdirectory, "2.png", "C:\\Users\\hp\\Desktop\\1212.png");
        //sFile download = sf.download("/home/1.png", "C:\\Users\\hp\\Desktop\\122221.png");

        //sf.delete(directory, deleteFile); //删除文件

        /*Vector<LsEntry> files = null;        //查看文件列表

        files = sf.listFiles(directory);

        for (LsEntry file : files) {
            System.out.println("###\t" + file.getFilename());
        }*/
        sf.disconnect();
    }
}