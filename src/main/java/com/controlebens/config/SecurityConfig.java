package com.controlebens.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
	    @Autowired
	    private SecurityFilter securityFilter;


	    @Bean
	    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
	        return httpSecurity
	                .csrf(csrf -> csrf.disable())
	                .cors(cors -> {})
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/docs").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/funcionario/login").permitAll()
	                        .requestMatchers("/hello").permitAll()
	                        .requestMatchers("/bem").hasRole("USER")
	                        .requestMatchers("/local").hasRole("USER")
	                        .anyRequest().hasRole("ADMIN")
	                )
	                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //verifica antes das condições anteriores se tem o filtro autenticado
	                .build();
	    }

	    @Bean
	    AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) throws Exception{
	        return authConfiguration.getAuthenticationManager();
	    }

	    @Bean
	    PasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }

}
