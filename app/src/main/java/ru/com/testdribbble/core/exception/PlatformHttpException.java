package ru.com.testdribbble.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.com.testdribbble.core.data.response.BaseRESTModel;

@AllArgsConstructor
@Getter
public class PlatformHttpException extends RuntimeException {

    private BaseRESTModel restModel;

}
