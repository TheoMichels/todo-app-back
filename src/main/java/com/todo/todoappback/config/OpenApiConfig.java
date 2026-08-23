package com.todo.todoappback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo & Suivi API")
                        .version("1.0.0")
                        .description(
                                "Tâches organisées par sections (avec une vue \"Supprimés\" pour les tâches "
                                        + "terminées) et points de suivi. Voir docs/api/openapi.yaml dans "
                                        + "todo-app-front pour le contrat de référence."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description(
                                        "Documenté pour anticiper le multi-appareils/comptes. "
                                                + "Non encore appliqué : aucune authentification n'est vérifiée "
                                                + "par ce backend pour l'instant.")));
    }
}
