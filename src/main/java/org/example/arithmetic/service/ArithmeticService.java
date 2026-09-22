package org.example.arithmetic.service;

import org.springframework.stereotype.Service;

/**
 * Կատարում է երկու float թվերի բաժանում՝ առանց "/" օպերատորի, Math, Float և Long
 * class-երի մեթոդների կիրառման։
 * Բաժանման տրամաբանությունը աշխատում է 2-ական (binary) համակարգում.
 *  - նշանը (sign) հաշվարկվում է տրամաբանական XOR (^) օպերատորով
 *  - թիվը նորմալացվում է [1,2) միջակայքում՝ պարզ բազմապատկումով (*2 / *0.5),
 *    ինչը IEEE-754 մանտիսսա/աստիճանացույցի արդյունահանման ճշգրիտ նույնարժեքն է
 *  - մանտիսսաների փաստացի բաժանումը կատարվում է 2-ական երկարացված բաժանման
 *    (binary restoring division) եղանակով՝ բացառապես bitwise օպերատորներով (<<, >>>, &, |)
 */
@Service
public class ArithmeticService {

    public float divide(float dividend, float divisor) {
        if (divisor == 0f) {
            throw new ArithmeticException("Բաժանումն ապարիզ է");
        }
        if (dividend == 0f) {
            return 0f;
        }

        boolean negative = (dividend < 0) ^ (divisor < 0);
        float absA = dividend < 0 ? -dividend : dividend;
        float absB = divisor < 0 ? -divisor : divisor;

        // Նորմալացում [1,2) միջակայքում՝ ինչպես IEEE-754 մանտիսսան, հաշվելով 2-ի աստիճանացույցը
        int expA = 0;
        while (absA >= 2f) { absA *= 0.5f; expA++; }
        while (absA < 1f)  { absA *= 2f;   expA--; }

        int expB = 0;
        while (absB >= 2f) { absB *= 0.5f; expB++; }
        while (absB < 1f)  { absB *= 2f;   expB--; }

        // absA, absB ∈ [1,2). Վերածում ենք ամբողջ թվերի (fixed point, 23 բիթանոց ֆրակցիա)
        final int MANT_BITS = 23;
        long mantA = (long) (absA * (1 << MANT_BITS));
        long mantB = (long) (absB * (1 << MANT_BITS));

        // --- 2-ական երկարացված բաժանում (binary restoring division), միայն bitwise օպերատորներով ---
        final int EXTRA_BITS = 27;
        int totalBits = MANT_BITS + 1 + EXTRA_BITS;
        long numerator = mantA << EXTRA_BITS;

        long quotient = 0;
        long remainder = 0;
        for (int i = totalBits - 1; i >= 0; i--) {
            remainder = (remainder << 1) | ((numerator >>> i) & 1L);
            if (remainder >= mantB) {
                remainder -= mantB;
                quotient |= (1L << i);
            }
        }
        // quotient = floor((mantA/mantB) * 2^EXTRA_BITS), mantA/mantB ∈ (0.5, 2)

        int exponent = expA - expB;
        int shift = ((quotient & (1L << EXTRA_BITS)) != 0) ? 0 : -1;
        exponent += shift;

        int shiftDown = EXTRA_BITS + shift - MANT_BITS;
        long normalizedMant = quotient >>> shiftDown;
        long fracBits = normalizedMant & ((1L << MANT_BITS) - 1);

        // Կառուցում ենք [1,2) միջակայքի մանտիսսան՝ բիթերից, միայն "+"/"*" օպերատորներով
        float fractionValue = 0f;
        float placeValue = 1f;
        for (int b = MANT_BITS - 1; b >= 0; b--) {
            placeValue *= 0.5f;
            if (((fracBits >>> b) & 1L) != 0) {
                fractionValue += placeValue;
            }
        }
        float result = 1f + fractionValue;

        // Կիրառում ենք վերջնական աստիճանացույցը՝ կրկնվող *2 / *0.5-ով
        if (exponent > 0) {
            for (int e = 0; e < exponent; e++) result *= 2f;
        } else {
            for (int e = 0; e < -exponent; e++) result *= 0.5f;
        }

        return negative ? -result : result;
    }
}
