package xoshnik;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ConverterApplication.class)
public class ConverterControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	@Test
	public void testPropSetToJson() throws Exception {
		testUserController("/proptojson", "xml/proptojson.xml");
	}

	@Test
	public void testJsonToPropSet() throws Exception {
		testUserController("/jsontoprop", "xml/jsontoprop.xml");
	}

	private void testUserController(String method, String file) throws Exception {
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post(method)
						.contentType(MediaType.APPLICATION_XML)
						.content(getXmlFromResource(file));

		this.mockMvc.perform(builder)
				.andExpect(MockMvcResultMatchers.status()
						.isOk()).andExpect(mvcResult -> {
			String contentAsString = mvcResult.getResponse().getContentAsString();
			if (!(!contentAsString.equals("") && contentAsString.contains("errorCode=\"0\""))) {
				throw new RuntimeException("Incorrect response");
			}
		});
	}

	private String getXmlFromResource(String filePath) throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(Objects
				.requireNonNull(getClass().getClassLoader().getResource(filePath)).toURI())));
	}
}