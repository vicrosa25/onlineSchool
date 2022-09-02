package rosa.victor.onlineschool.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import rosa.victor.onlineschool.model.Address;
import rosa.victor.onlineschool.model.Profile;
import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class ProfileController {

  @Autowired
  private UserRepository userRepository;


  @RequestMapping("/displayProfile")
  public ModelAndView displayMessages(Model model, HttpSession session) {
      User user = (User) session.getAttribute("loggedUser");
      Profile profile = new Profile();
      profile.setName(user.getName());
      profile.setMobileNumber(user.getMobileNumber());
      profile.setEmail(user.getEmail());

      if(user.getAddress() != null && user.getAddress().getAddressId() > 0) {
        profile.setAddress1(user.getAddress().getAddress1());
        profile.setAddress2(user.getAddress().getAddress2());
        profile.setCity(user.getAddress().getCity());
        profile.setState(user.getAddress().getState());
        profile.setZipCode(user.getAddress().getZipCode());
      }

      ModelAndView modelAndView = new ModelAndView("profile.html");
      modelAndView.addObject("profile", profile);

      return modelAndView;
  }

  @PostMapping(value = "/updateProfile")
  public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors, HttpSession session) {
      if(errors.hasErrors()) {
        return "profile.html";
      }

      User user = (User) session.getAttribute("loggedUser");
      user.setName(profile.getName());
      user.setEmail(profile.getEmail());
      user.setMobileNumber(profile.getMobileNumber());

      if(user.getAddress() == null || !(user.getAddress().getAddressId() > 0)) {
        user.setAddress(new Address());
      }

      user.getAddress().setAddress1(profile.getAddress1());
      user.getAddress().setAddress2(profile.getAddress2());
      user.getAddress().setCity(profile.getCity());
      user.getAddress().setState(profile.getState());
      user.getAddress().setZipCode(profile.getZipCode());

      userRepository.save(user);

      session.setAttribute("loggedUser", user);

      return "redirect:/displayProfile";
  }



}
