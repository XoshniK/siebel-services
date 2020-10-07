package xoshnik.service;

import com.siebel.data.SiebelPropertySet;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class XmlConverter {

	private DocumentBuilder builder;

	private Transformer transformer;

	@PostConstruct
	private void init() throws ParserConfigurationException, TransformerConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		String feature = "http://apache.org/xml/features/disallow-doctype-decl";
		documentBuilderFactory.setFeature(feature, true);
		builder = documentBuilderFactory.newDocumentBuilder();

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
	}


	public SiebelPropertySet convertToPropSet(String input) throws IOException, SAXException {
		Document doc = builder.parse(new InputSource(new StringReader(input)));
		if (doc != null) {
			SiebelPropertySet outputPS = new SiebelPropertySet();
			doc.getDocumentElement().normalize();
			Element documentElement = doc.getDocumentElement();
			addAttributesToPs(outputPS, documentElement.getAttributes());
			addNodesToPs(outputPS, documentElement.getChildNodes());
			return outputPS;
		}
		return null;
	}

	private void addAttributesToPs(SiebelPropertySet propertySet, NamedNodeMap attributes) {
		for (int i = 0; i < attributes.getLength(); i++) {
			Node item = attributes.item(i);
			propertySet.setProperty(item.getNodeName(), item.getNodeValue());
		}
	}

	private void addNodesToPs(SiebelPropertySet propertySet, NodeList childNodes) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			SiebelPropertySet childPS = new SiebelPropertySet();
			childPS.setType(item.getNodeName());
			childPS.setValue(item.getNodeValue());
			propertySet.addChild(childPS);
			addAttributesToPs(childPS, item.getAttributes());
			addNodesToPs(childPS, item.getChildNodes());
		}
	}

	public String convertToXml(SiebelPropertySet inputPS) throws TransformerException {
		Document document = builder.newDocument();
		Element listOfContent = document.createElement("ListOfContent");
		document.appendChild(listOfContent);
		addAttributesToXml(document, listOfContent, inputPS);
		addNodesToXml(document, listOfContent, inputPS);
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		return writer.toString();
	}

	private void addAttributesToXml(Document document, Element parent, SiebelPropertySet inputPS) {
		for (String propName = inputPS.getFirstProperty(); !propName.equals(""); propName = inputPS.getNextProperty()) {
			String propVal = inputPS.getProperty(propName);
			Attr attr = document.createAttribute(propName);
			attr.setValue(propVal);
			parent.setAttributeNode(attr);
		}
	}

	private void addNodesToXml(Document document, Element parent, SiebelPropertySet inputPS) {
		addAttributesToXml(document, parent, inputPS);
		for (int i = 0; i < inputPS.getChildCount(); i++) {
			SiebelPropertySet child = inputPS.getChild(i);
			Element element = document.createElement(child.getType());
			addNodesToXml(document, element, child);
			parent.appendChild(element);
		}
	}


}
