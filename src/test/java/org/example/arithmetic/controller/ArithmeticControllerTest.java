package org.example.arithmetic.controller;

import org.example.arithmetic.service.ArithmeticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArithmeticController.class)
class ArithmeticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArithmeticService arithmeticService;

    @Test
    void divideReturnsOkWithResult() throws Exception {
        when(arithmeticService.divide(10.4f, 2f)).thenReturn(5.2f);

        mockMvc.perform(get("/api/arithmetic/divide")
                        .param("dividend", "10.4")
                        .param("divisor", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.2"));
    }

    @Test
    void divideByZeroReturnsBadRequestWithErrorMessage() throws Exception {
        when(arithmeticService.divide(10f, 0f)).thenThrow(new ArithmeticException("Բաժանումն ապարիզ է"));

        mockMvc.perform(get("/api/arithmetic/divide")
                        .param("dividend", "10")
                        .param("divisor", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Բաժանումն ապարիզ է"));
    }

    @Test
    void exceptionHandlerReturnsExceptionMessage() {
        ArithmeticController controller = new ArithmeticController(arithmeticService);
        String result = controller.handleArithmeticException(new ArithmeticException("custom error"));
        assertEquals("custom error", result);
    }

    @Test
    void nonNumericDividendReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/arithmetic/divide")
                        .param("dividend", "abc")
                        .param("divisor", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingDivisorReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/arithmetic/divide")
                        .param("dividend", "10"))
                .andExpect(status().isBadRequest());
    }
}
