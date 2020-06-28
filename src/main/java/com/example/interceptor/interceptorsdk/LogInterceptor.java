package com.example.interceptor.interceptorsdk;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    private LogKafkaProducerService kafkaProducerService;

    private LogbackConfig config;

    public LogInterceptor(LogKafkaProducerService kafkaProducerService) {
        log.info("=========拦截器LogInterceptor开始初始化构造方法=========");
        this.config = LogbackProperties.getInstance().getLogbackConfig();
        this.kafkaProducerService=kafkaProducerService;
        log.info("=========拦截器LogInterceptor初始化构造方法结束=========");
    }

    //在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理
    //调用Controller某个方法之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("================进入拦截器LogInterceptor的preHandle方法==========================");
        log.info("================请求的url为："+request.getRequestURI()+"==========================");
        MessageContext msgContext = new MessageContext(request);
        return true;
    }

    //在业务处理器处理请求执行完成后，生成视图之前执行。
    //后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
    //Controller之后调用，视图渲染之前，如果控制器Controller出现了异常，则不会执行此方法
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("================进入拦截器LogInterceptor的postHandle方法==========================");
    }

    //在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
    //不管有没有异常，这个afterCompletion都会被调用，用于资源清理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("================进入拦截器LogInterceptor的afterCompletion方法==========================");
        log.info("===================开始进行接口的日志记录==========================");
        log.info("===================开始进行接口的日志记录==========================");
        log.info("===================开始进行接口的日志记录==========================");

        try {
            MessageContext msgContext = MessageContext.getMessageContext();
            if(msgContext==null) {
                return;
            }
            msgContext.remove();
            int code = response.getStatus();
            InterfaceLogBean interfaceLog = new InterfaceLogBean(
                    config.getServiceName(),config.getServiceVersion(),config.getServiceHost()
            );
            Object respBody = msgContext.getRespBody();
            interfaceLog.setCreateTime(msgContext.getStartTime());
            String level = null;
            if(code < 400 && ex == null) {
                level = "INFO";
            }else {
                level= "ERROR";
                if(ex!=null) {
                    //获取异常信息
                    JSONObject result = LogUtils.getExceptionStack(ex);
                    log.debug("exceptions:{}",result);
                    respBody = result;
                }
            }
            interfaceLog.setLevel(level);
            interfaceLog.setReqType(request.getScheme());
            interfaceLog.setIp(LogUtils.getRemoteIP(request));
            interfaceLog.setMethod(request.getMethod());
            interfaceLog.setStatus(code);
            interfaceLog.setServiceUrl(msgContext.getPath());
            Map<String,String[]> params = request.getParameterMap();
            Object reqBody = msgContext.getReqBody();
            if(!params.isEmpty()) {
                reqBody = params;
            }
            interfaceLog.setReqParams(LogUtils.toJsonReq(reqBody,config.getReqMaxSize()));
            interfaceLog.setUuid(request.getHeader("uuid"));
            long costTime = System.currentTimeMillis() - msgContext.getStartTime();
            interfaceLog.setCostTime(costTime);
            interfaceLog.setBytes(response.getBufferSize());
            interfaceLog.setResultMsg(LogUtils.toJsonResp(respBody,config.getRespMaxSize()));
            //打印日志数据
            if("INFO".equals(level)) {
                log.info(interfaceLog.toString());
            }else {
                log.error(interfaceLog.toString());
            }

            if(kafkaProducerService!=null) {
                interfaceLog.setLogType("interface");
                log.info("===================开始推送日志数据至kafka==========================");
                log.info("===================开始推送日志数据至kafka==========================");
                log.info("===================开始推送日志数据至kafka==========================");
                kafkaProducerService.sendLog(interfaceLog);
//                kafkaProducerService.sendLog2(interfaceLog);
                log.info("===================数据推送结束==========================");
                log.info("===================数据推送结束==========================");
                log.info("===================数据推送结束==========================");
            }

        } catch (Exception e) {
            e.printStackTrace();
            //异常信息不对外抛
            log.warn("记录接口日志时发生错误",e);
        }
        log.info("===================接口的日志记录结束==========================");
        log.info("===================接口的日志记录结束==========================");
        log.info("===================接口的日志记录结束==========================");

    }


}
