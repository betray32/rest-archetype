
package com.modular.ws.client.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SMS_ID" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="sms_fecha" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="sms_mensaje_id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "smsid",
    "smsFecha",
    "smsMensajeId"
})
@XmlRootElement(name = "CONSULTA")
public class CONSULTA {

    @XmlElement(name = "SMS_ID")
    protected int smsid;
    @XmlElement(name = "sms_fecha", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar smsFecha;
    @XmlElement(name = "sms_mensaje_id")
    protected int smsMensajeId;

    /**
     * Obtiene el valor de la propiedad smsid.
     * 
     */
    public int getSMSID() {
        return smsid;
    }

    /**
     * Define el valor de la propiedad smsid.
     * 
     */
    public void setSMSID(int value) {
        this.smsid = value;
    }

    /**
     * Obtiene el valor de la propiedad smsFecha.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSmsFecha() {
        return smsFecha;
    }

    /**
     * Define el valor de la propiedad smsFecha.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSmsFecha(XMLGregorianCalendar value) {
        this.smsFecha = value;
    }

    /**
     * Obtiene el valor de la propiedad smsMensajeId.
     * 
     */
    public int getSmsMensajeId() {
        return smsMensajeId;
    }

    /**
     * Define el valor de la propiedad smsMensajeId.
     * 
     */
    public void setSmsMensajeId(int value) {
        this.smsMensajeId = value;
    }

}
