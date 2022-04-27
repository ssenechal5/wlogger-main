package com.actility.thingpark.wlogger.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * Created by proye on 04/09/2014.
 */
public final class XMLUtils {

    public static byte[] writeSmpObject(Object o) throws JAXBException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final JAXBContext context = JAXBContext.newInstance(o.getClass());
        context.createMarshaller().marshal(o, bout);
        return bout.toByteArray();
    }

    public static <T> T convertToSmpObject(String content, Class<T> type) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(type);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new StreamSource(new StringReader(content)), type).getValue();
    }

}
