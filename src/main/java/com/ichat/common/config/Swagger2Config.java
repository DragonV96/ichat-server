package com.ichat.common.config;

import com.google.common.base.Predicates;
import com.ichat.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author glw
 * @date 2019/11/7 13:23
 * @descrpition: swagger api文档
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    private String basePackage = Application.class.getPackage().getName();

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("restful接口").select()
                .apis(Predicates.and(RequestHandlerSelectors.basePackage(basePackage)))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ichat后台服务 OPEN RESTful API文档")
                .description("开放接口参考和查看API详细信息")
                .termsOfServiceUrl("https://www.github.com/DragonV96")
                .contact(new Contact("glw", "", ""))
                .version("1.0")
                .build();
    }
}
