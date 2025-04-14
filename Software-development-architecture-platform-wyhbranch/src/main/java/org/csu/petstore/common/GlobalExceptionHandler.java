package org.csu.petstore.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        logger.error(e.getMessage());
        return CommonResponse.createForError("缺少参数");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleConstraintViolationException(ConstraintViolationException e){
        logger.error(e.getMessage());
        return CommonResponse.createForError("参数非法");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleException(Exception e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return CommonResponse.createForError("服务器异常");
    }
}
