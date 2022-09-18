package by.maksimruksha.mapgeneration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class BasicController {

    @GetMapping("/{who}")
    public String hello(@PathVariable String who)
    {
        return "Hello, " + who;
    }
}
