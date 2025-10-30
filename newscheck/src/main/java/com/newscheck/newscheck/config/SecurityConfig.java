package com.newscheck.newscheck.config;

import com.newscheck.newscheck.services.implementations.JwtAuthenticationFilter;
import com.newscheck.newscheck.services.implementations.authService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//An important class. Stores the config for Sprint security used by the Application.
//Comprised of JWTAuthentication, JWTAuthenticationFilter, enabling of CORS,
//DaoAuthenticationProvider, PasswordEncoder, and specifies which endpoints need
//authentiation and those which don't
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:4200"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;//Cors setup
                }))
                .csrf(csrf -> csrf.disable())//Crsf disabledd
                .logout(logout -> logout.disable())//Logout is handled by the app
                .authorizeHttpRequests(auth -> auth
                        //These endpoints don't need any authentication
                        .requestMatchers("/login", "/register", "/reset-password", "/forgot-password", "/logout", "/request-reset", "/request-forgot").permitAll()
                        //Admin endpoints need authentication
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //Verificaiton endpoints need authentication
                        .requestMatchers("/api/verification/**").authenticated()
                        .anyRequest().authenticated() //Any other not specified above endpoints need authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//Stateless requests
                )
                .authenticationProvider(authenticationProvider)//Sets authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //Sets JWTAuthenticationFilter and PasswordAuthenticationFilter
        return http.build();
    }

    @Bean //The Authentication Provider
    public AuthenticationProvider authenticationProvider(authService authService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean //The Authentication Manager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean //Password encoder using BCrypt
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}