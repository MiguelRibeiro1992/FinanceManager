package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StatementLine {

	private Date date;
	private Date valueDate;
	private String description;
	private double draft;
	private double credit;
	private double accountingBalance;
	private double availableBalance;
	private Category category;

	public StatementLine(Date date, Date valueDate, String description, double draft, double credit, double accountingBalance, double availableBalance, Category category) {
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("Description cannot be null or empty");
		}
		if (draft > 0) {
			throw new IllegalArgumentException("Draft must be zero or negative");
		}
		if (credit < 0) {
			throw new IllegalArgumentException("Credit must be zero or positive");
		}
		this.date = date;
		this.valueDate = valueDate;
		this.description = description;
		this.draft = draft;
		this.credit = credit;
		this.accountingBalance = accountingBalance;
		this.availableBalance = availableBalance;
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public String getDescription() {
		return description;
	}

	public double getCredit() {
		return credit;
	}

	public double getDraft() {
		return draft;
	}

	public double getAccountingBalance() {
		return accountingBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public Category getCategory() {
		return category;
	}

	public void setDraft(double draft) {
		this.draft = draft;
	}

	public void setAccountingBalance(double accountingBalance) {
		this.accountingBalance = accountingBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public void setCategory(Category cat) {
		this.category = cat;
		
	}

	public static StatementLine newStatement(String sst) {
		String[] split = sst.split(";");
		if (split.length < 7) {
			throw new IllegalArgumentException("Invalid statement line format: " + sst);
		}

		String[] dateParts = split[0].trim().split("-");
		if (dateParts.length < 3) {
			throw new IllegalArgumentException("Invalid date format: " + split[0]);
		}
		int day = Integer.parseInt(dateParts[0]);
		int month = Integer.parseInt(dateParts[1]);
		int year = Integer.parseInt(dateParts[2]);
		Date date = new Date(day, month, year);

		String[] valueDateParts = split[1].trim().split("-");
		if (valueDateParts.length < 3) {
			throw new IllegalArgumentException("Invalid value date format: " + split[1]);
		}
		int valueDay = Integer.parseInt(valueDateParts[0]);
		int valueMonth = Integer.parseInt(valueDateParts[1]);
		int valueYear = Integer.parseInt(valueDateParts[2]);
		Date valueDate = new Date(valueDay, valueMonth, valueYear);

		String description = split[2].trim();
		double draft = Double.parseDouble(split[3].trim());
		double credit = Double.parseDouble(split[4].trim());
		double accountingBalance = Double.parseDouble(split[5].trim());
		double availableBalance = Double.parseDouble(split[6].trim());

		return new StatementLine(date, valueDate, description, draft, credit, accountingBalance, availableBalance, null);
	}

	@Override
	public String toString() {
		return "Movimento: " +
				"Data - " + date +
				" | Data Valor - " + valueDate +
				" | Descrição - " + description + '\'' +
				" | Débito - " + draft +
				" | Crédito - " + credit +
				" | Saldo contabilístico - " + accountingBalance +
				" | Saldo disponível - " + availableBalance +
				" | Categoria - " + category;
	}
}
