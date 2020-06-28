package com.example.interceptor.interceptorsdk;

/**
 * 接口日志数据类
 */
public class InterfaceLogBean extends BasicLogBean {

    /**
     * 构造函数
     * 
     * @param serviceName
     *            服务名
     * @param serviceVersion
     *            服务版本
     */
    public InterfaceLogBean(String serviceName, String serviceVersion, String hostName) {
        super(serviceName, serviceVersion, hostName);
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
    public InterfaceLogBean(String serviceName, String serviceVersion, String hostName, String level) {
        super(serviceName, serviceVersion, hostName, level);
    }

    // 请求类型
    private String reqType;
    // 访问IP
    private String ip;
    // 接口名,url
    private String serviceUrl;
    // POST|GET
    private String method;
    // 响应码
    private int status;
    // 请求耗时
    private long costTime;
    // 请求参数
    private String reqParams;
    // 响应数据
    private String resultMsg;
    // 用户ID
    private String userId;
    // 流水号
    private String uuid;

    // 响应数据长度
    private long bytes;

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCreateTime()).append('|');
        sb.append(getLevel()).append('|');
        sb.append(getServiceName()).append('|');
        sb.append(getServiceVersion()).append('|');
        sb.append(reqType).append('@').append(ip).append('|');
        sb.append(method).append('|');
        sb.append(serviceUrl).append('|');
        sb.append(status).append('|');
        sb.append(costTime).append('|');
        sb.append(bytes).append('|');
        sb.append(userId).append('|');
        sb.append(uuid).append('|');
        sb.append(reqParams).append('|');
        sb.append(resultMsg);
        return sb.toString();
    }

}
