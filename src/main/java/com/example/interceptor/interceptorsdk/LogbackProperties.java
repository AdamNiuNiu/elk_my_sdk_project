package com.example.interceptor.interceptorsdk;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

public class LogbackProperties {
    private static final Logger log = LoggerFactory.getLogger(LogbackProperties.class);
    private static final String LOGBACK_LOG_PRO_PATH = "logback{0}.properties";
    private static final String[] APPLICATION_PRO_PATHS =
        {"application.properties", "application.yml", "application.yaml"};
    private static LogbackProperties instance;
    private static volatile boolean updateLog = true;
    private Properties pro = null;
    LogbackConfig config = null;

    private LogbackProperties() {
        init();
    }

    public static LogbackProperties getInstance() {
        if (instance == null) {
            instance = new LogbackProperties();
        }
        return instance;
    }

    public static boolean isUpdateLog() {
        return updateLog;
    }

    public LogbackProperties setPro(Properties pro) {
        this.pro = pro;
        return this;
    }

    public void build() {
        config = null;
        if (pro.containsKey("updateLog")) {
            LogbackProperties.updateLog = "true".equals(pro.getProperty("updateLog"));
        }
        getLogbackConfig();
    }

    public static void setUpdateLog(boolean updateLog) {
        LogbackProperties.updateLog = updateLog;
    }

    /**
     * 获取kafka配置
     * 
     * @return kafka配置
     */
    public Properties getKafkaProperties() {
        Properties kafkaPro = new Properties();
        if (pro != null) {
            for (Map.Entry<Object, Object> entry : pro.entrySet()) {
                String key = entry.getKey().toString();
                if (key.startsWith("logback.kafka")) {
                    kafkaPro.put(key.replace("logback.kafka.", ""), entry.getValue());
                }
            }
        }
        return kafkaPro;
    }

    /**
     * 获取配置
     * 
     * @return 配置
     */
    public LogbackConfig getLogbackConfig() {
        if (config == null) {
            if (pro != null) {
                JSONObject json = new JSONObject();
                for (Map.Entry<Object, Object> entry : pro.entrySet()) {
                    String key = entry.getKey().toString();
                    if (key.startsWith("logback.sdk.")) {
                        json.put(key.replace("logback.sdk.", ""), entry.getValue());
                    }
                }
                config = JSONObject.toJavaObject(json, LogbackConfig.class);
                if (StringUtils.isBlank(config.getServiceHost())) {
//                    config.setServiceHost(getHost());
                }
            }
        }
        return config;
    }

    private void init() {
        // 先获取application配置
        Resource resource = null;
        for (String path : APPLICATION_PRO_PATHS) {
            resource = new ClassPathResource(path);
            if (resource.exists()) {
                // 找到其中一种文件格式
                log.info("======SDK包======找到了application配置文件, path:{}", path);
                break;
            } else {
                log.info("======SDK包======配置文件不存在或不是文件, path:{}", path);
            }
        }
        // spring.profiles.active=dev
        String active = getActive(resource);
        String path = LOGBACK_LOG_PRO_PATH.replace("{0}", '-' + active);
        if (StringUtils.isNotBlank(active)) {
            resource = new ClassPathResource(path);
            try {
                pro = new Properties();
                pro.load(resource.getInputStream());
                // 如果加载成功，则返回不用加载默认配置
                return;
            } catch (Exception e) {
                log.warn("load '" + path + "' error:" + e.getMessage());
            }
        }
        path = LOGBACK_LOG_PRO_PATH.replace("{0}", "");
        // 获取配置
        resource = new ClassPathResource(path);
        try {
            pro = new Properties();
            pro.load(resource.getInputStream());
        } catch (Exception e) {
            log.error("load '" + path + "' error:" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private String getActive(Resource resource) {
        String active = "";
        try {
            String fileName = resource.getFilename();
            if (fileName.endsWith(".properties")) {
                pro = new Properties();
                pro.load(resource.getInputStream());
                active = pro.getProperty("spring.profiles.active");
            } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
                String[] nodes = {"spring", "profiles"};
                Yaml yaml = new Yaml();
                Map<String, Object> map = yaml.load(resource.getInputStream());
                for (String node : nodes) {
                    map = (Map<String, Object>)map.get(node);
                    if (map == null) {
                        break;
                    }
                }
                if (map != null) {
                    active = (String)map.get("active");
                }
            }
            log.info("======SDK包======成功读取到了active配置, resource:{}, active:{}", resource, active);
        } catch (Exception e) {
            log.warn("load '" + resource + "' error:" + e.getMessage());
        }
        return active;
    }

//    private String getHost() {
//        String host = "";
//        try {
//            // 打印数据
//            Map<String, String> evn = System.getenv();
//            // 从环境变量中获获取 node节点名称，要堆发布配置
//            String name = evn.get("CUSTOM_HOST_NAME");
//            // 从环境变量中获获取 node节点IP，要堆发布配置
//            String address = evn.get("CUSTOM_HOST_ADDRESS");
//            log.info("华为云节点变量, nodeName:{}, nodeIp:{}", name, address);
//            InetAddress inet4Address = Inet4Address.getLocalHost();
//            if (StringUtils.isBlank(name)) {
//                name = inet4Address.getHostName();
//            }
//            if (StringUtils.isBlank(address)) {
//                address = inet4Address.getHostAddress();
//            }
//            host = name + '@' + address;
//            log.info("系统参数, env: {}", JSONObject.toJSONString(evn));
//        } catch (UnknownHostException e) {
//            log.warn("获取计算机名和IP异常, msg:{}", e.getMessage());
//            host = "default@127.0.0.1";
//        }
//        return host;
//    }
}
