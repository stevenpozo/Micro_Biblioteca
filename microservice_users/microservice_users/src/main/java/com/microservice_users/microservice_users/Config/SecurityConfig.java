package com.microservice_users.microservice_users.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes desde cualquier origen
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Configuration
    @EnableWebSecurity
    public class ResourceServerConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            // Ejemplo: supongamos que /user/** es la API principal
                            // Reglas:
                            // 1. ADMIN puede hacer cualquier método (GET, POST, PUT, DELETE)
                            // 2. USER solo puede hacer GET y PUT, por ejemplo

                            .requestMatchers("/user/details").permitAll()
                            // => GET y PUT en /user/** para USER o ADMIN
                            .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("USER", "ADMIN")

                            // => POST y DELETE en /user/** solo para ADMIN
                            .requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")

                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    );

            // Podrías deshabilitar CSRF si es una API REST
            http.csrf(csrf -> csrf.disable());

            return http.build();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
            // Indica que use el claim "role" como autoridad
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            authoritiesConverter.setAuthoritiesClaimName("role"); // => "role": "ADMIN" o "USER"
            authoritiesConverter.setAuthorityPrefix("ROLE_");

            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
            converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
            return converter;
        }
    }


}