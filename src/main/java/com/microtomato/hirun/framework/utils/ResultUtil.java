package com.microtomato.hirun.framework.utils;

import com.microtomato.hirun.framework.data.Result;
import lombok.Data;

import java.util.List;

/**
 * @author Steven
 * @date 2019-04-25
 */
@Data
public class ResultUtil {

    public static Result success() {
        Result result = new Result();
        result.setCode(0);
        return result;
    }

    public static Result success(Object rows) {
        Result result = new Result();
        result.setCode(0);
        result.setRows(rows);
        if (rows instanceof List) {
            List list = (List) rows;
            result.setTotal(list.size());
        }
        return result;
    }

    public static Result failure(int code) {
        Result result = new Result();
        result.setCode(code);
        return result;
    }

    public static Result failure(String message) {
        Result result = new Result();
        result.setCode(-1);
        result.setMessage(message);
        return result;
    }

    public static Result failure(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
