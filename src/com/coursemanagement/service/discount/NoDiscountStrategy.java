package com.coursemanagement.service.discount;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        return BigDecimal.ZERO;
    }
}