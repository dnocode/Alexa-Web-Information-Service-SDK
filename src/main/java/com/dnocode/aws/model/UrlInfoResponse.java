//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.19 at 10:54:03 AM CEST 
//


package com.dnocode.aws.model;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for UrlInfoResultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UrlInfoResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Alexa" type="{http://awis.amazonaws.com/doc/2005-07-11}AlexaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "UrlInfoResponse",namespace = "http://alexa.amazonaws.com/doc/2005-10-05/")
public class UrlInfoResponse extends AlexaResponse {

    @XmlElement(name = "Response", required = true)
    protected ResponseUrlInfoType response;



}