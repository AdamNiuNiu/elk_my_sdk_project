package com.example.interceptor.interceptorsdk;


/**
 * 上报日志数据
 */
public interface LogKafkaProducerService {
    /**
     * 上报日志
     * 
     * @param log
     * 要上报的日志
     */
    void sendLog(BasicLogBean log);
    void sendLog2(BasicLogBean log);
}
