package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration  //声明为配置类
public class CorsFilterConfiguration {

    /**
     * 注入cors过滤器类，用于处理跨域问题
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){

        // 创建cors配置类，配置相关信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 添加允许跨域访问的域名  不能写*  否则就不能发送cookie
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        corsConfiguration.addAllowedOrigin("http://www.leyou.com");
        // 是否允许发送cookie(两个条件：1，这个属性为true 2，允许跨域访问的域名不能配置为*)
        corsConfiguration.setAllowCredentials(true);
        // 添加允许跨域访问的请求方式  这里可以设置为*  所有请求方式都可以跨域访问
        corsConfiguration.addAllowedMethod("*");
        // 添加允许跨域访问的请求的头信息
        corsConfiguration.addAllowedHeader("*");

        // 创建根据请求路径配置cors的数据源类
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // 设置拦截路径和cors配置类对象  这里/** 代表拦截一切请求
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        // 创建cors过滤器对象并返回
        return new CorsFilter(corsConfigurationSource);
    }

}
