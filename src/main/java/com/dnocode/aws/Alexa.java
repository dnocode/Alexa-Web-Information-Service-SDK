package com.dnocode.aws;

import com.dnocode.aws.fn.ErrorResponseFN;
import com.dnocode.aws.fn.UnmarshalSaxFN;
import com.dnocode.aws.handler.GenericSaxAlexaHandler;
import com.dnocode.aws.model.Actions;
import com.dnocode.aws.model.AlexaResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.xml.sax.helpers.DefaultHandler;
import rx.Observable;
import sun.misc.BASE64Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dino on 18/07/16.
 */
public class Alexa {


    private CloseableHttpAsyncClient httpAsyncClient;
    private static final String SERVICE_HOST = "awis.amazonaws.com";
    private static final String AWS_BASE_URL = "http://" + SERVICE_HOST + "/?";
    private static final String HASH_ALGORITHM = "HmacSHA256";
    private static final String DATEFORMAT_AWS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private  final  String AWS_ACCESS;
    private  final  String AWS_SECRET;


    public Alexa(String awsAccess,String awsSecret)  {
       this(awsAccess,awsSecret, HttpAsyncClients.createDefault());
    }


    public Alexa(String awsAccess,String awsSecret,CloseableHttpAsyncClient httpAsyncClient)  {

        this.AWS_ACCESS=awsAccess;
        this.AWS_SECRET=awsSecret;
        this.httpAsyncClient=httpAsyncClient;
        httpAsyncClient.start();

    }

    public AlexaRequestBuilder newRequestBuilder(){ return new AlexaRequestBuilder();    }

   public  class AlexaRequestBuilder {

       private String url;
       private Actions action;
       private ArrayList<String> responseGroup;

       public AlexaRequestBuilder(){ this.responseGroup= new ArrayList<>();}

      public AlexaRequestBuilder setAction(Actions action){
          this.action=action;
          return this;
      }

      public AlexaRequestBuilder setTargetUrl(String url){
          this.url=url;
          return this;
      }

       public  AlexaRequestBuilder  addResponseGroup(String responseGroup){
           this.responseGroup.add(responseGroup);
           return this;
       }

       private void validation() throws ValidationException {

           switch (action){

               case   UrlInfo:
                   if(this.url==null||
                           responseGroup.size()==0){
                        throw  new ValidationException("url is required!");
                   }
                   break;
               case   TrafficHistory:
                   //todo
                   break;
               case   CategoryBrowse:
                   //todo
                   break;
               case   CategoryListings:

                   //todo
                   break;
               case   SitesLinkingIn:

                   //todo
                   break;
           }
       }

       public AlexaRequest create() throws ValidationException,
               URISyntaxException,
               SignatureException,
               UnsupportedEncodingException {
           validation();
           URIBuilder builder = new URIBuilder();
           URIBuilder urib = builder.setScheme("http").setHost(SERVICE_HOST).setPort(80)
                   .setParameter("AWSAccessKeyId", URLEncoder.encode(AWS_ACCESS,"UTF8"))
                   .setParameter("Action", URLEncoder.encode(action.toString(),"UTF8"))
                   .setParameter("ResponseGroup", URLEncoder.encode(String.join(",",responseGroup),"UTF8"));
           urib.setParameter("SignatureMethod", URLEncoder.encode(HASH_ALGORITHM,"UTF8"));
           urib.setParameter("SignatureVersion", URLEncoder.encode("2","UTF8"));
           urib.setParameter("Timestamp", URLEncoder.encode(getTimestampFromLocalTime(Calendar.getInstance().getTime()),"UTF8"));
           if(url!=null){ urib.setParameter("Url",URLEncoder.encode(url,"UTF8")); }
           String queryString;
           try { queryString=urib.build().getQuery();  }
           catch (URISyntaxException e) {throw  new URISyntaxException(e.getMessage(),"error on query String creation");}
           String signature = "GET\n" + SERVICE_HOST + "\n/\n" + queryString;
           signature=generateSignature(signature);
           String uri = AWS_BASE_URL + queryString + "&Signature=" + URLEncoder.encode(signature, "UTF-8");
           System.out.println("doing request:"+ uri);
           return new AlexaRequest(uri);
       }

       /**
        * Computes RFC 2104-compliant HMAC signature.
        *
        * @param data The data to be signed.
        * @return The base64-encoded RFC 2104-compliant HMAC signature.
        * @throws java.security.SignatureException
        *          when signature generation fails
        */
       private String generateSignature(String data)
               throws java.security.SignatureException {
           String result;
           try {
               // get a hash key from the raw key bytes
               SecretKeySpec signingKey = new SecretKeySpec(AWS_SECRET.getBytes(), HASH_ALGORITHM);
               // get a hasher instance and initialize with the signing key
               Mac mac = Mac.getInstance(HASH_ALGORITHM);
               mac.init(signingKey);
               // compute the hmac on input data bytes
               byte[] rawHmac = mac.doFinal(data.getBytes());
               // base64-encode the hmac
               // result = Encoding.EncodeBase64(rawHmac);
               result = new BASE64Encoder().encode(rawHmac);

           } catch (Exception e) {
               throw new SignatureException("Failed to generate HMAC : "
                       + e.getMessage());
           }
           return result;
       }

   }

    /**
     * Generates a timestamp for use with AWS request signing
     *
     * @param date current date
     * @return timestamp
     */
    private  String getTimestampFromLocalTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT_AWS);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public class AlexaRequest {

        private final String finalUrl;



        private AlexaRequest(String finalUrl ){
        this.finalUrl=finalUrl;
        }

        public  <E extends AlexaResponse> Observable<E> toObservable(Class<E> clazz){

            return Observable.create(subscriber -> {

                try {
                    URL url = new URL(finalUrl);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();

                    Optional<GenericSaxAlexaHandler<E>> saxhandler = new UnmarshalSaxFN().apply(in, clazz);

                     subscriber.onNext(saxhandler.isPresent()?saxhandler.get().toAlexaResponse():null);

                } catch ( Exception e ) {
                    subscriber.onNext(new ErrorResponseFN<E>().apply(e,clazz));
                }

                subscriber.onCompleted();
            });

        }



    }


}
