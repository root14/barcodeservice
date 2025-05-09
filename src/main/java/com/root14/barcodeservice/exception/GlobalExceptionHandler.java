package com.root14.barcodeservice.exception;

import com.google.zxing.FormatException;
import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles exceptions of type {@link IllegalArgumentException} and {@link FormatException}.
     * This method will return appropriate HTTP responses for these specific exceptions.
     * <p>
     * If the cause of the {@link IllegalArgumentException} is a {@link FormatException},
     * the response will indicate an invalid barcode format.
     * Otherwise, the response will return a generic error message for invalid arguments.
     *
     * @param exception the exception that was thrown
     * @return a {@link ResponseEntity} containing a map with the error details
     * and the corresponding HTTP status code:
     * - 422 Unprocessable Entity for invalid barcode format
     * - 400 Bad Request for general invalid arguments
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, FormatException.class})
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        //com.google.zxing.FormatException wrapping with com.google.zxing.IllegalArgumentException
        if (exception.getCause() instanceof FormatException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("success", false, "message", "Data is not valid for barcode.", "errorCode", "INVALID_BARCODE_FORMAT"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", exception.getMessage(), "errorCode", "INVALID_ARGUMENT"));
    }

}
