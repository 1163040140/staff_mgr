package ec.backend.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("人员管理系统 API").version("1.0"))
                // 关键部分：添加全局的鉴权配置
                .addSecurityItem(new SecurityRequirement().addList("token"))
                .components(new Components()
                        .addSecuritySchemes("token", new SecurityScheme()
                                .name("token") // 这里必须和你拦截器里 request.getHeader("token") 的名字一致
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                        ));
    }
}