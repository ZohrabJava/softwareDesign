package org.example.arithmetic.controller;

import org.example.arithmetic.service.ArithmeticService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arithmetic")
public class ArithmeticController {

    private final ArithmeticService arithmeticService;

    public ArithmeticController(ArithmeticService arithmeticService) {
        this.arithmeticService = arithmeticService;
    }

    @GetMapping("/divide")
    public float divide(@RequestParam float dividend, @RequestParam float divisor) {
        return arithmeticService.divide(dividend, divisor);
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArithmeticException(ArithmeticException e) {
        return e.getMessage();
    }
}
