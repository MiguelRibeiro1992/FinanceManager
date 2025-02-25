package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.filters.unittests.NoCategorySelectorTest;

public class NoCategorySelector implements Selector <StatementLine> {

    private String category;

    public NoCategorySelector(String category){
        this.category = category;
    }

    @Override
    public boolean isSelected(StatementLine item) {

        if(item.getCategory() == null){
            return true;
        }
        return false;
    }
}
