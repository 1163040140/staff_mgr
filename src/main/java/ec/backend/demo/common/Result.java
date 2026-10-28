package ec.backend.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 成功状态码
    public static final Integer SUCCESS_CODE = 200;
    // 失败状态码
    public static final Integer ERROR_CODE = 500;

    // 成功消息
    public static final String SUCCESS_MESSAGE = "成功";
    // 失败消息
    public static final String ERROR_MESSAGE = "失败";

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.code = SUCCESS_CODE;
        result.message = SUCCESS_MESSAGE;
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.code = SUCCESS_CODE;
        result.message = SUCCESS_MESSAGE;
        result.data = data;
        return result;
    }

    public static <T> Result<T> ok(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Result<T> ok(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.code = ERROR_CODE;
        result.message = ERROR_MESSAGE;
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.code = ERROR_CODE;
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.code = ERROR_CODE;
        result.message = "系统错误";
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = ERROR_CODE;
        result.message = message;
        return result;
    }

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean isFailure() {
        return !isSuccess();
    }
}