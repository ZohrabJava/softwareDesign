package org.example.arithmetik;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArithmetikController {

    @GetMapping("/api/arithmetik/divide")
    public float divide(@RequestParam float dividend, @RequestParam float divisor) {
        return LessonOne.divide(dividend, divisor);
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArithmeticException(ArithmeticException e) {
        return e.getMessage();
    }
}
