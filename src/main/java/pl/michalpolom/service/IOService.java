package pl.michalpolom.service;

import pl.michalpolom.entity.LoanInput;
import pl.michalpolom.entity.LoanOffer;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class IOService {
    public LoanInput prepareInput() {

        var input = LoanInput.builder();
        Scanner sc = new Scanner(System.in);

        System.out.println("KALKULATOR KREDYTOWY");
        System.out.println("Uzupełnij parametry poniżej.\n");


        System.out.print("Okres zatrudnienia (w miesiącach): ");
        input.employmentPeriodInMonths(askUntilCorrectInteger(sc));

        System.out.print("Miesięczny dochód (w PLN): ");
        input.monthlyIncomeInPln(askUntilCorrectBigDecimal(sc));

        System.out.print("Miesięczne koszty utrzymania (w PLN): ");
        input.monthlyLivingCostsInPln(askUntilCorrectBigDecimal(sc));

        System.out.print("Miesięczna suma zobowiązań kredytowych (w PLN): ");
        input.monthlyTotalLoanLiabilitiesInPln(askUntilCorrectBigDecimal(sc));

        System.out.print("Suma sald kredytów ratalnych (w PLN): ");
        input.totalInstallmentLoanBalancesInPln(askUntilCorrectBigDecimal(sc));

        return input.build();

    }

    public void prepareOutput(List<LoanOffer> output) {

        if (output.isEmpty()) {
            System.out.println("\nBrak dostępnych ofert.");
        } else {
            System.out.println("\nDostępne oferty:");
            for (int i = 0; i < output.size(); i++) {
                System.out.println(
                        String.format("""
                                        Oferta %s:
                                            Maksymalny okres kredytowania [miesiące]: %s
                                            Maksymalna miesięczna rata: %szł
                                            Maksymalna kwota kredytu: %szł
                                        """,
                                i + 1,
                                output.get(i).maximumLoanPeriodInMonth(),
                                output.get(i).maximumMonthlyInstallment().toPlainString(),
                                output.get(i).maximumLoanAmountInPln().toPlainString()
                        )
                );
            }
        }

    }

    private static int askUntilCorrectInteger(Scanner sc) {
        Integer result = null;
        while (result == null) {
            try {
                result = sc.nextInt();
                if (result < 0) {
                    result = null;
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException ignored) {
                sc.nextLine();
                System.out.print("    Podaj poprawną wartość: ");
            }
        }
        return result;
    }

    private static BigDecimal askUntilCorrectBigDecimal(Scanner sc) {
        BigDecimal result = null;
        while (result == null) {
            try {
                result = sc.nextBigDecimal();
                if (result.compareTo(BigDecimal.ZERO) < 0) {
                    result = null;
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException ignored) {
                sc.nextLine();
                System.out.print("    Podaj poprawną wartość: ");
            }
        }
        return result;
    }
}
