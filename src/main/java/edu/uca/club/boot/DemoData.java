package edu.uca.club.boot;

import edu.uca.club.domain.Juego;
import edu.uca.club.domain.Socio;
import edu.uca.club.repo.ClubRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoData {
    @Bean
    CommandLineRunner seed(ClubRepo repo){
        return args -> {
            repo.save(new Socio("Ana")); repo.save(new Socio("Luis")); repo.save(new Socio("Mora"));
            repo.save(new Juego("Catan")); repo.save(new Juego("Azul"));
        };
    }
}