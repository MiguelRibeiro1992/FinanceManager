package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.*;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.utils.Menu;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PersonalFinanceManager {
    private List<Account> accounts;
    private List <Category> categories;
    private Map<String, Category> descriptionToCategoryMap;



    public PersonalFinanceManager(String pathname) throws FileNotFoundException {
        this.accounts = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.descriptionToCategoryMap = new HashMap<>();
        scanFolderForAccounts(pathname);
        Category.processStatementLines(accounts, categories, descriptionToCategoryMap);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    private void scanFolderForAccounts(String pathname) throws FileNotFoundException {
        File folder = new File(pathname);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (files != null) {
            for (File file : files) {
                Account account = Account.newAccount(file);
                if (account != null) {
                    addAccount(account);
                }
            }
        }
    }

    public void createAccountFromUserInput() {
        String[] accountTypes = {"DraftAccount", "SavingsAccount"};
        String accountType = (String) JOptionPane.showInputDialog(null, "Escolha o tipo de conta:", "Tipo de conta", JOptionPane.QUESTION_MESSAGE, null, accountTypes, accountTypes[0]);

        if (accountType == null) {
            return;
        }

        String idStr = JOptionPane.showInputDialog("Introduza um ID de conta:");
        if (idStr == null || idStr.isEmpty()) {
            return; // User canceled the input
        }
        long id = Long.parseLong(idStr);

        String name = JOptionPane.showInputDialog("Introduza o nome da conta:");
        if (name == null || name.isEmpty()) {
            return; // User canceled the input
        }

        String currency = JOptionPane.showInputDialog("Introduza o tipo de moeda:");
        if (currency == null || currency.isEmpty()) {
            return; // User canceled the input
        }

        Account account;
        if (accountType.equals("DraftAccount")) {
            account = new DraftAccount(id, name);
        } else {
            account = new SavingsAccount(id, name);
        }
        account.setCurrency(currency);

        addAccount(account);
        JOptionPane.showMessageDialog(null, "Conta criada com sucesso", "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    public String globalPosition() {
        StringBuilder result = new StringBuilder("Posição Global\nNumero de Conta  Saldo\n");
        double totalBalance = 0.0;

        for (Account account : accounts) {
            double balance = account.currentBalance();
            result.append(account.getId()).append("   ").append(balance).append("\n");
            totalBalance += balance;
        }

        result.append("\nSaldo total: ").append(totalBalance);
        return result.toString();
    }

    public List<Category> listCategories() {
        for (Account account:accounts){
            List <StatementLine> statements = account.getStatementLines();
            for (StatementLine statement:statements){
                Category category = statement.getCategory();
                if (!categories.contains(category) && category != null){
                    categories.add(category);
                }
            }
        }
        return categories;
    }

    public Map<String, Category> getDescriptionToCategoryMap() {
        return descriptionToCategoryMap;
    }

    public List <StatementLine>  getStatements (){
        List <StatementLine> statements = new ArrayList<>();
        for (Account account : accounts) {
            statements.addAll(account.getStatementLines());
        }
        return statements;
    }

    public void monthlySummary() {
        Map<String, double[]> monthlyData = new HashMap<>();

        for (Account account : accounts) {
            List<StatementLine> statements = account.getStatementLines();
            for (StatementLine statement : statements) {
                String monthYear = statement.getDate().getMonth() + "-" + statement.getDate().getYear();
                double[] data = monthlyData.getOrDefault(monthYear, new double[3]);
                data[0] += statement.getDraft();
                data[1] += statement.getCredit();
                data[2] = statement.getAvailableBalance();
                monthlyData.put(monthYear, data);
            }
        }

        System.out.println("Resumo mensal: ");
        for (Map.Entry<String, double[]> entry : monthlyData.entrySet()) {
            String monthYear = entry.getKey();
            double[] data = entry.getValue();
            double totalDifference = data[1] + data[0];
            System.out.println("Mês/Ano: " + monthYear + " | Total de débitos: " + data[0] + " | Total de créditos: " + data[1] + " | Balanço: " + totalDifference + "| Balanço final: " + data[2]);
        }
    }

    public void predictionPerCategory() {
        Map<Category, Double> categorySpending = new HashMap<>();

        for (Category category : categories) {
            categorySpending.put(category, 0.0);
        }

        for (Account account : accounts) {
            List<StatementLine> statements = account.getStatementLines();
            for (StatementLine statement : statements) {
                Category category = statement.getCategory();
                if (category != null) {
                    double amount = statement.getDraft();
                    categorySpending.put(category, categorySpending.getOrDefault(category, 0.0) + amount);
                } else {
                    System.out.println("Movimento: " + statement.getDescription() + " não tem categoria atribuida.");
                }
            }
        }

        System.out.println("Total gasto por categoria: ");
        for (Map.Entry<Category, Double> entry : categorySpending.entrySet()) {
            Category category = entry.getKey();
            double totalSpent = entry.getValue();
            System.out.println("Categoria " + category.getName() + " | Total gasto: " + totalSpent);
        }
    }

    public void anualInterest() {
        double totalDraftAccountInterest = 0.0;
        double totalSavingsAccountInterest = 0.0;

        for (Account account : accounts) {
            if (account instanceof DraftAccount) {
                double estimatedAverageBalance = account.estimatedAverageBalance();
                totalDraftAccountInterest += estimatedAverageBalance;
                System.out.println("Conta à ordem: " + account.getId());
                System.out.println("Saldo médio estimado: " + estimatedAverageBalance);
            } else {
                double estimatedAnnualInterest = account.estimatedAnnualInterest();
                totalSavingsAccountInterest += estimatedAnnualInterest;
                System.out.println("Conta poupança: " + account.getId());
                System.out.println("Juros anuais estimados: " + estimatedAnnualInterest);
            }
        }

        System.out.println("Total de juros estimados para contas à ordem: " + totalDraftAccountInterest);
        System.out.println("Total de juros estimados para contas poupança: " + totalSavingsAccountInterest);
    }
}
