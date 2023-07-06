package recordstofile.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${info.app.name}")
    String infoAppName;

    @Value("${info.app.environment}")
    String infoAppEnvironment;

    @Value("${info.app.version}")
    String infoAppVersion;

    @Bean
    public OpenAPI userMicroserviceOpenApi() {

        return new OpenAPI().info(new Info().title(infoAppName)
                                            .description(infoAppEnvironment)
                                            .version(infoAppVersion));
    }

}