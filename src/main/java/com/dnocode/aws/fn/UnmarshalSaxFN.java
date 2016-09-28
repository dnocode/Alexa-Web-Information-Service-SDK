package com.dnocode.aws.fn;

import com.dnocode.aws.handler.GenericSaxAlexaHandler;
import com.dnocode.aws.model.AlexaResponse;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiFunction;

public class UnmarshalSaxFN <T extends  AlexaResponse>
        implements BiFunction<InputStream,
        Class<T>,
        Optional<GenericSaxAlexaHandler>> {

    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final String HANDLER_SUFFIX = "Handler";
    private final String RESPONSE_SUFFIX = "Response";


    @Override
    public Optional<GenericSaxAlexaHandler> apply(InputStream is, Class<T> alexaResponseClass) {

        try {
            SAXParser saxParser = factory.newSAXParser();
            Class<DefaultHandler> SaxHandlerClazz = (Class<DefaultHandler>) Class
                    .forName("com.dnocode.aws.handler." + alexaResponseClass.getSimpleName()
                            .replace(RESPONSE_SUFFIX, HANDLER_SUFFIX));
            GenericSaxAlexaHandler alexaSaxHandler = (GenericSaxAlexaHandler) SaxHandlerClazz.newInstance();
            saxParser.parse(is, alexaSaxHandler);
            return Optional.of(alexaSaxHandler);
        } catch (Exception e) {
            System.out.print("error in UnmarshalSaxFN:" + e.toString());
            return Optional.empty();

        }
    }


}
