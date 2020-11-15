package xoshnik;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.siebel.data.SiebelPropertySet;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import xoshnik.exception.ConverterException;
import xoshnik.service.JsonToPropSetConverter;
import xoshnik.service.PropSetToJsonConverter;
import xoshnik.service.PropertySetUtils;

@Slf4j
@NoArgsConstructor
public class ConverterTest {

	private final JsonToPropSetConverter jsonToPropSetConverter = new JsonToPropSetConverter();

	private final PropSetToJsonConverter propSetToJsonConverter = new PropSetToJsonConverter(new PropertySetUtils());

	@Test
	public void glossary() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/glossary.json");
	}

	@Test
	public void menu() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/menu.json");
	}

	@Test
	public void widget() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/widget.json");
	}

	@Test
	public void webApp() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/webapp.json");
	}

	@Test
	public void test() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/test.json");
	}

	@Test
	public void yandex() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/yandex.json");
	}

	@Test
	public void emptyArray() throws ConverterException, IOException, URISyntaxException {
		testJSON("json/emptyArray.json");
	}

	private void testJSON(String fileName) throws ConverterException, IOException, URISyntaxException {
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

	private JsonObject getJsonFromResourceAsPS(String filePath, Gson gson) throws URISyntaxException, IOException {
		String json = new String(Files.readAllBytes(Paths.get(Objects
				.requireNonNull(getClass().getClassLoader().getResource(filePath)).toURI())));
		return gson.fromJson(json, JsonObject.class);
	}

}
