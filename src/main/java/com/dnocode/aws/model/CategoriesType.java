//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.19 at 10:54:03 AM CEST 
//


package com.dnocode.aws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CategoriesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CategoriesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CategoryData" type="{http://awis.amazonaws.com/doc/2005-07-11}CategoryDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoriesType", propOrder = {
    "categoryData"
})
public class CategoriesType {

    @XmlElement(name = "CategoryData", required = true)
    protected CategoryDataType categoryData;

    /**
     * Gets the value of the categoryData property.
     * 
     * @return
     *     possible object is
     *     {@link CategoryDataType }
     *     
     */
    public CategoryDataType getCategoryData() {
        return categoryData;
    }

    /**
     * Sets the value of the categoryData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CategoryDataType }
     *     
     */
    public void setCategoryData(CategoryDataType value) {
        this.categoryData = value;
    }

}
