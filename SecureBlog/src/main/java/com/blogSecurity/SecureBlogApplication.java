package com.blogSecurity;

import com.blogSecurity.authApp.domain.model.Roles;
import com.blogSecurity.authApp.domain.repository.RoleRepository;
import com.blogSecurity.postApp.domain.model.Tag;
import com.blogSecurity.postApp.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
	private final TagRepository tagRepository;

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SecureBlogApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		Roles role1 = Roles.builder().name("SUPER_ADMIN").build();
//		Roles role2 = Roles.builder().name("ADMIN").build();
//		Roles role3 = Roles.builder().name("USER").build();
//		roleRepository.save(role1);
//		roleRepository.save(role2);
//		roleRepository.save(role3);
//
//		Tag tag1 = Tag.builder().name("FASHION").build();
//		tagRepository.save(tag1);

	}
}
