package com.dnocode.aws.model;

/**
 * Created by dino on 19/07/16.
 */
public class AlexaResponse implements IAlexaResponse {

    protected String errorMessage;

    public boolean isOk(){ return errorMessage==null;}

    public void setErrorMessage(String errorMessage){this.errorMessage=errorMessage;}

    public String getErrorMessage(){return this.errorMessage;}


}
