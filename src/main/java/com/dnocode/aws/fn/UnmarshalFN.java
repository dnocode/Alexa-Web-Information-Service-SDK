package com.dnocode.aws.fn;

import com.dnocode.aws.model.Actions;
import com.dnocode.aws.model.AlexaResponse;
import com.dnocode.aws.model.ErrorResponse;
import rx.exceptions.Exceptions;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.function.Function;


public class UnmarshalFN<T extends AlexaResponse>
        implements Function<String, T> {
    @Override
    public T apply(String s) {
        String responseSuffix="Response";
        Class targetClazz = null;
       for(int i=0; i< Actions.values().length;i++){
          if(s.contains( Actions.values()[i]+responseSuffix)){
              try {
                  targetClazz=  Class.forName("com.dnocode.aws.model."+Actions.values()[i]+responseSuffix);
                  break;
              } catch (ClassNotFoundException e) {
                  Exceptions.propagate(e);
              }
          }
       }
        if (s.contains("Errors")&&s.contains("Status")){ targetClazz= ErrorResponse.class;   }

        try {
                 JAXBContext jc  = JAXBContext.newInstance(targetClazz);
                  Unmarshaller unmarshaller = jc.createUnmarshaller();
                   T result = (T) unmarshaller
                    .unmarshal(Optional.of(s).map(String::getBytes)
                            .map(ByteArrayInputStream::new).get());
            return result;
        } catch (JAXBException e) {
            Exceptions.propagate(e);
            return null;
        }
    }
}
