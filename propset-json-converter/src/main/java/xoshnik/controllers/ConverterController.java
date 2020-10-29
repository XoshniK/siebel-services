package xoshnik.controllers;

import com.siebel.data.SiebelPropertySet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xoshnik.dto.SiebelDTO;
import xoshnik.service.JsonToPropSetConverter;
import xoshnik.service.PropSetToJsonConverter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConverterController {

	private final JsonToPropSetConverter jsonToPropSetConverter;

	private final PropSetToJsonConverter propSetToJsonConverter;

	@PostMapping(value = "jsontoprop")
	public SiebelDTO convertJsonToPropertySet(@RequestBody SiebelDTO dto) {
		SiebelPropertySet output = jsonToPropSetConverter.process(dto.getPropertySet());
		return SiebelDTO.builder().errorCode("0").errorMessage("").requestIdentifier(dto.getRequestIdentifier())
				.propertySet(output)
				.build();
	}

	@PostMapping(value = "proptojson")
	public SiebelDTO convertPropertySetToJson(@RequestBody SiebelDTO dto) {
		SiebelPropertySet output = propSetToJsonConverter.process(dto.getPropertySet());
		return SiebelDTO.builder().errorCode("0").errorMessage("").requestIdentifier(dto.getRequestIdentifier())
				.propertySet(output)
				.build();
	}

}
