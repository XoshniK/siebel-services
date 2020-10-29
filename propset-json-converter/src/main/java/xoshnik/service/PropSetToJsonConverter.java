package xoshnik.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.siebel.data.SiebelPropertySet;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import xoshnik.enums.PsComponent;
import xoshnik.exception.ConverterException;

@Service
public class PropSetToJsonConverter {

	public SiebelPropertySet process(SiebelPropertySet input) {
		if (input != null) {
			JsonObject myJSON = convertPropSetToJson(input);
			SiebelPropertySet output = new SiebelPropertySet();
			output.setProperty("JSON", myJSON.toString());
			return output;
		}
		throw new ConverterException("PropertySet is not provided");
	}

	private JsonObject convertPropSetToJson(SiebelPropertySet ps) {
		JsonObject siebJSON = new JsonObject();
		addPropertiesToJson(ps, siebJSON);
		for (int i = 0; i < ps.getChildCount(); ++i) {
			SiebelPropertySet child = ps.getChild(i);
			applyCrutch(child);
			String childType = child.getType();
			if (childType.startsWith(PsComponent.LIST_OF + PsComponent.OBJECT_LIST)) {
				processListOfObjectList(siebJSON, child);
			} else if (childType.startsWith(PsComponent.PRIMITIVE_LIST)) {
				siebJSON.add(
						childType.substring(PsComponent.PRIMITIVE_LIST.length()),
						addPrimitivesToJson(child, new JsonArray())
				);
			} else if (childType.startsWith(PsComponent.LIST_OF)) {
					siebJSON.add(
							childType.substring(PsComponent.LIST_OF.length()),
							convertPropSetToJson(child.getChild(0))
					);
				} else {
				siebJSON.add(childType, convertPropSetToJson(child));
				}
		}
		return siebJSON;
	}

	private void processListOfObjectList(JsonObject siebJSON, SiebelPropertySet child) {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < child.getChildCount(); ++i) {
			jsonArray.add(convertPropSetToJson(child.getChild(i)));
		}
		siebJSON.add(child.getType().substring((PsComponent.LIST_OF + PsComponent.OBJECT_LIST).length()), jsonArray);
	}

	private void applyCrutch(SiebelPropertySet child) {
		if (child.getType().endsWith("_set")) {
			child.setType(child.getType().substring(0, child.getType().length() - 4));
		}
	}

	private JsonObject addPropertiesToJson(SiebelPropertySet ps, JsonObject jsonObject) {
		getPropertiesAsStream(ps).forEach(propName -> jsonObject.addProperty(propName, ps.getProperty(propName)));
		return jsonObject;
	}

	private JsonArray addPrimitivesToJson(SiebelPropertySet ps, JsonArray jsonArray) {
		getPropertiesAsStream(ps).forEach(propName -> jsonArray.add(new JsonPrimitive(ps.getProperty(propName))));
		return jsonArray;
	}

	private Stream<String> getPropertiesAsStream(SiebelPropertySet ps) {
		return Stream.concat(Stream.of(ps.getFirstProperty()), Stream.generate(ps::getNextProperty))
				.limit(ps.getPropertyCount());
	}

}
