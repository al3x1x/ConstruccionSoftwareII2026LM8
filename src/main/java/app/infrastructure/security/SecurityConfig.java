package app.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    // Escribimos el JSON directamente como una cadena de texto
                    response.getWriter().write("{\"status\":401,\"message\":\"No autorizado - Token inválido o ausente\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    // Escribimos el JSON directamente como una cadena de texto
                    response.getWriter().write("{\"status\":403,\"message\":\"Acceso denegado - No tienes los permisos requeridos\"}");
                })
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/clients/**").hasAnyRole("COMMERCIAL_EMPLOYEE", "COMPANY_SUPERVISOR", "INTERNAL_ANALYST")
                .requestMatchers(HttpMethod.POST, "/api/loans/**").hasAnyRole("INTERNAL_ANALYST", "COMPANY_SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/transfers").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/accounts/**").hasAnyRole("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "NATURAL_PERSON_CLIENT", "COMPANY_CLIENT")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}