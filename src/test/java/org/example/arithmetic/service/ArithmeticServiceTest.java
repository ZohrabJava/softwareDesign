package org.example.arithmetic.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArithmeticServiceTest {

    private static final float DELTA = 1e-4f;

    private final ArithmeticService arithmeticService = new ArithmeticService();

    @Test
    void dividesTwoPositiveNumbers() {
        assertEquals(5f, arithmeticService.divide(10f, 2f), DELTA);
    }

    @Test
    void dividesWithNonIntegerResult() {
        assertEquals(5.2f, arithmeticService.divide(10.4f, 2f), DELTA);
    }

    @Test
    void dividesTwoNegativeNumbers() {
        assertEquals(5f, arithmeticService.divide(-10f, -2f), DELTA);
    }

    @Test
    void dividesPositiveByNegative() {
        assertEquals(-5f, arithmeticService.divide(10f, -2f), DELTA);
    }

    @Test
    void dividesNegativeByPositive() {
        assertEquals(-5f, arithmeticService.divide(-10f, 2f), DELTA);
    }

    @Test
    void dividingZeroByAnyNumberReturnsZero() {
        assertEquals(0f, arithmeticService.divide(0f, 7f), DELTA);
    }

    @Test
    void dividingByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> arithmeticService.divide(10f, 0f));
    }

    @Test
    void dividingByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> arithmeticService.divide(10f, -0.0f));
    }

    @Test
    void dividingNegativeZeroByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> arithmeticService.divide(-0.0f, -0.0f));
    }

    @Test
    void dividingNegativeZeroDividendReturnsPositiveZero() {
        float result = arithmeticService.divide(-0.0f, 5f);
        assertEquals(0f, result, DELTA);
        assertEquals(0, Float.floatToRawIntBits(result), "expected +0.0 bit pattern, got -0.0");
    }

    @Test
    void dividingSameValueByItselfReturnsOne() {
        assertEquals(1f, arithmeticService.divide(Float.MAX_VALUE, Float.MAX_VALUE), DELTA);
        assertEquals(1f, arithmeticService.divide(-7f, -7f), DELTA);
    }

    @Test
    void dividesMaxValueByOne() {
        assertEquals(Float.MAX_VALUE, arithmeticService.divide(Float.MAX_VALUE, 1f), DELTA);
    }

    @Test
    void dividesSmallestSubnormalByTwoRoundsToZero() {
        assertEquals(0f, arithmeticService.divide(Float.MIN_VALUE, 2f), DELTA);
    }

    @Test
    void dividesSubnormalValueExactly() {
        assertEquals(Float.MIN_VALUE, arithmeticService.divide(Float.MIN_VALUE * 2, 2f), DELTA);
    }
}
