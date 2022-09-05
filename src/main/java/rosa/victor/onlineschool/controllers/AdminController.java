package rosa.victor.onlineschool.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import rosa.victor.onlineschool.model.OnlineClass;
import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.OnlineClassRepository;
import rosa.victor.onlineschool.repository.UserRepository;


import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("admin")
public class AdminController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OnlineClassRepository onlineClassRepository;

  @RequestMapping("/displayClasses")
  public ModelAndView displayClasses(Model model) {
    List<OnlineClass> onlineClasses = onlineClassRepository.findAll();
    ModelAndView modelAndView = new ModelAndView("classes.html");
    modelAndView.addObject("onlineClass", new OnlineClass());
    modelAndView.addObject("onlineClasses", onlineClasses);
    return modelAndView;
  }

  @PostMapping("/addNewClass")
  public ModelAndView addNewClass(@Valid @ModelAttribute("onlineClass") OnlineClass onlineClass) {

    onlineClassRepository.save(onlineClass);
    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");

    return modelAndView;
  }

  @RequestMapping("/deleteClass")
  public ModelAndView deleteClass(Model model, @RequestParam int id) {
    Optional<OnlineClass> onlineClass = onlineClassRepository.findById(id);

    for( User user : onlineClass.get().getStudents()) {
      user.setOnlineClass(null);
      userRepository.save(user);
    }

    onlineClassRepository.deleteById(id);

    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");

    return modelAndView;

  }

  @RequestMapping("/displayStudents")
  public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                      @RequestParam(value = "error", required = false) String error) {

    String errorMessage = null;
    ModelAndView modelAndView = new ModelAndView("students.html");
    Optional<OnlineClass> onlineClass = onlineClassRepository.findById(classId);
    modelAndView.addObject("onlineClass", onlineClass.get());
    modelAndView.addObject("user", new User());
    session.setAttribute("onlineClass", onlineClass.get());
    if(error != null) {
      errorMessage = "Invalid Email !!!!";
      modelAndView.addObject("errorMessage", errorMessage);
    }
    return modelAndView;
  }

  @PostMapping("/addStudent")
  public ModelAndView addStudent(Model model, @ModelAttribute("user") User user, HttpSession session){
    ModelAndView modelAndView = new ModelAndView();
    OnlineClass onlineClass = (OnlineClass) session.getAttribute("onlineClass");
    User student = userRepository.getByEmail(user.getEmail());

    if(student == null || !(student.getUserId() > 0)) {
      modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + onlineClass.getClassId() + "&error=true");
      return modelAndView;
    }

    student.setOnlineClass(onlineClass);
    userRepository.save(student);
    onlineClass.getStudents().add(student);
    onlineClassRepository.save(onlineClass);

    modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + onlineClass.getClassId());
    return modelAndView;
  }

  @GetMapping("/deleteStudent")
  public ModelAndView deleteStudent(Model model, @RequestParam int userId, HttpSession session) {
    OnlineClass onlineClass = (OnlineClass) session.getAttribute("onlineClass");
    Optional<User> student = userRepository.findById(userId);
    student.get().setOnlineClass(null);
    onlineClass.getStudents().remove(student.get());
    OnlineClass onlineClassSaved = onlineClassRepository.save(onlineClass);
    session.setAttribute("onlineClass", onlineClassSaved);

    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId=" + onlineClass.getClassId());

    return modelAndView;
  }
 


}
