package com.example.interceptor.interceptorsdk;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicLogBean {
    // 主机信息
    private String hostNode;
    // 时间戳
    private String createTime;
    // 日志级别
    private String level;
    // 服务名
    private String serviceName;
    // 服务版本号
    private String serviceVersion;
    // 日志类型
    private String logType;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getHostNode() {
        return hostNode;
    }

    public void setHostNode(String hostNode) {
        this.hostNode = hostNode;
    }

    /**
     * 构造函数
     * 
     * @param serviceName
     *            服务名
     * @param serviceVersion
     *            服务版本
     */
    public BasicLogBean(String serviceName, String serviceVersion, String hostNode) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.hostNode = hostNode;
    }

    /**
     * 构造函数
     * 
     * @param serviceName
     *            服务名
     * @param serviceVersion
     *            服务版本
     * @param level
     *            默认日志级别
     */
    public BasicLogBean(String serviceName, String serviceVersion, String hostName, String level) {
        this(serviceName, serviceVersion, hostName);
        this.level = level;
    }

    public void setCreateTime(long createTime) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.createTime = dateformat.format(new Date(createTime));
    }
}
