package com.example.demo1.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .select().apis(RequestHandlerSelectors.basePackage("com.example.demo1.controller"))
                .paths(PathSelectors.any()).build()
                .apiInfo(setApiInfo())
                .globalRequestParameters(setRequestParameter());
    }
    // swagger默认是不可以直接添加请求头的需要单独配置
    private List<RequestParameter> setRequestParameter() {
        RequestParameter parameter = new RequestParameterBuilder()
                .name("token")
                .description("token")
                .in(ParameterType.HEADER)
                .required(true)
                .build();
        return Collections.singletonList(parameter);
    }

    @SuppressWarnings("rawtypes")
    private ApiInfo setApiInfo() {
        Contact contact = new Contact("laizhenghua", "https://blog.csdn.net/m0_46357847", "3299447929@qq.com");
        return new ApiInfo("SpringSecurity基于token的认证功能", "通过token认证API完整实现", "v1.0",
                "https://blog.csdn.net/m0_46357847", contact, "Apache 2.0", "", new ArrayList<VendorExtension>());
    }

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(@SuppressWarnings("null") Object bean, @SuppressWarnings("null") String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings({ "unchecked", "null" })
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
