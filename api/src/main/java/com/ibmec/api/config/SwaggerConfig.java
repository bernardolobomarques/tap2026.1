package com.ibmec.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIFA World Cup API")
                        .description("""
                                Sistema de Gestão da Copa do Mundo — AP2 IBMEC

                                Permite cadastrar seleções, jogadores e partidas,
                                além de controlar quais seleções participam de cada jogo.

                                **Design Patterns utilizados:**
                                - Builder: PartidaBuilder para construção fluente de partidas
                                - Strategy: interfaces IService desacoplam controller da implementação
                                - Repository: Spring Data JPA abstrai o acesso ao banco
                                - Singleton: todos os @Service e @Repository são gerenciados pelo Spring
                                """)
                        .version("1.0.0"));
    }
}
