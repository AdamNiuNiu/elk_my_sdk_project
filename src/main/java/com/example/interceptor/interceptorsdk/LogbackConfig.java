package com.example.interceptor.interceptorsdk;

public class LogbackConfig {
    // 服务名
    private String serviceName;
    // 服务版本
    private String serviceVersion;
    // 服务node信息
    private String serviceHost;
    // 返回水消息成功值
    private int successCode;
    // 是否记录日志
    private boolean isPrintLog;
    // 返回消息key 默认code
    private String codeKey;

    // 要过虑掉的请求
    private String filterSuffix;

    // 不允许的记录的方法
    private String unallowedMethod = "OPTIONS";

    private int reqMaxSize = 128;
    private int respMaxSize = 1024;

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

    public int getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }

    public boolean isPrintLog() {
        return isPrintLog;
    }

    public void setPrintLog(boolean isPrintLog) {
        this.isPrintLog = isPrintLog;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getFilterSuffix() {
        return filterSuffix;
    }

    public void setFilterSuffix(String filterSuffix) {
        this.filterSuffix = filterSuffix;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public int getReqMaxSize() {
        return reqMaxSize;
    }

    public void setReqMaxSize(int reqMaxSize) {
        this.reqMaxSize = reqMaxSize;
    }

    public int getRespMaxSize() {
        return respMaxSize;
    }

    public void setRespMaxSize(int respMaxSize) {
        this.respMaxSize = respMaxSize;
    }

    public String getUnallowedMethod() {
        return unallowedMethod;
    }

    public void setUnallowedMethod(String unallowedMethod) {
        this.unallowedMethod = unallowedMethod;
    }

}
