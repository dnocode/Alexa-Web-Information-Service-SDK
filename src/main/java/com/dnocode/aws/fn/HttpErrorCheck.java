package com.dnocode.aws.fn;


import org.apache.http.HttpException;
import rx.apache.http.ObservableHttpResponse;
import rx.exceptions.Exceptions;

import java.util.function.Consumer;


public class HttpErrorCheck<T extends ObservableHttpResponse>
        implements Consumer<T> {

    @Override
    public void accept(T t) {


        if(t.getResponse().getStatusLine().getStatusCode()!=200){


            try {
                throw new HttpException(t
                        .getResponse()
                        .getStatusLine().getReasonPhrase());
            } catch (HttpException e) {
                Exceptions.propagate(e);
            }
        }

    }
}
