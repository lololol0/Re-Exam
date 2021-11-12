package com.example.ReExam.config;

import com.example.ReExam.models.User;
import com.example.ReExam.repo.UserDetailsRepo;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/", "/login**", "/js/**", "/error**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                .and()
                    .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepository) {
        return map -> {
            String id = (String) map.get("sub");
            User user = userDetailsRepository.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setUsername((String) map.get("name"));
                return newUser;
            });
            return userDetailsRepository.save(user);
        };
    }
}
