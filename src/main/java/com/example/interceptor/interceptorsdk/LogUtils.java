package com.example.interceptor.interceptorsdk;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class LogUtils {
    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
    // private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
    private static final String[] IP_HEANDERS =
        {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

    public static JSONObject getExceptionStack(Throwable ex) {
        JSONObject result = new JSONObject();
        StackTraceElement[] stacks = ex.getStackTrace();
        result.put("code", -1);
        result.put("exceptionMsg", ex.getMessage());
        if (stacks != null) {
            // 最多打5行堆信息
            int i = 1;
            List<StringBuilder> lines = new ArrayList<StringBuilder>();
            for (StackTraceElement stack : stacks) {
                StringBuilder sb = new StringBuilder();
                sb.append(stack.getClassName());
                sb.append('.').append(stack.getMethodName());
                sb.append("() ").append(stack.getLineNumber());
                lines.add(sb);
                if (i++ > 5) {
                    break;
                }
            }
            result.put("stacks", lines);
        }
        return result;
    }

    public static int getRetCode(Object respBody, String codeKey, int sucCode) {
        int retCode = sucCode;
        if (respBody != null) {
            try {
                String jsonStr = JSONObject.toJSONString(respBody);
                if (jsonStr.startsWith("{")) {
                    JSONObject json = JSONObject.parseObject(jsonStr);
                    if (json.containsKey(codeKey)) {
                        Integer code = json.getIntValue(codeKey);
                        if (code != null) {
                            retCode = code;
                        }
                    }
                }
            } catch (JSONException e) {
                // json解析失败，不再解析
            }
        }
        return retCode;
    }

    public static String getRemoteIP(HttpServletRequest req) {
        String ip = "";
        for (String key : IP_HEANDERS) {
            ip = req.getHeader(key);
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip)) {
                // 反向代理，最一个ip才是真实ip
                int index = ip.indexOf(",");
                if (index == -1) {
                    return ip;
                } else {
                    return ip.substring(0, index);
                }
            }
        }
        ip = req.getRemoteAddr();
        return ip;
    }

    public static String toJsonResp(Object obj, int size) {
        if (obj instanceof Exception && !(obj instanceof NullPointerException)) {
            obj = obj.toString();
        }
        String json = JSONObject.toJSONString(obj);
        if (json != null && json.length() > size) {
            log.debug("data to long, data:{}", json);
            json = json.substring(0, size);
        }
        return json;
    }

    public static String toJsonReq(Object obj, int size) {
        String json = JSONObject.toJSONString(obj);
        if (json != null && json.length() > size) {
            log.debug("data to long, data:{}", json);
            json = json.substring(0, size);
        }
        return json;
    }
}
