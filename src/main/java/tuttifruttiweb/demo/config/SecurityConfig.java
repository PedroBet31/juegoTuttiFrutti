/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuración principal de seguridad. Define qué rutas requieren login,
 * cómo funciona el formulario, el manejo de sesión y el encoder de contraseñas.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(f -> f.disable()))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/h2/**"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**")
                ).permitAll()
                .anyRequest().authenticated()
            )

            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/lobby", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(false)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/login?expired")
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
