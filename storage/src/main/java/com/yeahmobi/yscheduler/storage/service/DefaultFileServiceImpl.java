package com.yeahmobi.yscheduler.storage.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.yeahmobi.yscheduler.storage.FileEntry;
import com.yeahmobi.yscheduler.storage.FileKey;

/**
 * @author Abel.Cui
 * @date 2015/3/10
 */
public class DefaultFileServiceImpl implements FileService {

    private String basePath;

    private long   maxUploadSize = 100;

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public long getMaxUploadSize() {
        return this.maxUploadSize;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public long store(FileKey fileKey, FileEntry fileEntry) throws IOException {

        long version = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer(this.basePath);
        // 拼接存储路径
        spliceBasePath(fileKey, sb);
        // 存储文件时判断是否存在旧的版本，若存在则删除
        deleteDir(sb.toString());
        sb.append(version).append(IOUtils.DIR_SEPARATOR_UNIX);
        // 检查并创建目录
        createDirs(sb);
        // 写文件
        writeFile(fileEntry, sb);
        return version;

    }

    private void deleteDir(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && (file.list().length > 0)) {
            for (String name : file.list()) {
                if ((name.length() > 1) && StringUtils.isNumeric(name)) {
                    FileUtils.deleteDirectory(new File(path + name + IOUtils.DIR_SEPARATOR_UNIX));
                }
            }
        }
    }

    public FileEntry get(FileKey fileKey, long version) throws FileNotFoundException {
        StringBuffer sb = new StringBuffer(this.basePath);
        spliceDownloadPath(fileKey, version, sb);
        File file = new File(sb.toString());
        if (file.exists() && (file.listFiles().length > 0)) {
            InputStream inputStream;
            inputStream = new FileInputStream(file.listFiles()[0]);
            return new FileEntry(file.list()[0], new BufferedInputStream(inputStream));
        }
        throw new FileNotFoundException("该文件不存在！");
    }

    private void writeFile(FileEntry fileEntry, StringBuffer sb) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(sb.append(fileEntry.getFileName()).toString());
            IOUtils.copy(fileEntry.getInputStream(), outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (fileEntry.getInputStream() != null) {
                fileEntry.getInputStream().close();
            }
        }
    }

    private void createDirs(StringBuffer sb) {
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    // 拼接下载路径
    private void spliceDownloadPath(FileKey fileKey, long version, StringBuffer sb) {
        spliceBasePath(fileKey, sb);
        if (version == 0) {// 下载最新版本
            File file = new File(sb.toString());
            if (file.exists() && (file.list().length > 0)) {
                version = Collections.max(this.str2Long(file.list()));
            }
        }
        sb.append(version).append(IOUtils.DIR_SEPARATOR_UNIX);
    }

    private void spliceBasePath(FileKey fileKey, StringBuffer sb) {
        sb.append(IOUtils.DIR_SEPARATOR_UNIX).append(fileKey.getNameSpace()).append(IOUtils.DIR_SEPARATOR_UNIX);
        for (char k : fileKey.getKey().toCharArray()) {
            sb.append(k).append(IOUtils.DIR_SEPARATOR_UNIX);
        }
    }

    // 将字符串的时间戳转为Long型的
    private List<Long> str2Long(String[] strs) {
        List<Long> timeStamp = new ArrayList<Long>();
        for (String str : strs) {
            if ((str.length() > 1) && StringUtils.isNumeric(str)) {
                timeStamp.add(Long.valueOf(str));
            }
        }
        return timeStamp;
    }

}
