package xoshnik;

import static io.qameta.allure.Allure.step;

import com.siebel.data.SiebelPropertySet;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import xoshnik.service.SiebelCaller;
import xoshnik.service.XmlConverter;

class ExampleTest {

	private static SiebelCaller siebelCaller;

	@BeforeAll
	static void initCaller() throws ParserConfigurationException, TransformerConfigurationException {
		XmlConverter xmlConverter = new XmlConverter();
		xmlConverter.init();
		siebelCaller = new SiebelCaller(
				xmlConverter,
				"http://localhost/eai_anon_RUS/start.swe?SWEExtSource=WebService&SWEExtCmd=Execute"
		);
	}

	@Test
	void testWorkflowUtilities() throws TransformerException, IOException, SAXException {
		step("TestWF");
		SiebelPropertySet siebelPropertySet = new SiebelPropertySet();
		siebelPropertySet.setProperty("Name", "Value");
		siebelCaller.invokeBusinessService("Workflow Utilities", "Echo", siebelPropertySet);
		Assert.assertTrue(true);
	}

}
