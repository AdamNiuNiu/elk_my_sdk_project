package com.example.interceptor.interceptorsdk;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息上下文
 */
public class MessageContext {
    private static final ThreadLocal<MessageContext> DATA = new ThreadLocal<MessageContext>();
    private long startTime;
    private String path;
    private Object respBody;
    private Object reqBody;

    public MessageContext(HttpServletRequest request) {
        this.startTime = System.currentTimeMillis();
        this.path = request.getRequestURI();
        if (StringUtils.isBlank(path) || path.endsWith("/error")) {
            Object realPath = request.getAttribute("javax.servlet.error.request_uri");
            this.path = (realPath == null ? "null" : realPath.toString());
        }
        DATA.set(this);
    }

    /**
     * 删除数据
     */
    public void remove() {
        DATA.remove();
    }

    public static MessageContext getMessageContext() {
        return DATA.get();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getRespBody() {
        return respBody;
    }

    public void setRespBody(Object respBody) {
        this.respBody = respBody;
    }

    public Object getReqBody() {
        return reqBody;
    }

    public void setReqBody(Object reqBody) {
        this.reqBody = reqBody;
    }
}
