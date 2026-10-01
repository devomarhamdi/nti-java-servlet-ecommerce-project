package service;

public class DuplicateUserException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String field;

    public DuplicateUserException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
