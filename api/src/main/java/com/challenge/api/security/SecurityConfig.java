package com.challenge.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures security for the employee REST API.
 * <p>Employee endpoints require HTTP Basic authentication and use stateless
 * request processing. Clients must communicate over HTTPS and supply credentials through
 *  external configuration
 * @implNote CSRF is disabled because this is a stateless, machine-to-machine API that does not use browser sessions or cookie-based auth. Requiring CSRF tokens would introduce unnecessary token/session coordination for the webhook client, which we do not have access to.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Builds the security filter chain for the employee API.
     *
     * @param http the HTTP security configuration
     * @return the configured security filter chain
     * @throws Exception if the security configuration cannot be built
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.requestMatchers("/api/v1/employee/**")
                        .authenticated()
                        .anyRequest()
                        .denyAll())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
