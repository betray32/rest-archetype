
package com.modular.ws.client.sms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.test.ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("http://tempuri.org/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.test.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EnvioSMS }
     * 
     */
    public EnvioSMS createEnvioSMS() {
        return new EnvioSMS();
    }

    /**
     * Create an instance of {@link EnvioSMSResponse }
     * 
     */
    public EnvioSMSResponse createEnvioSMSResponse() {
        return new EnvioSMSResponse();
    }

    /**
     * Create an instance of {@link CONSULTA }
     * 
     */
    public CONSULTA createCONSULTA() {
        return new CONSULTA();
    }

    /**
     * Create an instance of {@link CONSULTAResponse }
     * 
     */
    public CONSULTAResponse createCONSULTAResponse() {
        return new CONSULTAResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
