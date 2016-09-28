package com.dnocode.aws.handler;

import com.dnocode.aws.model.UrlInfoResponse;
import com.dnocode.aws.model.alexa.*;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Created by dino on 28/09/16.
 */
public class UrlInfoHandler extends GenericSaxAlexaHandler<UrlInfoResponse> {


    private final String TAG_CONTENTDATA="aws:ContentData";
    private final String ATTR_DATAURL="aws:DataUrl";
    private final String ATTR_ADULT_CONTENT ="aws:AdultContent";
    private final String TAG_LANGUAGE="aws:Language";
    private final String ATTR_LOCALE="aws:Locale";
    private final String ATTR_ENCODING="aws:Encoding";
    private final String TAG_RELATED="aws:Related";

    private final String TAG_CATEGORIES="aws:Categories";
    private final String TAG_CATEGORY="aws:CategoryData";
    private final String ATTR_TITLE="aws:Title";
    private final String ATTR_PATH="aws:AbsolutePath";
    private UrlInfoResultType urlInfoResultType;


    @Override
    public void startDocument() throws SAXException {


        this. urlInfoResultType=new UrlInfoResultType();
        RelatedType relatedType=new RelatedType();
        CategoriesType categoriesType = new CategoriesType();
        categoriesType.setCategoryData(new ArrayList<>());
        relatedType.setCategories(categoriesType);
        LanguageType languageType=new LanguageType();
        ContentDataType contentDataType=new ContentDataType();
        contentDataType.setLanguage(languageType);
        AlexaType alextype = new AlexaType();
        alextype.setContentData(contentDataType);
        alextype.setRelated(relatedType);

        this.urlInfoResultType.setAlexa(alextype);




    }

    @Override
    protected void noDefaultCharacters(char[] ch, int start, int length) {


        if (checkPath(TAG_CONTENTDATA, ATTR_DATAURL)) {

                   DataUrlType dataUrlType=new DataUrlType();
                  dataUrlType.setValue(new String(ch,start,length));
                  this.urlInfoResultType.getAlexa().getContentData().setDataUrl(dataUrlType);
        } else if
                (checkPath(TAG_CONTENTDATA, ATTR_ADULT_CONTENT)) {

            this.urlInfoResultType.getAlexa().getContentData().setAdultContent(new String(ch,start,length));
        } else if
                (checkPath(TAG_LANGUAGE, ATTR_LOCALE)) {


            this.urlInfoResultType.getAlexa().getContentData().getLanguage().setLocale(new String(ch,start,length));




        } else if
                (checkPath(TAG_LANGUAGE, ATTR_ENCODING)) {

            this.urlInfoResultType.getAlexa().getContentData().getLanguage().setEncording(new String(ch,start,length));

        }

        else if
                (checkPath(TAG_RELATED, ATTR_DATAURL)) {
            DataUrlType dataUrlType=new DataUrlType();
            dataUrlType.setValue(new String(ch,start,length));
            this.urlInfoResultType.getAlexa().getRelated().setDataUrl(dataUrlType);

        } else if
                (checkPath(TAG_CATEGORY,ATTR_TITLE)) {


            CategoryDataType categoryDataType=new CategoryDataType();
            categoryDataType.setTitle(new String(ch,start,length).trim());

                    this.urlInfoResultType.getAlexa().getRelated().getCategories().getCategoryData()
                            .add(categoryDataType);

        }


        else if
                (checkPath(TAG_CATEGORY,ATTR_PATH)) {

            this.urlInfoResultType.getAlexa().getRelated().getCategories().getCategoryData()
                    .get( this.urlInfoResultType.getAlexa().getRelated().getCategories().getCategoryData().size()-1)
                    .setAbsolutePath(new String (ch,start,length).trim());
        }
    }


    @Override
    public UrlInfoResponse toAlexaResponse() {

        ResponseUrlInfoType alexaWrapper = new ResponseUrlInfoType();
        alexaWrapper.setResponseStatus(this.responseStatus);
        alexaWrapper.setOperationRequest(this.operationRequest);
        alexaWrapper.setUrlInfoResult(this.urlInfoResultType);
        UrlInfoResponse ourFormat=new UrlInfoResponse();
        ourFormat.setResponse(alexaWrapper);
        if(this.responseStatus.getStatusCode().equals("Success")==false) {
            ourFormat.setErrorMessage("response status :" + this.responseStatus.getStatusCode());
        }

        return ourFormat;
    }
}
