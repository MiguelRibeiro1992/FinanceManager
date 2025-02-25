// src/pt/upskill/projeto2/financemanager/gui/CategoryInputDialog.java

package pt.upskill.projeto2.financemanager.categories;

import pt.upskill.projeto2.financemanager.categories.Category;

import javax.swing.*;
import java.awt.*;

public class CategoryInputDialog extends JDialog {
    private JTextField categoryNameField;
    private JTextField tagField;
    private boolean confirmed;

    public CategoryInputDialog(Frame owner, String description) {
        super(owner, "Insira uma Categoria e um Tag", true);
        setLayout(new BorderLayout());

        JLabel descriptionLabel = new JLabel("Descrição do movimento: " + description);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(descriptionLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel categoryNameLabel = new JLabel("Nome da Categoria:");
        categoryNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(categoryNameLabel);

        categoryNameField = new JTextField();
        inputPanel.add(categoryNameField);

        JLabel tagLabel = new JLabel("Tag:");
        tagLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(tagLabel);

        tagField = new JTextField();
        inputPanel.add(tagField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCategoryName() {
        return categoryNameField.getText();
    }

    public String getTag() {
        return tagField.getText();
    }
}
