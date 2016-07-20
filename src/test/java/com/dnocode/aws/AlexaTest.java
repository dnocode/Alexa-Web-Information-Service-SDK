package com.dnocode.aws;

import com.dnocode.aws.fn.UnmarshalFN;
import com.dnocode.aws.model.Actions;
import com.dnocode.aws.model.UrlInfoResponse;
import org.apache.http.util.Asserts;
import org.junit.Test;
import rx.exceptions.Exceptions;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.SignatureException;
import java.util.Optional;


public class AlexaTest {


    @Test
    public void testResponseMappingFunction() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("response.xml").getFile());

        byte[] encoded = Files.readAllBytes(file.toPath());
        String stringResponse= new String(encoded, "UTF8");

        UrlInfoResponse response= (UrlInfoResponse) Optional
                .of(stringResponse)
                .map(new UnmarshalFN<>()).get();

        Asserts.check(response.isOk(),"mapping error");
    }

    @Test
    public void testRealUrlInfoRequest() throws JAXBException {


        Alexa alexa = new Alexa("access_key_id", "secret");

        try {


            UrlInfoResponse response = alexa.newRequestBuilder()
                    .setAction(Actions.UrlInfo)
                    .setTargetUrl("www.corrieredellosport.it")
                    .addResponseGroup("Categories")
                    .addResponseGroup("Language")
                    .create()
                    .toObservable(UrlInfoResponse.class)
                    .toBlocking().first();


        }catch (Exception e){


            System.out.print("error");
        }
    }
}
