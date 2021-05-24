package sk.majo.attendancesystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/{path:[^\\\\.]+}", "/logout", "/prihlasenie", "/403", "/{path:[^\\\\.]+}/{path:[^\\\\.]+}" })
public class VueController {

    @GetMapping
    public String vueIndex() {
        return "forward:/index.html";
    }
}
