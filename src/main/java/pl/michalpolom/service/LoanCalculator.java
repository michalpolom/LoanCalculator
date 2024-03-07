package pl.michalpolom.service;

import lombok.NoArgsConstructor;
import pl.michalpolom.entity.LoanInput;
import pl.michalpolom.entity.LoanOffer;
import pl.michalpolom.entity.LoanPeriod;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class LoanCalculator {

    private static final Integer UPPER_LIMIT_LOAN_PERIOD_IN_MONTHS = 100;
    private static final BigDecimal MAXIMUM_LOAN_AMOUNT_IN_PLN = BigDecimal.valueOf(150_000);
    private static final BigDecimal MINIMAL_LOAN_AMOUNT_IN_PLN = BigDecimal.valueOf(5_000);
    private static final BigDecimal MAXIMUM_COMMITMENT_AMOUNT_IN_PLN = BigDecimal.valueOf(200_000);


    public List<LoanOffer> calculate(LoanInput input) {

        Integer maximumLoanPeriod = maximumLoanPeriod(input.employmentPeriodInMonths());

        List<LoanPeriod> loanPeriods = Arrays.stream(LoanPeriod.values()).filter(period -> period.getFrom() < maximumLoanPeriod).toList();

        List<LoanOffer> results = new ArrayList<>();
        for (LoanPeriod loanPeriod : loanPeriods) {
            BigDecimal maximumMonthlyInstallment = maximumMonthlyInstallment(
                    loanPeriod,
                    input.monthlyIncomeInPln(),
                    input.monthlyLivingCostsInPln(),
                    input.monthlyTotalLoanLiabilitiesInPln()
                ).round(new MathContext(1));
            BigDecimal maximumLoanAmount = maximumLoanAmount(
                    loanPeriod,
                    input.totalInstallmentLoanBalancesInPln(),
                    maximumMonthlyInstallment
                ).round(new MathContext(1));
            results.add(new LoanOffer(loanPeriod.getTo(), maximumMonthlyInstallment, maximumLoanAmount));
        }

        results = results.stream().filter(this::checkOffer).toList();

        return results;
    }

    private boolean checkOffer(LoanOffer offer){
        return offer.maximumLoanAmountInPln().compareTo(MINIMAL_LOAN_AMOUNT_IN_PLN) >= 0;
    }


    private Integer maximumLoanPeriod(Integer employmentPeriodInMonths) {
        if (employmentPeriodInMonths < UPPER_LIMIT_LOAN_PERIOD_IN_MONTHS) {
            return employmentPeriodInMonths;
        }
        return UPPER_LIMIT_LOAN_PERIOD_IN_MONTHS;
    }

    private BigDecimal maximumMonthlyInstallment(LoanPeriod loanPeriod, BigDecimal monthlyIncomeInPln, BigDecimal monthlyLivingCostsInPln, BigDecimal monthlyTotalLoanLiabilitiesInPln) {
        BigDecimal v1 = monthlyIncomeInPln.subtract(monthlyLivingCostsInPln).subtract(monthlyTotalLoanLiabilitiesInPln);
        BigDecimal v2 = loanPeriod.getDTI().multiply(monthlyIncomeInPln).subtract(monthlyTotalLoanLiabilitiesInPln);
        return v1.min(v2);
    }

    private BigDecimal MI(LoanPeriod loanPeriod) {
        return loanPeriod.getInterestRatePerAnnum().divide(BigDecimal.valueOf(12), MathContext.DECIMAL32);
    }

    private BigDecimal maximumLoanAmount(LoanPeriod loanPeriod, BigDecimal totalInstallmentLoanBalancesInPln, BigDecimal maximumMonthlyInstallment) {
        BigDecimal v1 = MAXIMUM_COMMITMENT_AMOUNT_IN_PLN.subtract(totalInstallmentLoanBalancesInPln);
        BigDecimal v2 = maximumMonthlyInstallment.multiply(
                BigDecimal.ONE.subtract(
                        BigDecimal.ONE.add(MI(loanPeriod)).pow(-loanPeriod.getTo(), MathContext.DECIMAL32)
                ).divide(MI(loanPeriod), RoundingMode.valueOf(2))
        );

        return MAXIMUM_LOAN_AMOUNT_IN_PLN.min(v1).min(v2);
    }
}
