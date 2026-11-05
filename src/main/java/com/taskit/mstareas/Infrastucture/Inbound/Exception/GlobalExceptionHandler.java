package com.taskit.mstareas.Infrastucture.Inbound.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        String name = ex.getName();
        String type = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        Object value = ex.getValue();
        
        String message = String.format("El parámetro '%s' debe ser de tipo '%s'. Valor recibido: '%s'", 
                name, type, value);
        
        if (value != null && String.valueOf(value).contains("undefined")) {
            message += ". (Error frecuente del Frontend al enviar variables no inicializadas)";
        }

        error.put("error", "Parámetro inválido");
        error.put("message", message);
        error.put("parameter", name);
        error.put("receivedValue", String.valueOf(value));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
