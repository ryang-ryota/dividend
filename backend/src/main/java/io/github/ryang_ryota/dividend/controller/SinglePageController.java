package io.github.ryang_ryota.dividend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*")
public class SinglePageController {
    @GetMapping("/{path:^(?!api|static|public).*$}/**")
    public String redirect() {
        return "forward:/index.html";
    }
}
