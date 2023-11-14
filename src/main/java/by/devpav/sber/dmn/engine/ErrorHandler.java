package by.devpav.sber.dmn.engine;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {App.class})
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        return new Error(e.getMessage());
    }

    public static class Error {

        public String reason;

        public Error(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

}
