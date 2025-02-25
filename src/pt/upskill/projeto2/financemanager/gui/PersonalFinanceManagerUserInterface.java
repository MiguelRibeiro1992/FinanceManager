package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.PersonalFinanceManager;
import pt.upskill.projeto2.financemanager.accounts.*;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.utils.Menu;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class PersonalFinanceManagerUserInterface {

    public PersonalFinanceManagerUserInterface(
            PersonalFinanceManager personalFinanceManager) {
        this.personalFinanceManager = personalFinanceManager;
        setLookAndFeel();
    }

    private static final String OPT_GLOBAL_POSITION = "Posição Global";
    private static final String OPT_ACCOUNT_STATEMENT = "Movimentos Conta";
    private static final String OPT_LIST_CATEGORIES = "Listar categorias";
    private static final String OPT_ANALISE = "Análise";
    private static final String OPT_EXIT = "Sair";

    private static final String OPT_MONTHLY_SUMMARY = "Evolução global por mês";
    private static final String OPT_PREDICTION_PER_CATEGORY = "Previsão gastos totais do mês por categoria";
    private static final String OPT_ANUAL_INTEREST = "Previsão juros anuais";

    private static final String[] OPTIONS_ANALYSIS = {OPT_MONTHLY_SUMMARY, OPT_PREDICTION_PER_CATEGORY, OPT_ANUAL_INTEREST};
    private static final String[] OPTIONS = {OPT_GLOBAL_POSITION,
            OPT_ACCOUNT_STATEMENT, OPT_LIST_CATEGORIES, OPT_ANALISE, OPT_EXIT};

    private PersonalFinanceManager personalFinanceManager;

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {

        JFrame frame = new JFrame("Gestor de Finanças Pessoais");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gestor de Finanças Pessoais", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createAccountButton = new JButton("Criar Conta");
        createAccountButton.addActionListener(e -> personalFinanceManager.createAccountFromUserInput());
        centerPanel.add(createAccountButton);

        JButton globalPositionButton = new JButton("Posição Global");
        globalPositionButton.addActionListener(e -> showGlobalPosition());
        centerPanel.add(globalPositionButton);

        JButton accountStatementButton = new JButton("Movimentos de Conta");
        accountStatementButton.addActionListener(e -> getStatements());
        centerPanel.add(accountStatementButton);

        JButton newStatementButton = new JButton("Adicionar Movimentos");
        newStatementButton.addActionListener(e -> showNewStatementOptions());
        centerPanel.add(newStatementButton);

        JButton listCategoriesButton = new JButton("Listar Categorias");
        listCategoriesButton.addActionListener(e -> listCategories());
        centerPanel.add(listCategoriesButton);

        JButton analysisButton = new JButton("Análise");
        analysisButton.addActionListener(e -> showAnalysisOptions());
        centerPanel.add(analysisButton);

        JButton exitButton = new JButton("Sair");
        exitButton.addActionListener(e -> System.exit(0));
        centerPanel.add(exitButton);

        frame.add(centerPanel, BorderLayout.CENTER);

        JLabel statusBar = new JLabel("Estado: Pronto", JLabel.CENTER);
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void showGlobalPosition() {
        String globalPosition = personalFinanceManager.globalPosition();
        JOptionPane.showMessageDialog(null, globalPosition, "Posição Global", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listCategories() {
        StringBuilder categoriesList = new StringBuilder();
        personalFinanceManager.listCategories().forEach(category -> categoriesList.append(category).append("\n"));
        JOptionPane.showMessageDialog(null, categoriesList.toString(), "Categorias", JOptionPane.INFORMATION_MESSAGE);
    }

    private void getStatements() {
        StringBuilder statementsList = new StringBuilder();
        for (Account account : personalFinanceManager.getAccounts()) {
            if (account instanceof DraftAccount) {
                statementsList.append("Conta à ordem:\n");
            } else {
                statementsList.append("Conta poupança:\n");
            }
            for (StatementLine statement : account.getStatementLines()) {
                statementsList.append(statement).append("\n");
            }
            statementsList.append("\n");
        }
        JOptionPane.showMessageDialog(null, statementsList.toString(), "Movimentos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAnalysisOptions() {
        String option = Menu.requestSelection("Análise", new String[]{"Resumo Mensal", "Previsão por Categoria", "Taxa de Juro Anual"});
        switch (option) {
            case "Resumo Mensal":
                showMonthlySummary();
                break;
            case "Previsão por Categoria":
                showPredictionPerCategory();
                break;
            case "Taxa de Juro Anual":
                showAnnualInterest();
                break;
        }
    }

    private void showNewStatementOptions() {
        List<Account> accounts = personalFinanceManager.getAccounts();
        String[] accountNames = accounts.stream().map(Account::getName).toArray(String[]::new);

        String selectedAccountName = (String) JOptionPane.showInputDialog(null, "Escolha a conta:", "Adicionar Movimentos", JOptionPane.QUESTION_MESSAGE, null, accountNames, accountNames[0]);

        if (selectedAccountName != null) {
            Account selectedAccount = accounts.stream().filter(account -> account.getName().equals(selectedAccountName)).findFirst().orElse(null);

            if (selectedAccount != null) {
                boolean hasStatements = !selectedAccount.getStatementLines().isEmpty();
                StatementLineInputDialog dialog = new StatementLineInputDialog(null, !hasStatements);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    StatementLine newStatementLine = dialog.getStatementLine();

                    if (hasStatements) {
                        List<StatementLine> statements = selectedAccount.getStatementLines();
                        StatementLine lastStatement = statements.get(statements.size() - 1);

                        double lastAccountingBalance = lastStatement.getAccountingBalance();
                        double lastAvailableBalance = lastStatement.getAvailableBalance();

                        double newAccountingBalance = lastAccountingBalance + newStatementLine.getCredit() + newStatementLine.getDraft();
                        double newAvailableBalance = lastAvailableBalance + newStatementLine.getCredit() + newStatementLine.getDraft();

                        newStatementLine.setAccountingBalance(newAccountingBalance);
                        newStatementLine.setAvailableBalance(newAvailableBalance);
                    }

                    // Set the category based on the description
                    String description = newStatementLine.getDescription();
                    Category category = personalFinanceManager.getDescriptionToCategoryMap().get(description);
                    newStatementLine.setCategory(category);

                    if (selectedAccount instanceof SavingsAccount) {
                        Category savingsCategory = new Category("POUPANÇA");
                        savingsCategory.addTag("POUP");
                        newStatementLine.setCategory(savingsCategory);
                    }
                    selectedAccount.addStatementLine(newStatementLine);

                }
            }
        }
    }


    private void showMonthlySummary() {
        StringBuilder summary = new StringBuilder();
        Map<String, double[]> monthlyData = new HashMap<>();

        for (Account account : personalFinanceManager.getAccounts()) {
            List<StatementLine> statements = account.getStatementLines();
            for (StatementLine statement : statements) {
                String monthYear = statement.getDate().getMonth().ordinal() + "-" + statement.getDate().getYear();
                double[] data = monthlyData.getOrDefault(monthYear, new double[3]);
                data[0] += statement.getDraft();
                data[1] += statement.getCredit();
                data[2] = statement.getAvailableBalance();
                monthlyData.put(monthYear, data);
            }
        }

        summary.append("Resumo mensal:\n");
        monthlyData.entrySet().stream()
                .sorted((e1, e2) -> {
                    String[] parts1 = e1.getKey().split("-");
                    String[] parts2 = e2.getKey().split("-");
                    int yearComparison = Integer.compare(Integer.parseInt(parts1[1]), Integer.parseInt(parts2[1]));
                    if (yearComparison != 0) {
                        return yearComparison;
                    }
                    return Integer.compare(Integer.parseInt(parts1[0]), Integer.parseInt(parts2[0]));
                })
                .forEach(entry -> {
                    String monthYear = entry.getKey();
                    double[] data = entry.getValue();
                    double totalDifference = data[1] + data[0];
                    summary.append("Mês/Ano: ").append(monthYear)
                            .append(" | Total de débitos: ").append(data[0])
                            .append(" | Total de créditos: ").append(data[1])
                            .append(" | Balanço: ").append(totalDifference)
                            .append(" | Balanço final: ").append(data[2])
                            .append("\n");
                });

        JOptionPane.showMessageDialog(null, summary.toString(), "Resumo Mensal", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPredictionPerCategory() {
        StringBuilder prediction = new StringBuilder();
        Map<Category, Double> categorySpending = new HashMap<>();

        for (Category category : personalFinanceManager.listCategories()) {
            categorySpending.put(category, 0.0);
        }

        for (Account account : personalFinanceManager.getAccounts()) {
            List<StatementLine> statements = account.getStatementLines();
            for (StatementLine statement : statements) {
                if ("SUMMARY".equals(statement.getDescription())) {
                    continue;
                }
                Category category = statement.getCategory();
                if (category != null) {
                    double amount = statement.getDraft();
                    categorySpending.put(category, categorySpending.getOrDefault(category, 0.0) + amount);
                }
            }
        }

        prediction.append("Total gasto por categoria:\n");
        for (Map.Entry<Category, Double> entry : categorySpending.entrySet()) {
            Category category = entry.getKey();
            double totalSpent = entry.getValue();
            prediction.append("Categoria ").append(category.getName())
                    .append(" | Total gasto: ").append(totalSpent)
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, prediction.toString(), "Previsão por Categoria", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAnnualInterest() {
        StringBuilder interest = new StringBuilder();
        double totalDraftAccountInterest = 0.0;
        double totalSavingsAccountInterest = 0.0;

        for (Account account : personalFinanceManager.getAccounts()) {
            if (account instanceof DraftAccount) {
                double estimatedAverageBalance = account.estimatedAverageBalance();
                totalDraftAccountInterest += estimatedAverageBalance;
                interest.append("Conta à ordem: ").append(account.getId())
                        .append("\nSaldo médio estimado: ").append(estimatedAverageBalance)
                        .append("\n");
            } else {
                double estimatedAnnualInterest = account.estimatedAnnualInterest();
                totalSavingsAccountInterest += estimatedAnnualInterest;
                interest.append("Conta poupança: ").append(account.getId())
                        .append("\nJuros anuais estimados: ").append(estimatedAnnualInterest)
                        .append("\n");
            }
        }

        interest.append("Total de juros estimados para contas à ordem: ").append(totalDraftAccountInterest)
                .append("\nTotal de juros estimados para contas poupança: ").append(totalSavingsAccountInterest);

        JOptionPane.showMessageDialog(null, interest.toString(), "Taxa de Juro Anual", JOptionPane.INFORMATION_MESSAGE);
    }

}
