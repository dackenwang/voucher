package com.boc.voucherjavademo.tools;

import com.train.base.dto.ErrorMsg;
import com.train.base.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

public class AbstractController {
    protected Log log = LogFactory.getLog(this.getClass());

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(
            code = HttpStatus.BAD_REQUEST
    )
    public ErrorMsg bindException(MethodArgumentNotValidException e, HttpServletResponse response) {
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "校验失败:";

        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage = errorMesssage + fieldError.getField() + "(" + fieldError.getRejectedValue() + ")" + fieldError.getDefaultMessage() + ",";
        }

        errorMesssage = errorMesssage.substring(0, errorMesssage.length() - 1);
        return this.genResult(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errorMesssage);
    }

    protected ErrorMsg genResult(int status, String error, String message) {
        ErrorMsg err = new ErrorMsg();
        err.setTimestamp(DateUtils.getCurrentDateString("yyyy-MM-dd HH:mm:ss.SSS"));
        err.setStatus(status);
        err.setError(error);
        err.setMessage(message);
        return err;
    }
}
