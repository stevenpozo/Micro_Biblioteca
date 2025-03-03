package com.microservice_loan.microservice_loan.Config;

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
        // Permite solicitudes desde cualquier origen. En producción, se recomienda limitar los orígenes.
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
                        .requestMatchers(HttpMethod.POST, "/loan").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/loan").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/loan/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/loan/some-data").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/loan/returned-book/{loanId}").hasAnyRole("USER", "ADMIN")




                        //PERMIT ONDLY ADMIN
                        .requestMatchers(HttpMethod.GET, "/loan/binnacle").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/loan/disable-loan/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/loan/update").hasAnyRole("ADMIN")
                        
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Configura el convertidor para que extraiga el claim "role" y lo convierta en GrantedAuthorities
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role"); // Se espera que el token tenga "role": "ADMIN" o "USER"
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
