package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tacos.data.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            tacos.User user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        return http
        .authorizeRequests()
        .antMatchers("/design", "/orders/**").access("hasRole('ROLE_USER')")
        .antMatchers("/", "/**").access("permitAll")
        .and()
        .formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
        .and()
        .logout().logoutSuccessUrl("/")
        .and()
        .csrf().disable()
        .cors().disable()
        .build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodFilterChain(HttpSecurity http) throws Exception {
        return http
        .authorizeRequests()
        .antMatchers("/design", "/orders/**").access("hasRole('ROLE_USER')")
        .antMatchers("/", "/**").access("permitAll")
        .and()
        .formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
        .and()
        .logout().logoutSuccessUrl("/")
        .and()
        .csrf().ignoringAntMatchers("/h2-console/**")
        .and()
        .headers().frameOptions().sameOrigin()
        .and()
        .build();
    }

}