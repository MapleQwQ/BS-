package com.example.server.security;

import com.example.server.filter.AuthenticationFilter;
import com.example.server.filter.AuthorizationFilter;
import com.example.server.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private UserDetailsServiceImpl userDetailsService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  //控制认证请求
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
                .antMatchers("/**").permitAll()

//下面全部注释
        .antMatchers("/users/login").permitAll()
//                .antMatchers("users/**").permitAll()
//                .antMatchers("/users/login").permitAll()
        .antMatchers("/users/register").permitAll()
        .antMatchers("/categories/**").permitAll()
        .antMatchers("/entries/**").permitAll()
        .antMatchers("/entriesofcat/**").permitAll()



//                .antMatchers("/**").permitAll()
//                .antMatchers("/users/**","/entries/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilter(new AuthenticationFilter(authenticationManager()))
        .addFilter(new AuthorizationFilter(authenticationManager()))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().cors().and().csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    List<String> list = new ArrayList<String>();
    list.add("*");
    corsConfiguration.setAllowedMethods(list);
    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;
  }
}
