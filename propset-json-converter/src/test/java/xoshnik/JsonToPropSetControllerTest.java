package xoshnik;

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
@ContextConfiguration(classes = Application.class)
public class JsonToPropSetControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	@Test
	public void testUserController() throws Exception {

		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post("/json")
						.contentType(MediaType.APPLICATION_XML)
						.content(createUserInXml(
						));

		this.mockMvc.perform(builder)
				.andExpect(MockMvcResultMatchers.status()
						.isOk());
	}

	private static String createUserInXml() {
		return "<SiebelDTO>" +
				"<errorCode>" + 0 + "</errorCode>" +
				"<errorMessage>" + 0 + "</errorMessage>" +
				"<identifier>" + 0 + "</identifier>" +
				"<value>" + 0 + "</value>" +
				"</SiebelDTO>";
	}
}