package ec.backend.demo.config;

import ec.backend.demo.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入刚才写的拦截器
    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // "/**" 代表拦截所有请求
                .addPathPatterns("/**")
                // 放行以下接口（千万别拦截它们，否则你没法登录，也看不了文档）
                .excludePathPatterns(
                        "/login/**",              // 你的登录接口路径
                        "/swagger-ui/**",         // Swagger 页面
                        "/v3/api-docs/**",        // Swagger 数据
                        "/error"                  // 默认错误页面
                );
    }
}