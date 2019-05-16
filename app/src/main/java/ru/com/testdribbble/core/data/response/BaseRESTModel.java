package ru.com.testdribbble.core.data.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRESTModel implements Serializable {

    public String message;
    public List<Error> errors;

    @Override
    public String toString() {
        return "BaseRESTModel{" +
                "message='" + message + '\'' +
                '}';
    }
}
