package com.eviive.personalapi;

import com.eviive.personalapi.model.*;
import com.eviive.personalapi.service.ProjectService;
import com.eviive.personalapi.service.RoleService;
import com.eviive.personalapi.service.SkillService;
import com.eviive.personalapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class App {
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner commandLineRunner(
			UserService userService,
			RoleService roleService,
			ProjectService projectService,
			SkillService skillService
	) {
		return args -> {
			userService.save(new ApiUser(
					null,
					"Albert VAILLON",
					"Eviive",
					"12345",
					new ArrayList<>()
			));
			
			userService.save(new ApiUser(
					null,
					"Lilian BAUDRY",
					"Irophin",
					"6789",
					new ArrayList<>()
			));
			
			roleService.save(new Role(null, "ROLE_ADMIN"));
			roleService.save(new Role(null, "ROLE_USER"));
			
			userService.addRoleToUser(1L, 1L, 2L);
			userService.addRoleToUser(2L, 2L);
			
			skillService.save(new Skill(null, "Skill 1", "skill1.png"));
			skillService.save(new Skill(null, "Skill 2", "skill2.png"));
			skillService.save(new Skill(null, "Skill 3", "skill3.png"));
			
			Image image1 = new Image("image1.png", "Image 1's description");
			projectService.save(new Project(
					null,
					"Project 1",
					"Project 1's description",
					"https://github.com/user/Project1",
					"https://user.github.io/Project1",
					new ArrayList<>(),
					image1,
					false
			));

			Image image2 = new Image("image2.png", "Image 2's description");
			projectService.save(new Project(
					null,
					"Project 2",
					"Project 2's description",
					"https://github.com/user/Project2",
					"https://user.github.io/Project2",
					new ArrayList<>(),
					image2,
					true
			));
			
			projectService.addSkillToProject(1L, 1L, 2L);
			projectService.addSkillToProject(2L, 2L, 3L);
		};
	}
	
}