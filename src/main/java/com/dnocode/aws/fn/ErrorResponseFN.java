package com.dnocode.aws.fn;


import com.dnocode.aws.model.AlexaResponse;

import java.util.function.BiFunction;


public class ErrorResponseFN<T extends AlexaResponse> implements BiFunction<Throwable,Class<T>,T> {
    @Override
    public T apply(Throwable throwable, Class<T> tClass) {
        try {
            T errorResponse=tClass.newInstance();
            errorResponse.setErrorMessage(throwable.toString());
            return errorResponse;
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    }



