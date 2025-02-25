package pt.upskill.projeto2.financemanager.categories;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.DraftAccount;
import pt.upskill.projeto2.financemanager.accounts.SavingsAccount;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.utils.Menu;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Category implements Serializable {

    private String name;
    private List<String> tags;
    private static final long serialVersionUID = -9107819223195202547L;

    public Category(String name) {
        this.name = name;
        this.tags = new ArrayList<>();
    }

    /**
     * Função que lê o ficheiro categories e gera uma lista de {@link Category} (método fábrica)
     * Deve ser utilizada a desserialização de objetos para ler o ficheiro binário categories.
     *
     * @param file - Ficheiro onde estão apontadas as categorias possíveis iniciais, numa lista serializada (por defeito: /account_info/categories)
     * @return uma lista de categorias, geradas ao ler o ficheiro
     */

    public static List<Category> readCategories(File file) {
        List<Category> categories = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                for (Object item : list) {
                    if (item instanceof Category) {
                        categories.add((Category) item);
                    } else {
                        System.out.println("Encountered an object that is not a Category: " + item.getClass().getName());
                    }
                }
            } else {
                System.out.println("Encountered an object that is not a List: " + obj.getClass().getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Encountered an error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Encountered an error converting object: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Função que grava no ficheiro categories (por defeito: /account_info/categories) a lista de {@link Category} passada como segundo argumento
     * Deve ser utilizada a serialização dos objetos para gravar o ficheiro binário categories.
     * @param file
     * @param categories
     */

    public static void writeCategories(File file, List<Category> categories) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(categories);
            out.close();
            fileOut.close();
        } catch (IOException e){
            System.out.println("Encountered an error writing file");
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        if (tags.contains(tag)) {
            return true;
        }
        return false;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public String getName() {
        return name;
    }

    public boolean matches(String description) {
        for (String tag : tags) {
            if (description.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public static void processStatementLines(List<Account> accounts, List<Category> categories, Map<String, Category> descriptionToCategoryMap) {
        if (descriptionToCategoryMap != null) {
            descriptionToCategoryMap.clear();
        }

        for (Account account : accounts) {
            List<StatementLine> statements = account.getStatementLines();
            for (StatementLine statement : statements) {
                String description = statement.getDescription();
                if (description != null && !description.isEmpty()) {
                    Category category = null;
                    if (account instanceof SavingsAccount) {
                        category = new Category("POUPANÇA");
                        category.addTag("POUP");
                    } else if (account instanceof DraftAccount) {
                        category = descriptionToCategoryMap.get(description);
                        if (category == null) {
                            CategoryInputDialog dialog = new CategoryInputDialog(null, description);
                            dialog.setVisible(true);
                            if (dialog.isConfirmed()) {
                                String newCategoryName = dialog.getCategoryName();
                                String newTag = dialog.getTag();
                                category = new Category(newCategoryName);
                                category.addTag(newTag);
                                descriptionToCategoryMap.put(description, category);
                                categories.add(category);
                            } else {
                                return;
                            }
                        }
                    }
                    statement.setCategory(category);
                }
            }
        }

        writeCategories(new File("account_info/categories"), categories);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " | Tag: " + tags;
    }
}
