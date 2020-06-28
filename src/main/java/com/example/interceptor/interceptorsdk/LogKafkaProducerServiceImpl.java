package com.example.interceptor.interceptorsdk;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

@Component
public class LogKafkaProducerServiceImpl implements LogKafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(LogKafkaProducerServiceImpl.class);
    private ScheduledExecutorService executor = null;
    private Producer<String, String> producer = null;

    private String logTopic;

    public LogKafkaProducerServiceImpl() {
        Properties pro = LogbackProperties.getInstance().getKafkaProperties();
        log.info("************************SDK包************************初始化LogKafkaProducerServiceImpl,{}", pro.toString());
        log.info("======开始遍历kafka配置信息======");
        log.info("======开始遍历kafka配置信息======");
        log.info("======开始遍历kafka配置信息======");
        Iterator<Map.Entry<Object, Object>> it = pro.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key   :" + key);
            System.out.println("value :" + value);
            System.out.println("---------------");
        }
        log.info("======遍历kafka配置信息结束======");
        log.info("======遍历kafka配置信息结束======");
        log.info("======遍历kafka配置信息结束======");

        logTopic = pro.getProperty("topic");
        producer = new KafkaProducer<String, String>(pro);
        // 设置线程池名称
        CustomizableThreadFactory factory = new CustomizableThreadFactory("log-kafka-send-");
        // 默认5个线程
        executor = Executors.newScheduledThreadPool(5, factory);
    }

    @Override
    public void sendLog(final BasicLogBean logRecord) {
        if (logRecord == null) {
            log.info("************接口日志interfaceLog的值为："+logRecord.toString()+"”************");
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final String jsonRecord = JSONObject.toJSONString(logRecord);
                producer.send(new ProducerRecord<String, String>(logTopic, jsonRecord),
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception == null) {
                                log.debug("发送日志成功,log:{}", jsonRecord);
                            } else {
                                log.error("发送日志失败, log:{}, msg:{}", jsonRecord, exception.getMessage());
                            }
                        }
                    });
            }
        });

    }


    @Override
    public void sendLog2(final BasicLogBean logRecord) {
        if (logRecord == null) {
            log.info("************接口日志interfaceLog的值为："+logRecord.toString()+"”************");
        }
        final String jsonRecord = JSONObject.toJSONString(logRecord);
        Future<RecordMetadata> send = producer.send(new ProducerRecord<String, String>(logTopic, jsonRecord));
        String s = send.toString();
        log.info("************send返回值为："+s+"”************");
    }

}
