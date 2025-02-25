package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.date.Month;
import pt.upskill.projeto2.financemanager.filters.BeforeDateSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Account {

    private long id;
    private String name;
    private String currency;
    private Date startDate;
    private Date endDate;
    private double interestRate;
    protected double currentBalance = 0.0;
    private BanksConstants banksConstants = new BanksConstants();
    private List <StatementLine> statementLines;


    public Account(long id, String name) {
        this.id = id;
        this.name = name;
        this.statementLines = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public List<StatementLine> getStatementLines() {
        return statementLines;
    }

    public void addAccountInfo(AccountInfo accountInfo) {
        if(accountInfo.getCurrency()!= null){
            this.currency = accountInfo.getCurrency();
        }
        if(accountInfo.getName()!= null){
            this.name = accountInfo.getName();
        }
        if(accountInfo.getType()!= null){
            this.getClass().getSimpleName();
        }
        if(accountInfo.getAdditionalInfo()!= null){
            additionalInfo();
        }
    }

    public abstract String additionalInfo();

    public double currentBalance() {
        StatementLine latestStatementLine = null;

        for (StatementLine statementLine : statementLines) {
            if (latestStatementLine == null || statementLine.getDate().after(latestStatementLine.getDate())) {
                latestStatementLine = statementLine;
            }
        }

        if (latestStatementLine != null) {
            currentBalance = latestStatementLine.getAccountingBalance();
        }

        return currentBalance;
    }

    public abstract double estimatedAverageBalance();

    public BanksConstants getBanksConstants() {
        return banksConstants;
    }

    public abstract double getInterestRate();

    public abstract void setInterestRate(double r);

    public abstract double estimatedAnnualInterest();

    public static Account newAccount(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Account account = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("Account Info")) {
                continue;
            } else if (line.startsWith("Account") && account == null) {
                String[] split = line.split(";");
                if (split.length >= 5) {
                    long id = Long.parseLong(split[1].trim());
                    String currency = split[2].trim();
                    String name = split[3].trim();
                    String type = split[4].trim();
                    String additionalInfo = split.length > 5 ? split[5].trim() : "";
                    AccountInfo accountInfo = new AccountInfo(id, currency, name, type, additionalInfo);
                    if (type.equals("DraftAccount")) {
                        account = new DraftAccount(id, name);
                    } else {
                        account = new SavingsAccount(id, name);
                    }
                    account.addAccountInfo(accountInfo);
                }
            } else if (line.startsWith("Start Date")) {
                String[] split = line.split(";");
                String[] dateParts = split[1].trim().split("-");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                assert account != null;
                account.startDate = new Date(day, month, year);
            } else if (line.startsWith("End Date")) {
                String[] split = line.split(";");
                String[] dateParts = split[1].trim().split("-");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                assert account != null;
                account.endDate = new Date(day, month, year);
            } else if (line.startsWith("Date")) {
                continue;
            } else {
                StatementLine statementLine = StatementLine.newStatement(line);
                if (account != null) {
                    account.getStatementLines().add(statementLine);
                }
            }
        }
        scanner.close();
        return account;
    }


    public void addStatementLine(StatementLine statementLine) {
        if (statementLines.isEmpty()) {
            setStartDate(statementLine.getDate());
        }

        if (getEndDate() == null || statementLine.getDate().after(getEndDate())) {
            setEndDate(statementLine.getDate());
        }
        statementLines.add(statementLine);
        updateCurrentBalance(statementLine);
    }

    private void updateCurrentBalance(StatementLine statementLine) {
        currentBalance = statementLine.getAvailableBalance();
    }

    public void removeStatementLinesBefore(Date date) {
        BeforeDateSelector selector = new BeforeDateSelector(date);
        statementLines.removeIf(selector::isSelected);
    }

    public double totalDraftsForCategorySince(Category category, Date sinceDate) {
        if (statementLines == null) {
            return 0.0;
        }

        double total = 0.0;
        for (StatementLine line : statementLines) {
            if (line != null && line.getDate().after(sinceDate) && line.getCategory() != null && line.getCategory().equals(category)) {
                total += line.getDraft();
            }
        }
        return total;
    }

    public double totalForMonth(int month, int year) {
        double total = 0.0;
        Month month1 = Date.intToMonth(month);
        for (StatementLine line : statementLines) {
            if (line.getDate().getMonth() == month1 && line.getDate().getYear() == year) {
                total += line.getDraft();
            }
        }
        return total;
    }

    public void autoCategorizeStatements(List<Category> categories) {
        for (StatementLine line : statementLines) {
            if (line.getCategory() == null) {
                for (Category category : categories) {
                    if (category.hasTag(line.getDescription())) {
                        line.setCategory(category);
                    }
                }
            }
        }
    }
}
