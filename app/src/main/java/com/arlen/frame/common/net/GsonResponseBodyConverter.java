package com.arlen.frame.common.net;

import com.arlen.frame.common.model.BaseError;
import com.arlen.frame.common.model.BaseResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            BaseResult result = gson.fromJson(response, BaseResult.class);
            if (!result.isSuccess()){
                BaseError errResponse = gson.fromJson(response, BaseError.class);
                throw new ResultException(errResponse);
            } else {
                return gson.fromJson(response, type);
            }
        }finally {
            value.close();
        }
    }
}