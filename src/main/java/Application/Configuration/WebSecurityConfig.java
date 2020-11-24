package Application.Configuration;

import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/forgot_password").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/authorization").permitAll()
                .antMatchers("/verification/**").permitAll()
                .antMatchers("/message/**").permitAll()
                .antMatchers("/friends/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/user-all/**").permitAll()
                .antMatchers("/topic/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/posts/**").permitAll()
                .antMatchers("/post/**").permitAll()
                .antMatchers("/like/**").permitAll()
                .antMatchers("/comment/**").permitAll()
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/exit/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/", "/resources/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/");

        httpSecurity.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}