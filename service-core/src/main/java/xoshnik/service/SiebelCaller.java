package xoshnik.service;

import com.siebel.data.SiebelPropertySet;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.TransformerException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import xoshnik.exception.SiebelInvokingException;
import xoshnik.exception.UrlNotSpecifiedException;


@Service
public class SiebelCaller {

	private final XmlConverter xmlConverter;

	private final String siebelURL;

	public SiebelCaller(XmlConverter xmlConverter, @Value("${siebel.url:}") String siebelURL) {
		this.xmlConverter = xmlConverter;
		this.siebelURL = siebelURL;
	}

	public SiebelPropertySet invokeWorkflow(String processName, SiebelPropertySet inputPS)
			throws TransformerException, IOException, SAXException {
		inputPS.setProperty("ProcessName", processName);
		return invokeBusinessService("Workflow Process Manager", "RunProcess", inputPS);
	}

	public SiebelPropertySet invokeBusinessService(String serviceName, String serviceMethod, SiebelPropertySet inputPS)
			throws TransformerException, IOException, SAXException {
		return callSiebel(serviceName, serviceMethod, inputPS);
	}

	private SiebelPropertySet callSiebel(String serviceName, String serviceMethod, SiebelPropertySet inputPS)
			throws TransformerException, IOException, SAXException {
		if (org.springframework.util.StringUtils.isEmpty(siebelURL)) {
			throw new UrlNotSpecifiedException("Specify Siebel URL in \"siebel.url\" property");
		}
		inputPS.setType("Input");
		String requestXML = xmlConverter.convertToXml(inputPS);
		byte[] requestEncoded = StringUtils.getBytesUtf8(requestXML);
		String requestBase64 = Base64.encodeBase64String(requestEncoded);
		String responseBase64 = sendRequest(serviceName, serviceMethod, requestBase64);
		if (responseBase64.equals("")) {
			return new SiebelPropertySet();
		}
		byte[] responseEncoded = Base64.decodeBase64(responseBase64);
		String responseXML = StringUtils.newStringUtf8(responseEncoded);
		SiebelPropertySet response = xmlConverter.convertToPropSet(responseXML);
		String errorCode = response.getProperty("ErrorCode");
		String errorMessage = response.getProperty("ErrorMessage");
		if(!errorCode.equals("0")){
			throw new SiebelInvokingException(errorCode + errorMessage);
		}
		return response.getChild(0);
	}

	private String sendRequest(String serviceName, String serviceMethod, String serviceParams) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("SoapAction", "\"document/http://siebel.com/CustomUI:InvokeService\"");
		String xml =
				String.format(
						"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://siebel.com/CustomUI\">\n"
								+ "    <soapenv:Header/>\n"
								+ "    <soapenv:Body>\n"
								+ "        <cus:InvokeService_Input>\n"
								+ "            <Request>\n"
								+ "                <Input ServiceName=\"%s\" ServiceMethod=\"%s\" ServiceParams=\"%s\"/>\n"
								+ "            </Request>\n"
								+ "        </cus:InvokeService_Input>\n"
								+ "    </soapenv:Body>\n"
								+ "</soapenv:Envelope>",
						serviceName,
						serviceMethod,
						serviceParams
				);
		HttpEntity<String> request = new HttpEntity<>(xml, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(
				siebelURL,
				request,
				String.class
		);
		String responseBody = response.getBody();
		if (responseBody != null) {
			Pattern pattern = Pattern.compile(".*content=\"(.*)\".*");
			Matcher matcher = pattern.matcher(response.getBody());
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "";
	}

}
