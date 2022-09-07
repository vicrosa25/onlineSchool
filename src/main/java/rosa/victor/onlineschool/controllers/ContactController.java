package rosa.victor.onlineschool.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import rosa.victor.onlineschool.model.Contact;
import rosa.victor.onlineschool.services.ContactService;


@Slf4j
@Controller
public class ContactController {

  private final ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  
  @RequestMapping("/contact")
  public String displayContactPage(Model model) {
    model.addAttribute("contact", new Contact()); 
    return "contact.html";
  }


  @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
  public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {
    
    if (errors.hasErrors()) {
      log.error("Contact form validation failded due to: " + errors.toString());
      return "contact.html";
    }
    
    try {
      
      contactService.saveMessageDetails(contact);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return "redirect:/contact";
  }

  @RequestMapping("/displayMessages/page/{pageNum}")
    public ModelAndView displayMessages(@PathVariable(name = "pageNum") int pageNum,
                                        @RequestParam("sortField") String sortField,
                                        @RequestParam("sortDir") String sortDir,
                                        Model model) {

        Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum,sortField, sortDir);
        List<Contact> contactMsgs = msgPage.getContent();

        ModelAndView modelAndView = new ModelAndView("messages.html");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", msgPage.getTotalPages());
        model.addAttribute("totalMsgs", msgPage.getTotalElements());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc": "asc");

        modelAndView.addObject("contactMsgs", contactMsgs);

        return modelAndView;

    }

    @RequestMapping("/closeMsg")
    public String closeMsg(@RequestParam int id) {
        contactService.updateMsgStatus(id);
        return "redirect:/displayMessages/page/1?sortField=name&sortDir=desc";
    }  
}
