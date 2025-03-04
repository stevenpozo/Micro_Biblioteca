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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import java.util.List;


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
            http.cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:5173"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                config.setAllowCredentials(true);
                return config;
            }))
                    .authorizeHttpRequests(auth -> auth
                            // Ejemplo: supongamos que /user/** es la API principal
                            // Reglas:
                            // 1. ADMIN puede hacer cualquier método (GET, POST, PUT, DELETE)
                            // 2. USER solo puede hacer GET y PUT, por ejemplo

                            //ROUTES PERMIT ALL
                            .requestMatchers("/user/details").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/register-user").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/user/{id}").permitAll()

                            //ROUTES PERMIT ONLY USER AND ADMIN
                            .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/user/update-user/{id}").hasAnyRole("USER", "ADMIN")

                            //ROUTES PERMIT ONLY ADMIN
                            .requestMatchers(HttpMethod.POST, "/user/create-user").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/user/update-admin/{id}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/user/enable-user/{id}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/user/disable-user/{id}").hasRole("ADMIN")


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