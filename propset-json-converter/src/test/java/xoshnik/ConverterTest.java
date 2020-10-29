package xoshnik;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.siebel.data.SiebelPropertySet;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xoshnik.service.JsonToPropSetConverter;
import xoshnik.service.PropSetToJsonConverter;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@NoArgsConstructor
public class ConverterTest {

	@Autowired
	private JsonToPropSetConverter jsonToPropSetConverter;

	@Autowired
	private PropSetToJsonConverter propSetToJsonConverter;

	@Test
	public void glossary() {
		testJSON("json/glossary.json");
	}

	@Test
	public void menu() {
		testJSON("json/menu.json");
	}

	@Test
	public void widget() {
		testJSON("json/widget.json");
	}

	@Test
	public void webApp() {
		testJSON("json/webapp.json");
	}

	@Test
	public void test() {
		testJSON("json/test.json");
	}

	@Test
	public void yandex() {
		testJSON("json/yandex.json");
	}

	private void testJSON(String fileName) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject origJSON = getJsonFromResourceAsPS(fileName, gson);

		SiebelPropertySet siebelPropertySet = jsonToPropSetConverter.process(getPropSetWithJson(origJSON));

		SiebelPropertySet output = propSetToJsonConverter.process(siebelPropertySet);
		JsonObject newJSON = gson
				.fromJson(output.getProperty("JSON"), JsonObject.class);

		boolean result = gson.toJson(origJSON).length() == gson.toJson(newJSON).length();
		if (!result) {
			log.error(gson.toJson(origJSON));
			log.error(siebelPropertySet.toString());
			log.error(gson.toJson(newJSON));
		}
		assert (result);
	}

	private SiebelPropertySet getPropSetWithJson(JsonObject jsonObject) {
		SiebelPropertySet siebelPropertySet = new SiebelPropertySet();
		siebelPropertySet.setProperty("JSON", jsonObject.toString());
		return siebelPropertySet;
	}

	private JsonObject getJsonFromResourceAsPS(String filePath, Gson gson) {
		JsonObject jsonObject = null;
		try {
			FileReader fileInputStream = null;
			URL resource = this.getClass().getClassLoader().getResource(filePath);
			if (resource != null) {
				fileInputStream = new FileReader(resource.getFile());
			}
			if (fileInputStream != null) {
				jsonObject = gson.fromJson(
						fileInputStream,
						JsonObject.class
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
