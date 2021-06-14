package vms.vevs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("vms.vevs.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getAppInfo());
    }


    private ApiInfo getAppInfo() {
        return new ApiInfoBuilder().title("Visitor Entry and verification System  ")
                .description("Welcomes your visitor or guest in Digital India style ")
                .termsOfServiceUrl("https://callus.herokuapp.com/anujKumarPalResume").build();

    }
}
