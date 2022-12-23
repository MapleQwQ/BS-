package com.example.server;

import com.example.server.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.server.entity.AppUser;
@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
//	  @Bean
//	  CommandLineRunner run(AppUserService appUserService){
//    return args -> {
//      appUserService.create(new AppUser("USER_TREST","email",1,"123456"));
//      appUserService.create(new AppUser("USER_TREST2","email",1,"123456"));
//      appUserService.create(new AppUser("USER_TREST3","email",1,"123456"));
//      appUserService.create(new AppUser("USER_TREST4","email",1,"123456"));
//    };

}
