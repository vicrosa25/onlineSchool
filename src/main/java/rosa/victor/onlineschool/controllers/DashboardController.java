package rosa.victor.onlineschool.controllers;

import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.UserRepository;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session) {

        User user = userRepository.getByEmail(authentication.getName());
        System.out.println(user);
        model.addAttribute("username", user.getName());
        model.addAttribute("roles", authentication.getAuthorities().toArray());
        session.setAttribute("loggedUser", user);
        return "dashboard.html";
    }
}
