package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;

public class SimpleStatementFormat implements StatementLineFormat {
    @Override
    public String fields() {
        return "Date \tDescription \tDraft \tCredit \tAvailable balance ";
    }

    @Override
    public String format(StatementLine objectToFormat) {
        return String.format ("%s \t%s \t%s \t%s \t%s",
                objectToFormat.getDate().toString(),
                objectToFormat.getDescription(),
                objectToFormat.getDraft(),
                objectToFormat.getCredit(),
                objectToFormat.getAvailableBalance());

    }
}
