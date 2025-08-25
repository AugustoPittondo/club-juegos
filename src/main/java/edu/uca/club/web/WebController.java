package edu.uca.club.web;

import edu.uca.club.repo.ClubRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    private final ClubRepo repo;
    public WebController(ClubRepo repo){ this.repo = repo; }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("socios", repo.socios());
        model.addAttribute("juegos", repo.juegos());
        return "home"; // templates/home.html
    }
}
