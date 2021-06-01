package Classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Classes.BuisnessLogic.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	UserService service;
	
	
	@Bean 
	 public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http

    	.csrf().disable()
        .authorizeRequests()
            //Доступ разрешен всем пользователей
            .antMatchers("/Login", "/Registration", "/css/**", "/js/**", "/images/**", "/confirm-account").permitAll()
            //.antMatchers("/Dialogs").hasRole("USER")
        //Все остальные страницы требуют аутентификации
        .anyRequest().authenticated()
        .and()
            //Настройка для входа в систему
            .formLogin()
            .loginPage("/Login")
            //Перенарпавление на главную страницу после успешного входа
            .defaultSuccessUrl("/")
            .permitAll()
        .and()
            .logout()
            .permitAll()
            .logoutSuccessUrl("/Login");
    	
    	
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(encoder());
       
    }

}