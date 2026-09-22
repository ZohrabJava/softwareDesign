package org.example.arithmetik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArithmetikController.class)
class ArithmetikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void divideReturnsOkWithResult() throws Exception {
        mockMvc.perform(get("/api/arithmetik/divide")
                        .param("dividend", "10.4")
                        .param("divisor", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.2"));
    }

    @Test
    void divideByZeroReturnsBadRequestWithErrorMessage() throws Exception {
        mockMvc.perform(get("/api/arithmetik/divide")
                        .param("dividend", "10")
                        .param("divisor", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Բաժանումն ապարիզ է"));
    }

    @Test
    void exceptionHandlerReturnsExceptionMessage() {
        ArithmetikController controller = new ArithmetikController();
        String result = controller.handleArithmeticException(new ArithmeticException("custom error"));
        assertEquals("custom error", result);
    }

    @Test
    void nonNumericDividendReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/arithmetik/divide")
                        .param("dividend", "abc")
                        .param("divisor", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingDivisorReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/arithmetik/divide")
                        .param("dividend", "10"))
                .andExpect(status().isBadRequest());
    }
}
