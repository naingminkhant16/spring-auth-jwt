package com.moe.jwttest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    
    @Bean
    public OpenAPI defineOpenAPI() {

        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact contact = new Contact();
        contact.setName("Blog");
        contact.setEmail("nmk@gmail.com");

        Info information =
                new Info()
                        .title("Blog APIs")
                        .version("1.0")
                        .description("This API exposes endpoints to manage the system.")
                        .contact(contact);

        return new OpenAPI().info(information).servers(List.of(server));
    }
}
