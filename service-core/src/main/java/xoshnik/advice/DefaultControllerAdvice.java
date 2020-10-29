package xoshnik.advice;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xoshnik.dto.SiebelDTO;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultControllerAdvice {

	@ExceptionHandler({Exception.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	SiebelDTO runtimeExceptionHandler(RuntimeException ex) {
		UUID uuid = UUID.randomUUID();
		String errorMessage = ex.getLocalizedMessage() + "; ErrorId = " + uuid;

		log.error(errorMessage, ex);
		return
				SiebelDTO.builder()
						.errorCode("1")
						.errorMessage(errorMessage)
						.build();
	}
}
