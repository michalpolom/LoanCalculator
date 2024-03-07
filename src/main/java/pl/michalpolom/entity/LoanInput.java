package pl.michalpolom.entity;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record LoanInput(
        Integer employmentPeriodInMonths,
        BigDecimal monthlyIncomeInPln,
        BigDecimal monthlyLivingCostsInPln,
        BigDecimal monthlyTotalLoanLiabilitiesInPln,
        BigDecimal totalInstallmentLoanBalancesInPln) {
}
