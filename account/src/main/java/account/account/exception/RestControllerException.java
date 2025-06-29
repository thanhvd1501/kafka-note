package account.account.exception;

import account.account.dto.NotFoundDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestControllerException extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public final ResponseEntity<Object> handleBadRequestException(NotFoundException ex) {
    NotFoundDto notFoundDto = NotFoundDto.builder()
        .timestamp(new Date())
        .errorMessage(ex.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundDto);
  }

}
