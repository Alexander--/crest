/*
 * Copyright 2010 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.serializer.jaxb;

import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class XmlEncodedFormJaxbSerializer implements Serializer<List<Param>> {

    public static final String POOL_RETRIEVAL_MAX_WAIT_PROP = JaxbFactory.POOL_RETRIEVAL_MAX_WAIT_PROP;
    public static final String CUSTOM_JAXB = JaxbFactory.CUSTOM_JAXB;
    public static final String MODEL_PACKAGE = JaxbFactory.MODEL_PACKAGE;
    public static final String MODEL_FACTORY_CLASS = JaxbFactory.MODEL_FACTORY_CLASS;

    public static final String DEFAULT_WRAPPER_ELEMENT_NAME = "form-data";

    private final Jaxb jaxb;
    private final QName wrapperElementName;

    public XmlEncodedFormJaxbSerializer(Map<String, Object> config) throws JAXBException {
        this.jaxb = JaxbFactory.create(config);
        this.wrapperElementName = new QName(CRestProperty.get(config, "crest.entity.writer.xml.element-name", DEFAULT_WRAPPER_ELEMENT_NAME));
    }

    public void serialize(List<Param> value, Charset charset, OutputStream out) throws Exception {
        JAXBElement<JaxbParam> object = JaxbParamJAXBElement.create(wrapperElementName, value);
        jaxb.marshal(object, out, charset);
    }


    private static class JaxbParamJAXBElement extends JAXBElement<JaxbParam> implements Classes {

        public JaxbParamJAXBElement(QName name, JaxbParam value) {
            super(name, JaxbParam.class, value);
        }

        public static JAXBElement<JaxbParam> create(QName wrapperElementName, List<Param> value){
            JaxbParam params = new JaxbParam();
            params.setParams(value);
            return new JaxbParamJAXBElement(wrapperElementName, params);
        }

        public Set<Class<?>> getClasses() {
            return getValue().getClasses();
        }

    }


    @XmlRootElement
    private static class JaxbParam {

        @XmlAnyElement
        private List<JAXBElement<?>> params;

        @XmlTransient
        private Set<Class<?>> classes;

        JaxbParam() {
        }

        public Set<Class<?>> getClasses() {
            return classes;
        }

        public void setParams(List<Param> params) {
            this.params = new ArrayList<JAXBElement<?>>();
            this.classes = new HashSet<Class<?>>();
            this.classes.add(getClass());
            for (Param entry: params) {
                Class<?> cls = entry.getConfig().getValueClass();

                for(Object value : entry.getValue()){
                    if(value == null) continue;
                    Object valueEl;
                    if(isPrimitive(value)) {
                       valueEl = value;
                    }else{
                        // Any other cleaner way to do that ??
                        XmlRootElement root = cls.getAnnotation(XmlRootElement.class);
                        String name;
                        String ns;
                        if(root != null) {
                            name = "##default".equals(root.name()) ? decapitalize(cls.getSimpleName()) : root.name();
                            ns = "##default".equals(root.namespace()) ? null : root.namespace();
                        }else{
                            name = decapitalize(cls.getSimpleName());
                            ns = null;
                        }
                        valueEl = new JAXBElement(new QName(ns, name), cls, value);
                    }
                    JAXBElement entryEl = new JAXBElement(new QName(entry.getConfig().getName()), valueEl.getClass(), valueEl);
                   this.params.add(entryEl);
                }


                this.classes.addAll(Types.getActors(entry.getConfig().getValueGenericType()));
            }
        }

        private boolean isPrimitive(Object value){
            return value.getClass().isPrimitive()
                    || value instanceof Number
                    || value instanceof Boolean
                    || value instanceof Character
                    || value instanceof String
                    || value instanceof Date;
        }
        private String decapitalize(String name) {
            if (name == null || name.length() == 0) {
                return name;
            }
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                    Character.isUpperCase(name.charAt(0))){
                return name;
            }
            char chars[] = name.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }





}