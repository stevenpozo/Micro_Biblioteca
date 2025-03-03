package com.irojas.oauth.oauthserver.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().equals("access_token")) {
            Authentication principal = context.getPrincipal();

            if (principal != null && principal.getPrincipal() instanceof User) {
                User userPrincipal = (User) principal.getPrincipal();

                // Suponiendo que solo hay un rol, lo extraemos
                String authority = userPrincipal.getAuthorities().iterator().next().getAuthority();
                // authority = "ROLE_ADMIN" o "ROLE_USER"
                String role = authority.replace("ROLE_", ""); // => "ADMIN" o "USER"

                context.getClaims().claim("role", role);
            }
        }
    }
}
