package org.example.arithmetik;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LessonOneTest {

    private static final float DELTA = 1e-4f;

    @Test
    void dividesTwoPositiveNumbers() {
        assertEquals(5f, LessonOne.divide(10f, 2f), DELTA);
    }

    @Test
    void dividesWithNonIntegerResult() {
        assertEquals(5.2f, LessonOne.divide(10.4f, 2f), DELTA);
    }

    @Test
    void dividesTwoNegativeNumbers() {
        assertEquals(5f, LessonOne.divide(-10f, -2f), DELTA);
    }

    @Test
    void dividesPositiveByNegative() {
        assertEquals(-5f, LessonOne.divide(10f, -2f), DELTA);
    }

    @Test
    void dividesNegativeByPositive() {
        assertEquals(-5f, LessonOne.divide(-10f, 2f), DELTA);
    }

    @Test
    void dividingZeroByAnyNumberReturnsZero() {
        assertEquals(0f, LessonOne.divide(0f, 7f), DELTA);
    }

    @Test
    void dividingByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> LessonOne.divide(10f, 0f));
    }
}
