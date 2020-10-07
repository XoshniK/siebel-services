package xoshnik.dto;

import com.siebel.data.SiebelPropertySet;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "ListOfSiebelDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiebelDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String errorCode;

	@XmlAttribute
	private String errorMessage;

	@XmlAttribute
	private String requestIdentifier;

	@XmlAttribute
	private String content;

	@XmlTransient
	private SiebelPropertySet propertySet;

}
