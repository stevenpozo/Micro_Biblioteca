package com.irojas.oauth.oauthserver.oauth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import com.irojas.oauth.oauthserver.Service.RemoteUserDetailsService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Importa CorsFilter
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private RemoteUserDetailsService remoteUserDetailsService;

	// Bean para encriptar contraseñas
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(remoteUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(daoAuthenticationProvider());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// Permitir que se invoque desde cualquier origen (ojo en producción)
		configuration.addAllowedOrigin("http://localhost:5173");
		// Métodos permitidos (puedes poner "*")
		configuration.addAllowedMethod("*");
		// Headers permitidos (puedes poner "*")
		configuration.addAllowedHeader("*");
		// Opcionalmente, si necesitas credenciales
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Aplica esta configuración a todas las rutas
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		http
				.cors(cors -> {}) // Asegurar que CORS esté habilitado
				.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si es necesario
				.addFilterBefore(new CorsFilter(corsConfigurationSource()), UsernamePasswordAuthenticationFilter.class) // Añadir el filtro antes de autenticación
				.exceptionHandling(exceptions -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				)
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}

	@Bean
	@Order(0)
	public SecurityFilterChain preflightSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/oauth2/**") // Se aplica a cualquier ruta que comience con /oauth2/
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(org.springframework.http.HttpMethod.OPTIONS).permitAll()
						.requestMatchers(org.springframework.http.HttpMethod.POST, "/oauth2/token").permitAll()
				)
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/error", "/css/**", "/js/**").permitAll()
						.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.disable())
				.formLogin(Customizer.withDefaults())
				.logout(logout -> logout
						.logoutUrl("/logout")                      // La URL que disparará el logout
						.logoutSuccessUrl("/login?logout")         // A dónde redirigir después del logout
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.authenticationManager(authenticationManager())
				.cors(Customizer.withDefaults());

		return http.build();
	}

	// Fuente de claves para la generación del JWKSet
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	// Generador de claves RSA
	private static KeyPair generateRsaKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	// Decoder para JWT
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	// Registro de clientes en memoria
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("oidc-client")
				.clientSecret("{noop}123456789")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://oauthdebugger.com/debug")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.scope("read")
				.scope("write")
				.build();

		RegisteredClient oauthClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("oauth-client")
				.clientSecret(passwordEncoder().encode("12345678910"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

				//.redirectUri("http://127.0.0.1:8080/login/oauth2/code/oauth-client")
				//.redirectUri("http://127.0.0.1:8080/authorized")
				.redirectUri("http://localhost:5173/")
				.postLogoutRedirectUri("http://127.0.0.1:8080/logout")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.scope("read")
				.scope("write")
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofMinutes(1))  // Token válido 5 minutos
						.build())
				.build();

		return new InMemoryRegisteredClientRepository(oidcClient, oauthClient);
	}

	// Configuración de parámetros del servidor de autorización
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	// Uso del RemoteUserDetailsService para el UserDetailsService
	@Bean
	public UserDetailsService userDetailsService(RemoteUserDetailsService remoteUserDetailsService) {
		return remoteUserDetailsService;
	}
}
