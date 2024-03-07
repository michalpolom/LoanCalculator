package pl.michalpolom.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum LoanPeriod {

    PERIOD_1(6, 12, BigDecimal.valueOf(0.02), BigDecimal.valueOf(0.6)),
    PERIOD_2(13, 36, BigDecimal.valueOf(0.03), BigDecimal.valueOf(0.6)),
    PERIOD_3(37, 60, BigDecimal.valueOf(0.03), BigDecimal.valueOf(0.5)),
    PERIOD_4(61, 100,BigDecimal.valueOf(0.03) , BigDecimal.valueOf(0.55));

    private final int from;
    private final int to;
    private final BigDecimal interestRatePerAnnum;
    private final BigDecimal DTI;

    LoanPeriod(int from, int to, BigDecimal interestRatePerAnnum, BigDecimal DTI) {
        this.from = from;
        this.to = to;
        this.interestRatePerAnnum = interestRatePerAnnum;
        this.DTI = DTI;
    }

}
