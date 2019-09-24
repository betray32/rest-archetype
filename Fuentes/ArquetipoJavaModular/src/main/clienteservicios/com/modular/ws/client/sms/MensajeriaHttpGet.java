package com.modular.ws.client.sms;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.4.redhat-1
 * 2017-10-25T10:42:28.271-03:00
 * Generated source version: 3.1.4.redhat-1
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "MensajeriaHttpGet")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface MensajeriaHttpGet {

    @WebMethod(operationName = "EnvioSMS")
    @WebResult(name = "string", targetNamespace = "http://tempuri.org/", partName = "Body")
    public java.lang.String envioSMS(
        @WebParam(partName = "Mensaje", name = "Mensaje", targetNamespace = "http://tempuri.org/")
        java.lang.String mensaje,
        @WebParam(partName = "celular", name = "celular", targetNamespace = "http://tempuri.org/")
        java.lang.String celular
    );

    @WebMethod(operationName = "CONSULTA")
    public void consulta(
        @WebParam(partName = "SMS_ID", name = "SMS_ID", targetNamespace = "http://tempuri.org/")
        java.lang.String smsID,
        @WebParam(partName = "sms_fecha", name = "sms_fecha", targetNamespace = "http://tempuri.org/")
        java.lang.String smsFecha,
        @WebParam(partName = "sms_mensaje_id", name = "sms_mensaje_id", targetNamespace = "http://tempuri.org/")
        java.lang.String smsMensajeId
    );
}
