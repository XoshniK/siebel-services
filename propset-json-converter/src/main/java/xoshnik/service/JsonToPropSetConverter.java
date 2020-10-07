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

	public SiebelPropertySet process(SiebelPropertySet input) {
		String json = input.getProperty("JSON");
		if (json != null) {
			JsonObject obj = new Gson().fromJson(json, JsonObject.class);
			return jsonObjectToPropertySet(obj, new SiebelPropertySet());
		}
		throw new ConverterException("JSON is not provided");
	}

	private static SiebelPropertySet jsonObjectToPropertySet(JsonObject jsonObj, SiebelPropertySet ps) {
		for (Entry mapEntry : jsonObj.entrySet()) {
			if (mapEntry != null) {
				JsonElement jsonelement = (JsonElement) mapEntry.getValue();
				SiebelPropertySet child;
				if (jsonelement.isJsonArray()) {
					JsonArray jsonArray = jsonelement.getAsJsonArray();
					child = new SiebelPropertySet();
					if (jsonArray.size() > 0 && jsonArray.get(0).isJsonPrimitive()) {
						child.setType(PsComponent.PRIMITIVE_LIST + mapEntry.getKey().toString());
					} else {
						child.setType(PsComponent.LIST_OF + PsComponent.OBJECT_LIST + mapEntry.getKey().toString());
					}

					for (int i = 0; i < jsonArray.size(); ++i) {
						if (jsonArray.get(i).isJsonPrimitive()) {
							child.setProperty(String.valueOf(i), jsonArray.get(i).getAsString());
						} else {
							SiebelPropertySet temp = new SiebelPropertySet();
							temp.setType(PsComponent.OBJECT_LIST + mapEntry.getKey().toString());
							if (jsonArray.get(i).isJsonObject()) {
								child.addChild(jsonObjectToPropertySet(jsonArray.get(i).getAsJsonObject(), temp));
							} else {
								JsonObject aux = new JsonObject();
								aux.add(String.valueOf(i), jsonArray.get(i));
								child.addChild(jsonObjectToPropertySet(aux, temp));
							}
						}
					}
					ps.addChild(child);
				} else if (jsonelement.isJsonObject()) {
					JsonObject jsonObject = jsonelement.getAsJsonObject();
					child = new SiebelPropertySet();
					child.setType(mapEntry.getKey().toString());
					ps.addChild(jsonObjectToPropertySet(jsonObject, child));
				} else {
					ps.setProperty(mapEntry.getKey().toString(), removeQuotes(mapEntry.getValue().toString()));
				}
			}
		}

		return ps;
	}

	private static String removeQuotes(String inputString) {
		if (inputString.charAt(0) == '\"' && inputString.charAt(inputString.length() - 1) == '\"'
				|| inputString.charAt(0) == '\'' && inputString.charAt(inputString.length() - 1) == '\'') {
			return inputString.substring(1, inputString.length() - 1);
		}
		return inputString;
	}

}
