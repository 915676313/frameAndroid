package com.arlen.frame.common.net;

import com.arlen.frame.common.model.BaseError;

import java.io.IOException;

public class ResultException extends IOException {

    public BaseError getBaseError() {
        return baseError;
    }

    public void setBaseError(BaseError baseError) {
        this.baseError = baseError;
    }

    private BaseError baseError;

    public ResultException(BaseError baseError){
        this.baseError = baseError;
    }

}