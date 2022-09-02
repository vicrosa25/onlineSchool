package rosa.victor.onlineschool.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController {
  
  @ExceptionHandler(Exception.class)
  public ModelAndView exceptionHandler(Exception exception) {
    ModelAndView errorPage = new ModelAndView();
    errorPage.setViewName("error");
    errorPage.addObject("errormsg", exception.getMessage());

    return errorPage;
  }
}
