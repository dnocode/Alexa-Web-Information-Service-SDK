package com.dnocode.aws.handler;

import com.dnocode.aws.model.AlexaResponse;
import com.dnocode.aws.model.alexa.OperationRequestType;
import com.dnocode.aws.model.alexa.ResponseStatusType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by dino on 28/09/16.
 */
public abstract class GenericSaxAlexaHandler<T extends AlexaResponse> extends DefaultHandler {


    private final static String TAG_OPERATION_REQUEST="aws:OperationRequest";
    private final static String ATTR_REQUEST_ID="aws:RequestId";

    private final static String TAG_RESPONSE_STATUS="aws:ResponseStatus";
    private final static String ATTR_STATUS_CODE="aws:StatusCode";

    protected List<String> currentPath=new ArrayList();
    protected ResponseStatusType responseStatus;
    protected OperationRequestType operationRequest;



    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {currentPath.add(qName);}

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {currentPath.remove(qName);}


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(checkPath(TAG_OPERATION_REQUEST,ATTR_REQUEST_ID)){
           this.operationRequest=new OperationRequestType();
            this.operationRequest.setRequestId(new String(ch,start,length));
        }else
        if(checkPath(TAG_RESPONSE_STATUS,ATTR_STATUS_CODE)){
            this.responseStatus=new ResponseStatusType();
            this.responseStatus.setStatusCode(new String(ch,start,length));
        }else{

            noDefaultCharacters(ch,start,length);
        }

    }



    protected  boolean checkPath(String ... tags ){

        try {
            for (int i = 1; i < tags.length; i++) {
                int index = tags.length - i;
                if (currentPath.size() - i < 0) {
                    return false;
                }
                if (tags[index].equals(currentPath.get(currentPath.size() - i)) == false) {
                    return false;
                }
            }

            return true;

        }catch (Exception e){


            return false;}
    }


    protected abstract void  noDefaultCharacters(char[] ch, int start, int length);

    public abstract T toAlexaResponse();

}
