package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;

public class LongStatementFormat implements StatementLineFormat {

    @Override
    public String fields() {
        return "Date \tValue Date \tDescription \tDraft \tCredit \tAccounting balance \tAvailable balance ";
    }


    @Override
    public String format(StatementLine objectToFormat) {
        return String.format("%s \t%s \t%s \t%s \t%s \t%s \t%s",
                objectToFormat.getDate().toString(),
                objectToFormat.getValueDate().toString(),
                objectToFormat.getDescription(),
                objectToFormat.getDraft(),
                objectToFormat.getCredit(),
                objectToFormat.getAccountingBalance(),
                objectToFormat.getAvailableBalance());
    }
}
