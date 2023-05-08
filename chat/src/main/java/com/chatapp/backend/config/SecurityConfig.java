package com.chatapp.backend.config;

import com.chatapp.backend.filter.CustomAuthorizationFilter;
import com.chatapp.backend.filter.CustomAuthenticationFilter;
import com.chatapp.backend.util.JsonUtil;
import com.chatapp.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

/**
 * Provides security configuration for the application.
 *
 * <p>
 * This configuration class extends {@link WebSecurityConfigurerAdapter} and provides methods for configuring
 * authentication and authorization using Spring Security. It uses a {@link UserDetailsService} implementation and a
 * {@link BCryptPasswordEncoder} to hash and compare passwords.
 * </p>
 *
 * <p>
 * The authentication process is handled by a custom filter called {@link CustomAuthenticationFilter}. This filter
 * processes login requests and generates JSON Web Tokens (JWT) to be used for subsequent requests. The authorization
 * process is handled by a custom filter called {@link CustomAuthorizationFilter}. This filter checks for the presence
 * of a valid JWT in the request header and authorizes the request accordingly.
 * </p>
 *
 * <p>
 * This class allows access to the /api/login, /api/register, and /stream/** endpoints without authentication. All
 * other requests require authentication.
 * </p>
 *
 * <p>
 * This code is adapted from a tutorial by Get Arrays (https://www.getarrays.io/).
 * </p>
 *
 * @version 1.0
 * @since 7/10/2021
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean(),
                        JwtUtil.getInstance(), JsonUtil.getSingleton());

        http.cors();
        http.csrf().disable();
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login", "/api/register", "/stream/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(JwtUtil.getInstance()),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
