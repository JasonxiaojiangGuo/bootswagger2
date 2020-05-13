package com.jason.bootswagger2.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gxj.demo.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("SpringBoot整合Swagger")
                        .description("SpringBoot整合Swagger,详细信息......")
                        .version("0.0.1")
                        .contact(new Contact("啦啦啦啦", "哈哈哈", "www.baidu.com"))
                        .license("The Apache License").licenseUrl("wwww.baidu.com")
                        .build());
    }


    /**
     * 写在Spring Security的配置类中
     * @param web
     * @throws Exception

     @Override public void configure(WebSecurity web) throws Exception {
     web.ignoring()
     .antMatchers("/swagger-ui.html")
     .antMatchers("/v2/**")
     .antMatchers("/swagger-resources/**");

     }
     */
}
