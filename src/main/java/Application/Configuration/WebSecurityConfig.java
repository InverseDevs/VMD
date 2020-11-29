package Application.Configuration;

import Application.Security.CORSFilter;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(new CORSFilter(), LogoutFilter.class)
                .authorizeRequests()
                .antMatchers("/websocket-chat/**").permitAll().and()
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
                .antMatchers("/chats/**").permitAll()
                .antMatchers("/user-all/**").permitAll()
                .antMatchers("/topic/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/posts/**").permitAll()
                .antMatchers("/group/**").permitAll()
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

        httpSecurity.headers()
                .frameOptions()
                .disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}