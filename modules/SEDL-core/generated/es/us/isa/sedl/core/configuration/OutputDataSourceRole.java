//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.07.14 a las 12:52:39 PM CEST 
//


package es.us.isa.sedl.core.configuration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para OutputDataSourceRole.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="OutputDataSourceRole"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="mainResult"/&gt;
 *     &lt;enumeration value="additionalEvidence"/&gt;
 *     &lt;enumeration value="miscellaneous"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OutputDataSourceRole")
@XmlEnum
public enum OutputDataSourceRole {

    @XmlEnumValue("mainResult")
    MAIN_RESULT("mainResult"),
    @XmlEnumValue("additionalEvidence")
    ADDITIONAL_EVIDENCE("additionalEvidence"),
    @XmlEnumValue("miscellaneous")
    MISCELLANEOUS("miscellaneous");
    private final String value;

    OutputDataSourceRole(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OutputDataSourceRole fromValue(String v) {
        for (OutputDataSourceRole c: OutputDataSourceRole.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
