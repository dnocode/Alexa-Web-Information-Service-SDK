package com.dnocode.aws;

import com.dnocode.aws.fn.UnmarshalFN;
import com.dnocode.aws.fn.UnmarshalSaxFN;
import com.dnocode.aws.handler.UrlInfoHandler;
import com.dnocode.aws.model.Actions;
import com.dnocode.aws.model.UrlInfoResponse;
import org.apache.http.util.Asserts;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;


public class AlexaTest {


   // @Test
    public void testResponseMappingFunction() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("response.xml").getFile());

        FileInputStream is = new FileInputStream(file);
        Optional<UrlInfoHandler> handler = new UnmarshalSaxFN().apply(is, UrlInfoResponse.class);
        UrlInfoResponse urlResponse = handler.get().toAlexaResponse();
        Asserts.check(urlResponse.isOk(),"invalid status of response");
        Asserts.check(urlResponse.getResponse().getOperationRequest()!=null,"invalid  response");
        Asserts.check(urlResponse.getResponse().getResponseStatus()!=null&&
                urlResponse.getResponse().getResponseStatus().getStatusCode().equals("Success"),"invalid  response");
        Asserts.check(urlResponse.getResponse().getUrlInfoResult()!=null&&
                        urlResponse.getResponse().getUrlInfoResult().getAlexa()!=null&&
                        urlResponse.getResponse().getUrlInfoResult().getAlexa().getContentData()!=null&&
                        urlResponse.getResponse().getUrlInfoResult().getAlexa().getRelated()!=null
                ,"invalid  response");
        Asserts.check(urlResponse.getResponse().getUrlInfoResult().getAlexa()
                .getRelated().getCategories().getCategoryData().size()==1,"no categories mapped");
        System.out.println(urlResponse.getResponse().getUrlInfoResult().getAlexa().getRelated()
                .getCategories().getCategoryData().get(0).getAbsolutePath());
        System.out.println(urlResponse.getResponse().getUrlInfoResult()
                .getAlexa().getRelated().getCategories().getCategoryData().get(0).getTitle());
    }



    //@Test
    public void testRealUrlInfoRequest() throws JAXBException {


        Alexa alexa = new Alexa("client_id", "secret_id");
        try {

            UrlInfoResponse response = alexa.newRequestBuilder()
                    .setAction(Actions.UrlInfo)
                    .setTargetUrl("edebiyathaber.net")
                    .addResponseGroup("Categories")
                    .addResponseGroup("Language")
                    .create()
                    .toObservable(UrlInfoResponse.class)
                    .toBlocking().first();


            System.out.print(response.getResponse().getUrlInfoResult()
                    .getAlexa().getRelated().getCategories().getCategoryData().get(0).getAbsolutePath());

        }catch (Exception e){


            System.out.print("error");
        }
    }
}

