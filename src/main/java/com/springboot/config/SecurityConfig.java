package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ปิด CSRF สำหรับการทดสอบ (เปิดภายหลังได้)
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
            	.requestMatchers("/", "/login", "/register", "/about", "/contact").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // ✅ ใช้หน้า login.html
                .loginProcessingUrl("/login") // action ของ form
                .defaultSuccessUrl("/profile", true) // หลัง login สำเร็จไปหน้าแรก
                .failureUrl("/login?error=true") // ถ้า login fail
                .permitAll()
            )
            .logout(logout -> logout
            		.logoutUrl("/logout")
            	    .logoutSuccessUrl("/")
            	    .invalidateHttpSession(true)          // ล้าง session ปัจจุบัน
            	    .deleteCookies("JSESSIONID")          // ลบ cookie session
            	    .permitAll()
            );

        return http.build();
    }

    // ✅ ใช้เข้ารหัส password ด้วย BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
