package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;


public class FileAccountFormat implements Format <Account> {

    @Override
    public String format(Account objectToFormat) {
        if (objectToFormat == null) {
            throw new IllegalArgumentException("Account object cannot be null");
        }

        String nl = System.getProperty("line.separator");
        StringBuilder formattedString = new StringBuilder();
        formattedString.append(String.format("Account Info - %s%s", new Date().toString(), nl));
        formattedString.append(String.format("Account  ;%d ; %s  ;%s ;%s ;%s",
                objectToFormat.getId(),
                objectToFormat.getCurrency() != null ? objectToFormat.getCurrency() : "N/A",
                objectToFormat.getName() != null ? objectToFormat.getName() : "N/A",
                objectToFormat.getClass().getSimpleName(),
                nl));
        formattedString.append(String.format("Start Date ;%s%s",
                objectToFormat.getStartDate() != null ? objectToFormat.getStartDate().toString() : "N/A",
                nl));
        formattedString.append(String.format("End Date ;%s%s",
                objectToFormat.getEndDate() != null ? objectToFormat.getEndDate().toString() : "N/A",
                nl));
        formattedString.append("Date ;Value Date ;Description ;Draft ;Credit ;Accounting balance ;Available balance" + nl);

        for (StatementLine line : objectToFormat.getStatementLines()) {
            formattedString.append(String.format("%s ;%s ;%s ;%.1f ;%.1f ;%.1f ;%.1f%s",
                    line.getDate() != null ? line.getDate().toString() : "N/A",
                    line.getValueDate() != null ? line.getValueDate().toString() : "N/A",
                    line.getDescription() != null ? line.getDescription() : "N/A",
                    line.getDraft(),
                    line.getCredit(),
                    line.getAccountingBalance(),
                    line.getAvailableBalance(),
                    nl).replace(',', '.'));
        }

        return formattedString.toString();
    }
}

