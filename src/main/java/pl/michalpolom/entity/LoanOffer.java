package pl.michalpolom.entity;

import java.math.BigDecimal;

public record LoanOffer(
        Integer maximumLoanPeriodInMonth,
        BigDecimal maximumMonthlyInstallment,
        BigDecimal maximumLoanAmountInPln) {
}
