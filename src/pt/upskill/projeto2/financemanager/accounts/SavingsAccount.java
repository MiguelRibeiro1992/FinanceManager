package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class SavingsAccount extends Account{


    public static final Category savingsCategory = new Category("Savings");

    public SavingsAccount(long id, String name) {
        super(id, name);
    }

    @Override
    public String additionalInfo() {
        return "";
    }

    @Override
    public double estimatedAverageBalance() {
        return currentBalance();
    }

    @Override
    public double getInterestRate() {
        return getBanksConstants().savingsInterestRate();
    }

    @Override
    public void setInterestRate(double r) {
        getBanksConstants().setSavingsInterestRate(r);
    }

    @Override
    public void addStatementLine(StatementLine line) {
        if (line.getCategory() == null) {
            line.setCategory(savingsCategory);
        }
        super.addStatementLine(line);
    }

    @Override
    public double estimatedAnnualInterest() {
        return estimatedAverageBalance() * getInterestRate();
    }
}


