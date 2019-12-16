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

    private static final  Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * handleNullPointerExceptions()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponseDto handleNullPointerExceptions(NullPointerException exception) {
        String defaultMessage = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.NO_ELEMENT_FOUND, defaultMessage);
    }

    /**
     * handleValidationError()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleValidationError(MethodArgumentNotValidException exception) {
    	logger.debug("In handleValidationError");
        BindingResult bindingResult = exception.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, defaultMessage);
    }



    /**
     * handleAgeNotMatchedException()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AgeNotMatchedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleAgeNotMatchedException(AgeNotMatchedException exception) {
        String message = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, message);
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
    public ExceptionResponseDto handlePasswordInvalidException(PasswordInvalidException exception) {
        String message = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, message);
    }
    
    /**
     * handleMobileNumberExistException()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MobileNumberExistException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleMobileNumberExistException(MobileNumberExistException exception) {
        String message = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, message);
    }
    
    /**
     * handleAccountNotFoundException()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDto handleAccountNotFoundException(AccountNotFoundException exception) {
        String message = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, message);
    }
    

    /**
     * handleCommonExceptionExceptions()
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ResponseEntity<ErrorResponse> commonException(CommonException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(e.exception.getCode(), e.exception.getMessage()));

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
    public final ExceptionResponseDto handleAllRuntimeExceptions(RuntimeException exception) {
        String defaultMessage = exception.getMessage();
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
    public final ExceptionResponseDto handleAllExceptions(Exception exception) {
        String defaultMessage = exception.getMessage();
        return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
    }
}
