package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.JSONException;
import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class MiscCodec implements ObjectSerializer {

    public static final MiscCodec instance = new MiscCodec();

    private static String toString(org.w3c.dom.Node node) {
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource domSource = new DOMSource(node);
            StringWriter out = new StringWriter();
            transformer.transform(domSource, new StreamResult(out));
            return out.toString();
        } catch (TransformerException e) {
            throw new JSONException("xml node to string error", e);
        }
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Class<?> objClass = object.getClass();
        String strVal;
        if (objClass == SimpleDateFormat.class) {
            String pattern = ((SimpleDateFormat) object).toPattern();
            if (out.isEnabled(Feature.WRITE_CLASS_NAME)) {
                if (object.getClass() != fieldType) {
                    out.write('{');
                    out.writeFieldName(Inspector.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", pattern);
                    out.write('}');
                    return;
                }
            }
            strVal = pattern;
        } else if (objClass == Class.class) {
            Class<?> clazz = (Class<?>) object;
            strVal = clazz.getName();
        } else if (objClass == InetSocketAddress.class) {
            InetSocketAddress address = (InetSocketAddress) object;
            InetAddress inetAddress = address.getAddress();
            out.write('{');
            if (inetAddress != null) {
                out.writeFieldName("address");
                serializer.write(inetAddress);
                out.write(',');
            }
            out.writeFieldName("port");
            out.writeInt(address.getPort());
            out.write('}');
            return;
        } else if (object instanceof File) {
            strVal = ((File) object).getPath();
        } else if (object instanceof InetAddress) {
            strVal = ((InetAddress) object).getHostAddress();
        } else if (object instanceof TimeZone) {
            TimeZone timeZone = (TimeZone) object;
            strVal = timeZone.getID();
        } else if (object instanceof Currency) {
            Currency currency = (Currency) object;
            strVal = currency.getCurrencyCode();
        } else if (object instanceof Iterator) {
            Iterator<?> it = ((Iterator<?>) object);
            writeIterator(serializer, out, it);
            return;
        } else if (object instanceof Iterable) {
            Iterator<?> it = ((Iterable<?>) object).iterator();
            writeIterator(serializer, out, it);
            return;
        } else if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) object;
            Object objKey = entry.getKey();
            Object objVal = entry.getValue();
            if (objKey instanceof String) {
                String key = (String) objKey;
                if (objVal instanceof String) {
                    String value = (String) objVal;
                    out.writeFieldValueStringWithDoubleQuoteCheck('{', key, value);
                } else {
                    out.write('{');
                    out.writeFieldName(key);
                    serializer.write(objVal);
                }
            } else {
                out.write('{');
                serializer.write(objKey);
                out.write(':');
                serializer.write(objVal);
            }
            out.write('}');
            return;
        } else if (object.getClass().getName().equals("net.sf.json.JSONNull")) {
            out.writeNull();
            return;
        } else if (object instanceof org.w3c.dom.Node) {
            strVal = toString((Node) object);
        } else {
            throw new JSONException("not support class : " + objClass);
        }
        out.writeString(strVal);
    }

    private void writeIterator(JSONSerializer serializer, SerializeWriter out, Iterator<?> it) {
        int i = 0;
        out.write('[');
        while (it.hasNext()) {
            if (i != 0) {
                out.write(',');
            }
            Object item = it.next();
            serializer.write(item);
            ++i;
        }
        out.write(']');
    }
}
