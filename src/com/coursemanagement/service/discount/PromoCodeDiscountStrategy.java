package com.coursemanagement.service.discount;

import java.math.BigDecimal;

public class PromoCodeDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        return price.multiply(new BigDecimal("0.15"));
    }
}