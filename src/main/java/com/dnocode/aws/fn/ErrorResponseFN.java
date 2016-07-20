package com.dnocode.aws.fn;


import com.dnocode.aws.model.AlexaResponse;
import org.apache.http.HttpException;
import rx.apache.http.ObservableHttpResponse;
import rx.exceptions.Exceptions;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


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



