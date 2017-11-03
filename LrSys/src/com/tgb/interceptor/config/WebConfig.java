/*package com.tgb.interceptor.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean corsFilterRegistrationBean() {
        // 对响应头进行CORS授权
        MyCorsRegistration corsRegistration = new MyCorsRegistration("/**");
        this._configCorsParams(corsRegistration);

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsRegistration.getCorsConfiguration());
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        return new FilterRegistrationBean(corsFilter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置CorsInterceptor的CORS参数
        this._configCorsParams(registry.addMapping("/**"));
    }

    private void _configCorsParams(CorsRegistration corsRegistration) {
        corsRegistration.allowedOrigins(CrossOrigin.DEFAULT_ORIGINS)
               .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(), HttpMethod.PUT.name())
                .allowedHeaders(CrossOrigin.DEFAULT_ALLOWED_HEADERS)
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                .allowCredentials(CrossOrigin.DEFAULT_ALLOW_CREDENTIALS)
                .maxAge(CrossOrigin.DEFAULT_MAX_AGE);
    }
}
*/