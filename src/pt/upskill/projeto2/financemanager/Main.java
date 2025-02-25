package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.DraftAccount;
import pt.upskill.projeto2.financemanager.accounts.SavingsAccount;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.gui.PersonalFinanceManagerUserInterface;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        PersonalFinanceManager personalFinanceManager = new PersonalFinanceManager("./account_info");

        PersonalFinanceManagerUserInterface gui = new PersonalFinanceManagerUserInterface(
                personalFinanceManager);
        gui.execute();

    }

}
