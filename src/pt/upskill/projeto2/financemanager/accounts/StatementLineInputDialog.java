package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.date.Date;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class StatementLineInputDialog extends JDialog {
    private JTextField dateField;
    private JTextField valueDateField;
    private JTextField descriptionField;
    private JTextField draftField;
    private JTextField creditField;
    private JTextField accountingBalanceField;
    private JTextField availableBalanceField;
    private boolean confirmed;
    private boolean requestBalances;

    public StatementLineInputDialog(Frame owner, boolean requestBalances) {
        super(owner, "Adicionar movimento", true);
        this.requestBalances = requestBalances;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Inserir novo movimento");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(requestBalances ? 7 : 5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Data (DD-MM-YYYY):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Data Valor (DD-MM-YYYY):"));
        valueDateField = new JTextField();
        inputPanel.add(valueDateField);

        inputPanel.add(new JLabel("Descrição:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Débito:"));
        draftField = new JTextField();
        inputPanel.add(draftField);

        inputPanel.add(new JLabel("Crédito:"));
        creditField = new JTextField();
        inputPanel.add(creditField);

        if (requestBalances) {
            inputPanel.add(new JLabel("Saldo contabilístico:"));
            accountingBalanceField = new JTextField();
            inputPanel.add(accountingBalanceField);

            inputPanel.add(new JLabel("Saldo disponível:"));
            availableBalanceField = new JTextField();
            inputPanel.add(availableBalanceField);
        }

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public StatementLine getStatementLine() {
        String[] dateParts = dateField.getText().split("-");
        Date date = new Date(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));

        String[] valueDateParts = valueDateField.getText().split("-");
        Date valueDate = new Date(Integer.parseInt(valueDateParts[0]), Integer.parseInt(valueDateParts[1]), Integer.parseInt(valueDateParts[2]));

        String description = descriptionField.getText();
        double draft = Double.parseDouble(draftField.getText());
        double credit = Double.parseDouble(creditField.getText());
        double accountingBalance = requestBalances ? Double.parseDouble(accountingBalanceField.getText()) : 0.0;
        double availableBalance = requestBalances ? Double.parseDouble(availableBalanceField.getText()) : 0.0;

        return new StatementLine(date, valueDate, description, draft, credit, accountingBalance, availableBalance, null);
    }
}
