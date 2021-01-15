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

	@Test
	void testJsonToProp() throws TransformerException, IOException, SAXException {
		step("TestOutbound");
		SiebelPropertySet request = new SiebelPropertySet();
		request.setProperty("RequestIdentifier", "TesticoId");
		request.setProperty("RequestURL", "http://localhost:8080/jsontoprop");
		SiebelPropertySet childPS = new SiebelPropertySet();
		childPS.setType("PropertySet");
		childPS.setProperty(
				"JSON",
				"{ \"type\" : \"list\", \"items\" : [ { \"id\" : \"ra-27769a03-0000-0050-2ae2-eada63edae8b\", \"type\" : \"payment\", \"status\" : \"succeeded\", \"payment_id\" : \"277699da-000f-5000-9000-1d5213f23213\", \"fiscal_document_number\" : \"139761\", \"fiscal_storage_number\" : \"9289000100547721\", \"fiscal_attribute\" : \"3058651636\", \"registered_at\" : \"2020-12-24T12:14:00.000Z\", \"fiscal_provider_id\" : \"cda9c646-d6dd-470f-bcc9-614c7400e916\", \"items\" : [ { \"quantity\" : 1.000, \"amount\" : { \"value\" : \"348.99\", \"currency\" : \"RUB\" }, \"vat_code\" : 6, \"description\" : \"Набор накладных ногтей `DECO.` BRIGHT GLOW pinky (24 шт + клеевые стикеры 24 шт)\", \"payment_mode\" : \"full_prepayment\", \"payment_subject\" : \"commodity\" }, { \"quantity\" : 1.000, \"amount\" : { \"value\" : \"0.01\", \"currency\" : \"RUB\" }, \"vat_code\" : 6, \"description\" : \"Клей для накладных ногтей `DECO.` 10 мл\", \"payment_mode\" : \"full_prepayment\", \"payment_subject\" : \"commodity\" }, { \"quantity\" : 1.000, \"amount\" : { \"value\" : \"319.00\", \"currency\" : \"RUB\" }, \"vat_code\" : 6, \"description\" : \"Набор накладных ногтей `DECO.` STARDUST snowhite (24 шт + клеевые стикеры 24 шт)\", \"payment_mode\" : \"full_prepayment\", \"payment_subject\" : \"commodity\" }, { \"quantity\" : 1.000, \"amount\" : { \"value\" : \"249.00\", \"currency\" : \"RUB\" }, \"vat_code\" : 6, \"description\" : \"Доставка\", \"payment_mode\" : \"full_prepayment\", \"payment_subject\" : \"service\" } ], \"tax_system_code\" : 1 } ] }"
		);
		request.addChild(childPS);
		final SiebelPropertySet response = siebelCaller
				.invokeWorkflow("SiebelServices Outbound Integration", request);
		Assert.assertTrue(true);
	}

	@Test
	void testPropToJson() throws TransformerException, IOException, SAXException {
		step("TestOutbound");
		SiebelPropertySet request = new SiebelPropertySet();
		request.setProperty("RequestIdentifier", "TesticoId");
		request.setProperty("RequestURL", "http://localhost:8080/proptojson");
		SiebelPropertySet childPS = new SiebelPropertySet();
		childPS.setType("PropertySet");
		request.addChild(childPS);

		SiebelPropertySet orderHeaderPs = new SiebelPropertySet();
		childPS.addChild(orderHeaderPs);
		orderHeaderPs.setType("OrderHeader");
		orderHeaderPs.setProperty("ChequeStatus", "6");
		orderHeaderPs.setProperty("ChequeId", "E0002639559");

		SiebelPropertySet listOfObjectListItemsPs = new SiebelPropertySet();
		childPS.addChild(listOfObjectListItemsPs);
		listOfObjectListItemsPs.setType("ListOfObjectList-Items");

		SiebelPropertySet listOfObjectListGiftsPs = new SiebelPropertySet();
		childPS.addChild(listOfObjectListGiftsPs);
		listOfObjectListGiftsPs.setType("ListOfObjectList-Gifts");

		final SiebelPropertySet response = siebelCaller
				.invokeWorkflow("SiebelServices Outbound Integration", request);
		Assert.assertTrue(true);
	}

}
