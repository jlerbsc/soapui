/*
 * SoapUI, Copyright (C) 2004-2022 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.support;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.groovy.JsonSlurper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlainJavaJsonProvider extends AbstractJsonProvider {

    private JsonSlurper jsonSlurper = new JsonSlurper();

    @Override
    public Object parse(String json) throws InvalidJsonException {
        return parse(new StringReader(json));
    }

    @Override
    public Object parse(InputStream inputStream, String s) throws InvalidJsonException {
        return parse(new InputStreamReader(inputStream, Charset.forName(s)));
    }

    private Object parse(Reader jsonReader) throws InvalidJsonException {
        try {
            JSON jsonRoot = jsonSlurper.parse(jsonReader);
            Object converted = convertToPlainJavaImplementation(jsonRoot);
            return MutableValue.TO_MUTABLE_VALUE.apply(converted);
        } catch (Exception e) {
            throw new InvalidJsonException(e);
        }
    }

    @Override
    public String toJson(Object obj) {
        return ((JSON) obj).toString(3);
    }

    @Override
    public Object createMap() {
        return new LinkedHashMap();
    }

    @Override
    public Iterable createArray() {
        return new ArrayList();
    }

    @Override
    public boolean isArray(Object obj) {
        return MutableValue.extractValueFromMutable(obj) instanceof List;
    }

    @Override
    public boolean isMap(Object obj) {
        return MutableValue.extractValueFromMutable(obj) instanceof Map;
    }

    @Override
    public void setProperty(Object obj, Object key, Object value) {
        super.setProperty(MutableValue.extractValueFromMutable(obj), key, value);
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        return super.getPropertyKeys(MutableValue.extractValueFromMutable(obj));
    }

    @Override
    public int length(Object obj) {
        return super.length(MutableValue.extractValueFromMutable(obj));
    }

    private Object convertToPlainJavaImplementation(JSON jsonRoot) {
        if (jsonRoot.isArray()) {
            List<Object> returnedList = new ArrayList<>();
            JSONArray array = (JSONArray) jsonRoot;
            for (Object originalValue : array) {
                if (originalValue instanceof JSON) {
                    returnedList.add(convertToPlainJavaImplementation((JSON) originalValue));
                } else {
                    returnedList.add(originalValue);
                }
            }
            return returnedList;
        } else if (jsonRoot instanceof JSONObject) {
            Map<Object, Object> returnedMap = new HashMap<>();
            JSONObject jsonObject = (JSONObject) jsonRoot;
            for (Object o : jsonObject.keySet()) {
                Object value = jsonObject.get(o);
                if (value instanceof JSON) {
                    returnedMap.put(o, convertToPlainJavaImplementation((JSON) value));
                } else {
                    returnedMap.put(o, value);
                }
            }
            return returnedMap;
        } else {
            //should be JSONNull
            return null;
        }
    }

}
