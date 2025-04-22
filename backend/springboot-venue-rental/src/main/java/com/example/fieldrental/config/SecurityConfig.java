package com.example.fieldrental.config;

import com.example.fieldrental.entity.User;
import com.example.fieldrental.handler.JwtAuthFilter;
import com.example.fieldrental.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    final private UserService userService;


    @Bean
    public UserDetailsService userDetailsService() {
        return username ->{
            User user = userService.getUserByUsername(username);
            if (user == null)
            {
                throw new UsernameNotFoundException("Username not found");
            }
            return user;
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false); // 返回时是否生成凭证
        // 当allowCredentials为true时，allowedOrigins不能包含特殊值“*”，因为它不能在“Access-Control-Allow-Origin”响应头中设置。要允许凭据到一组来源，请显式列出它们，或者考虑使用“allowedOriginPatterns”。
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setMaxAge(Duration.ofHours(1));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

//    @Bean
//    SecurityFilterChain securityFilterChain() {
//        List<Filter> filters = new ArrayList<>();
//        return new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"), filters);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable());
        http
                // 如果 不添加的话会出现 csrf 阻拦外部请求 如果不这么写可能需要后面加一个cdn做数据代理 但也有更新的写法但我没有找到 但更好的办法就是添加一个 repository 去处理这个 csrf 添加一个 csrfToken处理机制 毕竟这里不是 企业级服务器 没必要搞那么强大功能
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.qu)
                .authorizeHttpRequests()
                .requestMatchers( "/login/**"  , "/webjars/**" , "/swagger-resources/**","/image/**").permitAll()
                .requestMatchers( "/register/**","/comment/**").permitAll()
                .requestMatchers(  "/alipay/notify","/field/getByAddress/**","/field/get/**","/field/getByName/**","/field/search/**","/field/all","/field/is/**").permitAll()
                .requestMatchers( "/v2/**","/v3/**").permitAll()
                .requestMatchers( "/doc.html/**","/swagger-ui.html/**","/doc.html#/**" ,"/swagger-ui/**","/error/**").permitAll()
                .requestMatchers("/field/getPosition/**","/field/date/**","/user/**").permitAll()
                .anyRequest().authenticated();
//                http.authenticationProvider(authProvider); // 似乎在 6.2版本中被移除 现在使用 customizer 会自动导入一个 passwordusernameprovider 存疑 后面看底层
                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


//   使用方法级的 安全认证  @PreAuthorize("hasAuthority('USER')")

}