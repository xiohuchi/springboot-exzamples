package com.dianmi.auth.util;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yangbin
 * @date 2019年10月14日
 * <p>
 * iam-dianmi
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;


    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, 200, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, 200, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, 200, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, 500, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, 500, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, 500, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, 500, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
