/*
 * Copyright (c) 2016 Àlex Magaz Graça <rivaldi8@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gnucash.android.test.unit.model;

import org.gnucash.android.model.Price;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class PriceTest {
    @Test
    public void creatingFromExchangeRate_ShouldGetPrecisionRight() {
        Locale.setDefault(Locale.US);

        String exchangeRateString = "0.123456";
        BigDecimal exchangeRate = new BigDecimal(exchangeRateString);
        Price price = new Price("commodity1UID", "commodity2UID", exchangeRate);
        assertThat(price.toString()).isEqualTo(exchangeRateString);

        // ensure we don't get more decimal places than needed (0.123000)
        exchangeRateString = "0.123";
        exchangeRate = new BigDecimal(exchangeRateString);
        price = new Price("commodity1UID", "commodity2UID", exchangeRate);
        assertThat(price.toString()).isEqualTo(exchangeRateString);
    }

    @Test
    public void toString_shouldUseDefaultLocale() {
        Locale.setDefault(Locale.GERMANY);

        String exchangeRateString = "1.234";
        BigDecimal exchangeRate = new BigDecimal(exchangeRateString);
        Price price = new Price("commodity1UID", "commodity2UID", exchangeRate);
        assertThat(price.toString()).isEqualTo("1,234");
    }

    /**
     * BigDecimal throws an ArithmeticException if it can't represent exactly
     * a result. This can happen with divisions like 1/3 if no precision and
     * round mode is specified with a MathContext.
     */
    @Test
    public void toString_shouldNotFailForInfinitelyLongDecimalExpansion() {
        long numerator = 1;
        long denominator = 3;
        Price price = new Price();

        price.setValueNum(numerator);
        price.setValueDenom(denominator);
        try {
            price.toString();
        } catch (ArithmeticException e) {
            fail("The numerator/denominator division in Price.toString() should not fail.");
        }
    }

    @Test
    public void getNumerator_shouldReduceAutomatically() {
        long numerator = 1;
        long denominator = 3;
        Price price = new Price();

        price.setValueNum(numerator * 2);
        price.setValueDenom(denominator * 2);
        assertThat(price.getValueNum()).isEqualTo(numerator);
    }

    @Test
    public void getDenominator_shouldReduceAutomatically() {
        long numerator = 1;
        long denominator = 3;
        Price price = new Price();

        price.setValueNum(numerator * 2);
        price.setValueDenom(denominator * 2);
        assertThat(price.getValueDenom()).isEqualTo(denominator);
    }
}
