package xoshnik.controllers;

import com.siebel.data.SiebelPropertySet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xoshnik.dto.SiebelDTO;
import xoshnik.service.PropSetToJsonConverter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PropSetToJsonController {

	private final PropSetToJsonConverter propSetToJsonConverter;

	@PostMapping(value = "proptojson")
	public SiebelDTO convertJsonToPropertySet(@RequestBody SiebelDTO dto) {
		SiebelPropertySet output = propSetToJsonConverter.process(dto.getPropertySet());
		return SiebelDTO.builder().errorCode("0").errorMessage("").requestIdentifier(dto.getRequestIdentifier())
				.propertySet(output)
				.build();
	}

}
