package com.morelos.mercado.web.security;

import com.morelos.mercado.domain.service.MercadoUserDetailsService;
import com.morelos.mercado.web.config.JwtAuthenticationEntryPoint;
import com.morelos.mercado.web.security.filter.CustomAccessDeniedHandler;
import com.morelos.mercado.web.security.filter.JwtFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // clase encargada de la seguridad
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MercadoUserDetailsService mercadoUserDetailsService;

    @Autowired
    private JwtFilterRequest jwtFilterRequest;
    /*@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mercadoUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**/authenticate", "/v2/api-docs", "/configuration/ui",
                        "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().authenticated().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler()).
                //        authenticationEntryPoint(jwtAuthenticationEntryPoint).
                        and().sessionManagement()// indicar que jwtFilterRequest, este filtro se encarga
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);// STATELESS =>sin sesión, porque jwt se encarga controla cada petición en particular sin manejar una sesión como tal


        //indicar el (filtro , tipo de filtro )
        http.addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

   /* @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }*/

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
