package rosa.victor.onlineschool.controllers;

import javax.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import rosa.victor.onlineschool.model.User;


@Controller
@RequestMapping("student")
public class StudentController {


  @GetMapping("/displayCourses")
  public ModelAndView displayCourses(HttpSession session) {
    User user = (User) session.getAttribute("loggedUser");
    ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
    modelAndView.addObject("user", user);

    return modelAndView;
  }
}
