package pt.upskill.projeto2.financemanager.exceptions;

public class UnknownAccountException extends Exception {
    /**
     *
     * @author upSkill 2020
     *
     * ...
     *
     */

    public UnknownAccountException() {
        super();
    }

    public UnknownAccountException(String message) {
        super(message);
    }
}
