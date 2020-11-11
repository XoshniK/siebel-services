package xoshnik.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.siebel.data.SiebelPropertySet;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;
import xoshnik.enums.PsComponent;
import xoshnik.exception.ConverterException;

@Service
public class JsonToPropSetConverter {

	public SiebelPropertySet process(SiebelPropertySet input) throws ConverterException {
		String json = input.getProperty("JSON");
		if (json != null) {
			return processJsonObject(new Gson().fromJson(json, JsonObject.class), new SiebelPropertySet());
		}
		throw new ConverterException("JSON is not provided");
	}

	private SiebelPropertySet processJsonObject(JsonObject jsonObj, SiebelPropertySet ps) {
		for (Entry<String, JsonElement> mapEntry : jsonObj.entrySet()) {
			String jsonKey = mapEntry.getKey();
			JsonElement jsonElement = mapEntry.getValue();
			if (jsonElement.isJsonArray()) {
				processJsonArray(jsonKey, jsonElement.getAsJsonArray(), ps);
			} else if (jsonElement.isJsonObject()) {
				SiebelPropertySet child = new SiebelPropertySet();
				child.setType(jsonKey);
				ps.addChild(processJsonObject(jsonElement.getAsJsonObject(), child));
			} else {
				processPrimitives(jsonKey, jsonElement, ps);
			}
		}
		return ps;
	}

	private void processJsonArray(String jsonKey, JsonArray jsonArray, SiebelPropertySet ps) {
		SiebelPropertySet child = new SiebelPropertySet();
		child.setType(PsComponent.PRIMITIVE_LIST + jsonKey);
		for (int i = 0; i < jsonArray.size(); ++i) {
			JsonElement jsonElement = jsonArray.get(i);
			if (i == 0 && !jsonElement.isJsonPrimitive()) {
					child.setType(PsComponent.LIST_OF + PsComponent.OBJECT_LIST + jsonKey);
			}
			if (jsonElement.isJsonPrimitive()) {
				child.setProperty(String.valueOf(i), jsonElement.getAsString());
			} else {
				SiebelPropertySet temp = new SiebelPropertySet();
				temp.setType(PsComponent.OBJECT_LIST + jsonKey);
				if (jsonElement.isJsonObject()) {
					child.addChild(processJsonObject(jsonElement.getAsJsonObject(), temp));
				} else {
					JsonObject aux = new JsonObject();
					aux.add(String.valueOf(i), jsonElement);
					child.addChild(processJsonObject(aux, temp));
				}
			}
		}
		ps.addChild(child);
	}

	private void processPrimitives(String jsonKey, JsonElement jsonElement, SiebelPropertySet ps) {
		ps.setProperty(jsonKey, removeQuotes(jsonElement.toString()));
	}

	private String removeQuotes(String inputString) {
		if (inputString.charAt(0) == '\"' && inputString.charAt(inputString.length() - 1) == '\"'
				|| inputString.charAt(0) == '\'' && inputString.charAt(inputString.length() - 1) == '\'') {
			return inputString.substring(1, inputString.length() - 1);
		}
		return inputString;
	}

}
