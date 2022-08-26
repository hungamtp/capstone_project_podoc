package com.capstone.pod.security;

import com.capstone.pod.auth.ApplicationUserService;
import com.capstone.pod.jwt.AccountVerifier;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.jwt.TokenVerifier;
import com.capstone.pod.repositories.CredentialRepository;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationUserService applicationUserService;
    @Autowired
    private  CredentialRepository credentialRepository;
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilterBefore(new TokenVerifier(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AccountVerifier(credentialRepository),UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/auth/register",
                        "/auth/login",
                        "/product/**",
                        "/products/{id}",
                        "/products/rating/{id}",
                        "/webjars/**",
                        "/design/**",
                        "/user/**",
                        "/category/**",
                        "/rating/**",
                        "/order/**")
                .permitAll().anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(applicationUserService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public javax.crypto.SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }
        };
    }

}
