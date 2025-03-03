package com.microservice_books.microservice_books.Config;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth


                        //PERMIT ALL
                        .requestMatchers(HttpMethod.GET, "/book/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/book").permitAll()
                        .requestMatchers(HttpMethod.GET, "/book/tac").permitAll()
                        .requestMatchers(HttpMethod.GET, "/book/verify-status/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/disable/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/enable/{id}").permitAll()


                        // PERMIT ONLY ADMIN
                        .requestMatchers(HttpMethod.GET, "/book/some-data").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/book/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/book/update/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/book/enable/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/book/disable/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/book/total-amount").hasAnyRole("ADMIN")


                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Se configura para que el claim "role" se interprete como autoridad,
        // agregando el prefijo "ROLE_"
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
