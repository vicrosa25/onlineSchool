package rosa.victor.onlineschool.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import rosa.victor.onlineschool.model.Course;
import rosa.victor.onlineschool.model.OnlineClass;
import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.CourseRepositoy;
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

  @Autowired
  private CourseRepositoy courseRepositoy;



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


  @GetMapping("/displayCourses")
  public ModelAndView displayCourses() {
 
    // List<Course> courses = courseRepositoy.findByOrderByNameDesc();
    List<Course> courses = courseRepositoy.findAll(Sort.by("name").descending());
    ModelAndView modelAndView = new ModelAndView("courses_secure.html");
    modelAndView.addObject("courses", courses);
    modelAndView.addObject("course", new Course());

    return modelAndView;

  }

  @PostMapping(value="addNewCourse")
  public ModelAndView addNewCourse(@ModelAttribute("course") Course course) {
      
    courseRepositoy.save(course);
    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayCourses");

    return modelAndView;
  }


  @GetMapping(value="viewStudents")
  public ModelAndView viewStudents(@RequestParam int id, HttpSession session,
                                   @RequestParam(required = false) String error) {

    String errorMessage = null;
    ModelAndView modelAndView = new ModelAndView("course_students.html");
    Optional<Course> course = courseRepositoy.findById(id);
    modelAndView.addObject("course", course.get());
    modelAndView.addObject("user", new User());
    session.setAttribute("course", course.get());

    if (error != null) {
      errorMessage = "Invalid Email entered";
      modelAndView.addObject("errorMessage", errorMessage);
    }

    return modelAndView;
  }


  @PostMapping(value = "addStudentToCourse")
  public ModelAndView addStudentToCourse(@ModelAttribute("user") User user, HttpSession session) {
    ModelAndView modelAndView = new ModelAndView();
    
    
    Course course = (Course) session.getAttribute("course");
    User userEntity = userRepository.getByEmail(user.getEmail());
    
    if(userEntity == null || !(userEntity.getUserId() > 0)) {
      modelAndView.setViewName("redirect:/admin/viewStudents?id=" + course.getCourseId() + "&error=true");
      return modelAndView;
    }
        
    course.getUsers().add(userEntity);
    userEntity.getCourses().add(course);
    userRepository.save(userEntity);

    session.setAttribute("course", course);

    modelAndView.setViewName("redirect:/admin/viewStudents?id=" + course.getCourseId());
    return modelAndView;
  }
  
  @GetMapping("deleteStudentFromCourse")
  public ModelAndView deleteStudentFromCourse(@RequestParam int userId, HttpSession session) {
    
    Course course = (Course) session.getAttribute("course");
    Optional<User> user = userRepository.findById(userId);

    course.getUsers().remove(user.get());
    user.get().getCourses().remove(course);
    userRepository.save(user.get());

    ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id=" + course.getCourseId());
    return modelAndView;
  
  }
  

 


}
