package rosa.victor.onlineschool.controllers;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rosa.victor.onlineschool.model.Holiday;
import rosa.victor.onlineschool.repository.HolidaysRepository;

@Controller
public class HolidaysController {

  @Autowired
  private HolidaysRepository holidaysRepository;

  @GetMapping("/holidays/{display}")
  public String displayHolidays(@PathVariable String display, Model model) {

    if(null != display  && display.equals("all")) {
      model.addAttribute("federal", true);
      model.addAttribute("festival", true);
    } else if (display != null && display.equals("federal")) {
        model.addAttribute("federal", true);
    } else if (display != null && display.equals("festival")) {
        model.addAttribute("festival", true);
    }

    Iterable<Holiday> holidays = holidaysRepository.findAll();
    List<Holiday> holidaysList = StreamSupport.stream(holidays.spliterator(), false)
                                              .collect(Collectors.toList());

    Holiday.Type[] types = Holiday.Type.values();

    for (Holiday.Type type  : types) {
        model.addAttribute(type.toString(),
                         (holidaysList.stream().filter(holiday -> holiday.getType().equals(type))
                                           .collect(Collectors.toList())));
    }
    return "holidays.html";
  }
  
}
