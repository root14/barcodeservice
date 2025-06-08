package com.root14.barcodeservice.exception;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link IllegalArgumentException} and {@link FormatException}.
     * This method will return appropriate HTTP responses for these specific exceptions.
     * <p>
     * If the cause of the {@link IllegalArgumentException} is a {@link FormatException},
     * the response will indicate an invalid barcode format (422 Unprocessable Entity).
     * If {@link FormatException} is thrown directly or for general {@link IllegalArgumentException},
     * the response will return a generic error message for invalid arguments (400 Bad Request).
     *
     * @param exception the exception that was thrown
     * @return a {@link ResponseEntity} containing a map with the error details
     * and the corresponding HTTP status code.
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, FormatException.class})
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());

        if (exception.getCause() instanceof FormatException || exception.getCause() instanceof FormatException) {
            body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
            body.put("error", "Unprocessable Entity");
            body.put("message", "Data is not valid for barcode format.");
            body.put("errorCode", "INVALID_BARCODE_FORMAT");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
        } else {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Bad Request");
            body.put("message", exception.getMessage());
            body.put("errorCode", "INVALID_ARGUMENT");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    /**
     * Handles {@link NotFoundException} from ZXing.
     * Returns HTTP 404 Not Found.
     *
     * @param ex the NotFoundException that was thrown
     * @return a {@link ResponseEntity} with error details and 404 status.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", "Barcode not found in the provided data.");
        body.put("errorCode", "BARCODE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles {@link IOException}.
     * Returns HTTP 500 Internal Server Error.
     *
     * @param ex the IOException that was thrown
     * @return a {@link ResponseEntity} with error details and 500 status.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An I/O error occurred during processing.");
        body.put("errorCode", "IO_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Handles {@link WriterException} from ZXing.
     * Returns HTTP 400 Bad Request as it usually indicates an encoding issue with data.
     *
     * @param ex the WriterException that was thrown
     * @return a {@link ResponseEntity} with error details and 400 status.
     */
    @ExceptionHandler(WriterException.class)
    public ResponseEntity<Map<String, Object>> handleWriterException(WriterException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Error encoding barcode data: " + ex.getMessage());
        body.put("errorCode", "BARCODE_ENCODING_ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * A generic handler for any other unhandled {@link Exception}.
     * Returns HTTP 500 Internal Server Error.
     *
     * @param ex the Exception that was thrown
     * @return a {@link ResponseEntity} with error details and 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllOtherExceptions(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected server error occurred.");
        body.put("errorCode", "UNEXPECTED_ERROR");
        // for more detailed err msg
        // body.put("debugMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}