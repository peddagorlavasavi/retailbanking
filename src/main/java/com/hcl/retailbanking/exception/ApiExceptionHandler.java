package com.hcl.retailbanking.exception;

import com.hcl.retailbanking.dto.ExceptionResponseDto;
import com.hcl.retailbanking.util.ApiConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * handleNullPointerExceptions()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponseDto handleNullPointerExceptions(NullPointerException ex) {
        String defaultMessage = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.NO_ELEMENT_FOUND, defaultMessage);
    }

    /**
     * handleValidationError()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, defaultMessage);
    }



    /**
     * handleAgeNotMatchedException()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AgeNotMatchedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleAgeNotMatchedException(AgeNotMatchedException ex) {
        String msg = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, msg);
    }
    
    /**
     * handlePasswordInvalidException()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(PasswordInvalidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handlePasswordInvalidException(PasswordInvalidException ex) {
        String msg = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, msg);
    }
    
    /**
     * handleMobileNumberExistException()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MobileNumberExistException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleMobileNumberExistException(MobileNumberExistException ex) {
        String msg = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, msg);
    }
    
    /**
     * handleAccountNotFoundException()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleAccountNotFoundException(AccountNotFoundException ex) {
        String msg = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, msg);
    }
    

    /**
     * handleCommonExceptionExceptions()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ResponseEntity<ErrorResponse> commonException(CommonException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(e.ex.getCode(), e.ex.getMessage()));

	}

    /**
     * handleAllRuntimeExceptions()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponseDto handleAllRuntimeExceptions(RuntimeException ex) {
        String defaultMessage = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
    }

    /**
     * handleAllExceptions()
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponseDto handleAllExceptions(Exception ex) {
        String defaultMessage = ex.getMessage();
        return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
    }
}
