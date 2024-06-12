package net.elghz.gatwayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll().pathMatchers("/site/**").hasAuthority("ADMIN")
                        //.pathMatchers("/site/**").hasAuthority("TECHNICIEN")
                        .pathMatchers("/pointMesure/**").hasAuthority("ADMIN")
                        .pathMatchers("/users/**").hasAnyAuthority("ADMIN", "TECHNICIEN" , "RESPONSABLE DE MAINTENANCE")
                        .pathMatchers("/keycloak/**").hasAnyAuthority("ADMIN", "TECHNICIEN" , "RESPONSABLE DE MAINTENANCE")
                        .pathMatchers("/planningsMaintenances/**").hasAnyAuthority("ADMIN", "TECHNICIEN" , "RESPONSABLE DE MAINTENANCE")
                        .pathMatchers("/interventions/**").hasAnyAuthority("ADMIN", "TECHNICIEN")
                        .anyExchange().authenticated()
                ).oauth2ResourceServer((oauth) -> oauth
                        .jwt(Customizer.withDefaults()))
                .build();
    }

}
