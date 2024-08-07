package one.digitalinnovation.parking.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    public String hello() {
        return "Hello DIO. Java dev";
    }
}
