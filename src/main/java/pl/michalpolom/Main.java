package pl.michalpolom;


import pl.michalpolom.entity.LoanInput;
import pl.michalpolom.entity.LoanOffer;
import pl.michalpolom.service.IOService;
import pl.michalpolom.service.LoanCalculator;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        LoanCalculator loanCalculator = new LoanCalculator();
        IOService ioService = new IOService();

        LoanInput input = ioService.prepareInput();
        List<LoanOffer> output = loanCalculator.calculate(input);
        ioService.prepareOutput(output);
    }
}