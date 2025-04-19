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

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String errorMessage = "缺少必要参数: " + e.getParameterName();
        logger.error(errorMessage, e);
        return CommonResponse.createForError(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = "参数验证失败: " + e.getMessage();
        logger.error(errorMessage, e);
        return CommonResponse.createForError(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = "参数不合法: " + e.getMessage();
        logger.error(errorMessage, e);
        return CommonResponse.createForError(errorMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleNullPointerException(NullPointerException e) {
        String errorMessage = "系统错误: 空指针异常";
        logger.error(errorMessage, e);
        return CommonResponse.createForError(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleException(Exception e) {
        String errorMessage = "服务器异常: " + e.getMessage();
        logger.error(errorMessage, e);
        return CommonResponse.createForError(errorMessage);
    }
}
