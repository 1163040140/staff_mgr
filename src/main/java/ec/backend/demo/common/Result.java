package ec.backend.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static Result<Object> ok() {
        Result<Object> result = new Result<>();
        result.code = 200;
        result.message = "成功";
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "成功";
        result.data = data;
        return result;
    }
}