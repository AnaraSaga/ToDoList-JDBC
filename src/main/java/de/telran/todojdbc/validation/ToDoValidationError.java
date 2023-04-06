package de.telran.todojdbc.validation;

import java.util.ArrayList;
import java.util.List;

public class ToDoValidationError {

    private List<String> errors = new ArrayList<>();

    private final String errorMsg;

    public ToDoValidationError(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void addValidationError(String error){
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
