package com.hepsi.todoapp.config;

import com.hepsi.todoapp.manager.AuthManagerImpl;
import com.hepsi.todoapp.security.jwt.AuthTokenFilter;
import com.hepsi.todoapp.security.jwt.AuthEntryPointJwt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    @AllArgsConstructor
    public static class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter{
        private final AuthManagerImpl authManagerImpl;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private AuthEntryPointJwt unauthorizedHandler;

        @Bean
        public AuthTokenFilter authenticationJwtTokenFilter() {
            return new AuthTokenFilter();
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests(configurer ->
                            configurer
                                    .antMatchers(
                                            "/api/personal/auth/**",
                                            "/api/company/auth/**",
                                            "/api/company/get-overview/**",
                                            "/api/event/get-overviews/**",
                                            "/api/sub-account/auth/**",
                                            "/api/auth/**",
                                            "/api/country/**",
                                            "/api/job-title/**",
                                            "/api/industry-title/**",
                                            "/api/package/**",
                                            "/swagger-ui/**",
                                            "/v3/api-docs/**",
                                            "/image/**",
                                            "/api/event/get/**",
                                            "/api/event/get-by-company/**",
                                            "/api/event/get-all/**",
                                            "/api/job/get/**",
                                            "/api/job/get-all/**",
                                            "/api/job/get-by-company/**"

                                    )
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated()
                    );

            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(authManagerImpl).passwordEncoder(bCryptPasswordEncoder);
        }
    }
}
