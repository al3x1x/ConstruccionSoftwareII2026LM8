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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\":401,\"message\":\"No autorizado - Token inválido o ausente\"}");
                })
            )
            .authorizeHttpRequests(authz -> authz
                // Liberar de forma absoluta CUALQUIER petición que contenga /auth/
                .requestMatchers("/api/auth/**", "/auth/**", "/api/auth/login", "/api/auth/register").permitAll()
                
                // Rutas del negocio protegidas con roles
                .requestMatchers(HttpMethod.GET, "/api/clients/**").hasAnyAuthority("COMMERCIAL_EMPLOYEE", "COMPANY_SUPERVISOR", "INTERNAL_ANALYST")
                .requestMatchers(HttpMethod.POST, "/api/loans/**").hasAnyAuthority("INTERNAL_ANALYST", "COMPANY_SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/transfers").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/accounts/**").hasAnyAuthority("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "NATURAL_PERSON_CLIENT", "COMPANY_CLIENT")
                
                // Cualquier otra requiere token
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}