package com.coursemanagement.service.discount;

import com.coursemanagement.model.enums.DiscountType;

public class DiscountStrategyFactory {

    public static DiscountStrategy getStrategy(DiscountType type) {

        if (type == null) {
            return new NoDiscountStrategy();
        }

        return switch (type) {
            case NONE -> new NoDiscountStrategy();
            case STUDENT -> new StudentDiscountStrategy();
            case VIP -> new VipDiscountStrategy();
            case PROMO -> new PromoCodeDiscountStrategy();
        };
    }
}