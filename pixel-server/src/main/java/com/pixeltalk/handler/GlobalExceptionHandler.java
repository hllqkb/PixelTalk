package com.pixeltalk.handler;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.pixeltalk.exception.BaseException;
import com.pixeltalk.exception.NotLoginException;
import com.pixeltalk.result.Result;
import com.pixeltalk.utils.AjaxJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获数据完整性违反异常
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        // 获取原始的 SQLException
        Throwable cause = ex.getCause();
        String errorMessage = "发生数据完整性违反异常";

        if (cause instanceof SQLException) {
            SQLException sqlException = (SQLException) cause;
            errorMessage = sqlException.getMessage();
            // 如果是数据截断异常，进一步处理
            if (errorMessage.contains("Data truncated")) {
                errorMessage = "提供的数据值超出了列的定义范围或长度";
            }
        }

        log.error("DataIntegrityViolationException: {}", errorMessage, ex);
        return new ResponseEntity<>(AjaxJson.getError(errorMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * 捕获所有异常
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);

        AjaxJson aj = null;
        if (ex instanceof NotLoginException) {    // 如果是未登录异常
            NotLoginException ee = (NotLoginException) ex;
            aj = AjaxJson.getNotLogin().setMsg(ee.getMessage());
        } else if(ex instanceof NotRoleException) {       // 如果是角色异常
            NotRoleException ee = (NotRoleException) ex;
            aj = AjaxJson.getNotJur("无此角色：" + ee.getRole());
        } else if(ex instanceof NotPermissionException) {   // 如果是权限异常
            NotPermissionException ee = (NotPermissionException) ex;
            aj = AjaxJson.getNotJur("无此权限：" + ee.getCode());
        } else {    // 普通异常, 输出：500 + 异常信息
            aj = AjaxJson.getError(ex.getMessage());
        }

        return new ResponseEntity<>(aj, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
