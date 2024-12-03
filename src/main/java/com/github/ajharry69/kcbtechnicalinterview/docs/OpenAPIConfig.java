package com.github.ajharry69.kcbtechnicalinterview.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Harrison",
                        email = "oharry0535@gmail.com"
                ),
                description = "OpenAPI documentation for Xently",
                title = "OpenAPI specification - KCB Test",
                version = "v1"
        ),
        servers = {@Server(
                description = "Development",
                url = "http://localhost:8080"
        )}
)
public class OpenAPIConfig {
}
