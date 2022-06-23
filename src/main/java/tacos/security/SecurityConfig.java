package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    //     List<UserDetails> usersList = new ArrayList<>();

    //     usersList.add(
    //         new User(
    //             "buzz",
    //             encoder.encode("password"),
    //             Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
    //         )
    //     );

    //     usersList.add(
    //         new User(
    //             "woody",
    //             encoder.encode("password"),
    //             Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
    //         )
    //     );

    //     return new InMemoryUserDetailsManager(usersList);
    // }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            tacos.User user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        .cors().disable()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/design", "/orders").access("hasRole('USER')")
        .and()
        .formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
        .and()
        .logout().logoutSuccessUrl("/")
        .and()
        .build();
    }

    // THE SECOND WAY TO IMPLEMENT filterChain() by .access():

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     return http
    //         .authorizeRequests()
    //         .antMatchers("/design", "/orders")
    //         .hasRole("USER")  // role "ROLE_USER"
    //         .antMatchers("/", "/**")
    //         .permitAll()
    //         .and()
    //         .formLogin()
    //         .loginPage("/login")
    //         .and()
    //         .build();
    // }

}