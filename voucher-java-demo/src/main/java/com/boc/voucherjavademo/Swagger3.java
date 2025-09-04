package com.boc.voucherjavademo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3 {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("传票管理平台-微服务管理RESTful APIs")
                        .description("微服务管理")
                        .version("v0.0.1"));

    }
  
}  