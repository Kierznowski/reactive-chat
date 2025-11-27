package com.example.user_service;

import com.example.user_service.model.Room;
import com.example.user_service.model.User;
import com.example.user_service.repository.RoomRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

    @Bean
    public ApplicationRunner dataLoader(RoomRepository roomRepository, UserRepository userRepository) {
        return args -> {
            Optional<User> userOpt = userRepository.findByEmail("bill@test.com");
            Optional<User> userOpt2 = userRepository.findByEmail("alice@test.com");
            User user;
            User user2;

            if (userOpt.isEmpty()) {
                    user = new User();
                    user.setEmail("bill@test.com");
                    user.setUsername("bill");
                    user.setPasswordHash(new BCryptPasswordEncoder().encode("pass"));
                    userRepository.save(user);
            } else {
                user = userOpt.get();
            }

            if (userOpt2.isEmpty()) {
                user2 = new User();
                user2.setEmail("alice@test.com");
                user2.setUsername("alice");
                user2.setPasswordHash(new BCryptPasswordEncoder().encode("pass"));
                userRepository.save(user);
            } else {
                user2 = userOpt2.get();
            }

                Room sampleRoom = new Room();
                sampleRoom.setName("Test room");
                sampleRoom.setOwner(user);

                user.getRooms().add(sampleRoom);
                user2.getRooms().add(sampleRoom);

                roomRepository.save(sampleRoom);
                userRepository.save(user);
                userRepository.save(user2);
                System.out.println("Loaded sample room");
        };
    }

}
