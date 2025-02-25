package pt.upskill.projeto2.financemanager.accounts;

import org.junit.Before;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.filters.BeforeDateSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DraftAccount extends Account{

    public DraftAccount(long id, String name) {
        super(id, name);
    }

    @Override
    public String additionalInfo() {
        return "";
    }


    @Override
    public double estimatedAverageBalance() {
        double totalBalance = 0.0;
        int totalDays = 0;

        List<StatementLine> statementLines = getStatementLines();
        if (statementLines.isEmpty()) {
            return 0.0;
        }

        StatementLine lastStatement = null;
        for (StatementLine line : statementLines) {
            if (line.getDate().getYear() < statementLines.get(statementLines.size() - 1).getDate().getYear()) {
                if (lastStatement == null || line.getDate().after(lastStatement.getDate())) {
                    lastStatement = line;
                }
            }
        }

        if (lastStatement == null) {
            lastStatement = statementLines.get(0);
        }

        Date previousDate = lastStatement.getDate();
        double previousBalance = lastStatement.getAvailableBalance();
        Date endDate = statementLines.get(statementLines.size() - 1).getDate();

        for (StatementLine line : statementLines) {
            Date lineDate = line.getDate();
            if (!lineDate.after(endDate)) {
                int days = previousDate.diffInDays(lineDate);
                totalBalance += previousBalance * days;
                totalDays += days;
                previousDate = lineDate;
                previousBalance = line.getAvailableBalance();
            }
        }

        int remainingDays = previousDate.diffInDays(endDate);
        totalBalance += previousBalance * remainingDays;
        totalDays += remainingDays;

        return totalDays > 0 ? totalBalance / totalDays : 0.0;
    }

    @Override
    public double getInterestRate() {
        return getBanksConstants().normalInterestRate();
    }

    @Override
    public void setInterestRate(double r) {
        getBanksConstants().setNormalInterestRate(r);

    }

    @Override
    public double estimatedAnnualInterest() {
        return 0;
    }


}
