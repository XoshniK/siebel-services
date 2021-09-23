package xoshnik;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.NoArgsConstructor;
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
@ContextConfiguration(classes = InvokerApplication.class)
@NoArgsConstructor
public class InvokerControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	@Test
	public void testJsonToSoap() throws Exception {
		testUserController("/jsontosoap", "json/jsontosoap.json");
	}


	private void testUserController(String method, String file) throws Exception {
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post(method)
						.contentType(MediaType.APPLICATION_JSON)
						.content(getJsonFromResource(file));

		mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status()
				.isOk());

	}

	private String getJsonFromResource(String filePath) throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(Objects
				.requireNonNull(getClass().getClassLoader().getResource(filePath)).toURI())));
	}
}