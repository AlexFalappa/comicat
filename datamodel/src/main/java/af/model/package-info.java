/**
 * JAXB annotations to assign a namespace to comic datamodel classes.
 * Created by sasha on 30/05/15.
 */
@XmlSchema(
        namespace = "http://falappa.net/comics",
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = {
                @XmlNs(prefix = "cc", namespaceURI = "http://falappa.net/comics")
        }
) package af.model;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;