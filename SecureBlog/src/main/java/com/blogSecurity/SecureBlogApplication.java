package com.blogSecurity;

import com.blogSecurity.model.Roles;
import com.blogSecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class SecureBlogApplication implements ApplicationRunner {
	private final RoleRepository roleRepository;

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SecureBlogApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Roles roles1 = Roles.builder().name("ADMIN").build();
		Roles roles2 = Roles.builder().name("USER").build();
		roleRepository.save(roles1);
		roleRepository.save(roles2);
	}
}
