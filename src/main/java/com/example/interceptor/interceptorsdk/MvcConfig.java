package com.example.interceptor.interceptorsdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Import(value = LogInterceptor.class)
public class MvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(logInterceptor).excludePathPatterns("/webjars/**", "/swagger-ui.html",
                "/swagger-resources/**", "/csrf", "/static/**", "/public/**", "/resources/**");
//        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    //注册静态资源解析器
    //如果不加，则静态资源会被拦截，导致访问不到静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    //解决响应中文乱码
    @Bean
    public HttpMessageConverter<String> responseBodyStringConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyStringConverter());
    }
}
