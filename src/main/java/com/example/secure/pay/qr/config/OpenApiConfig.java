package com.example.secure.pay.qr.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:SecurePay QR}")
    private String applicationName;

    @Value("${spring.application.description:Stripe entegreli, QR kod destekli güvenli ödeme API'si}")
    private String applicationDescription;

    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .servers(List.of(buildServer()))
                .externalDocs(buildExternalDocs());
    }

    private Info buildInfo() {
        return new Info()
                .title(applicationName + " API")
                .description(applicationDescription + " - Dinamik ödeme oturumları ve QR kod yönetimi.")
                .version(applicationVersion)
                .contact(buildContact())
                .license(buildLicense());
    }

    private Contact buildContact() {
        return new Contact()
                .name("Orhan Türkmenoğlu")
                .email("orhantrkmn749@gmail.com")
                .url("https://github.com/orhanturkmenoglu");
    }

    private License buildLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private ExternalDocumentation buildExternalDocs() {
        return new ExternalDocumentation()
                .description("Proje GitHub Deposu")
                .url("https://github.com/orhanturkmenoglu/securepay-qr");
    }

    private Server buildServer() {
        String url = "http://localhost:" + serverPort + contextPath;
        return new Server()
                .url(url)
                .description("Yerel Geliştirme Sunucusu");
    }
}
