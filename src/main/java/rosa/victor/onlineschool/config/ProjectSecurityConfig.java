package rosa.victor.onlineschool.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/saveMsg");
        http.csrf().ignoringAntMatchers("/public/**");


        http.authorizeRequests(
            resquests -> resquests.mvcMatchers("/home").permitAll()
                                  .mvcMatchers("/holidays/**").permitAll()
                                  .mvcMatchers("/contact").permitAll()
                                  .mvcMatchers("/saveMsg").permitAll()
                                  .mvcMatchers("/courses").permitAll()
                                  .mvcMatchers("/about").permitAll()
                                  .mvcMatchers("/login").permitAll()
                                  .mvcMatchers("/login?error=true").permitAll()
                                  .mvcMatchers("/login?logout=true").permitAll()
                                  .mvcMatchers("/dashboard").authenticated()
                                  .mvcMatchers("/displayProfile").authenticated()
                                  .mvcMatchers("/updateProfile").authenticated()
                                  .mvcMatchers("/admin/**").hasRole("ADMIN")
                                  .mvcMatchers("/displayMessages").hasRole("ADMIN")
                                  .mvcMatchers("/public/**").permitAll()
                                  .mvcMatchers("/student/**").hasRole("STUDENT")
        );
        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard")
            .failureUrl("/login?error=true")
            .and()
            .logout()
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true);
        http.httpBasic();


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
}
