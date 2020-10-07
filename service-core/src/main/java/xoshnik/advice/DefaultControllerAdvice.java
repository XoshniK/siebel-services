package xoshnik.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xoshnik.aspect.RequestContext;
import xoshnik.dto.SiebelDTO;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultControllerAdvice {

	private final RequestContext requestContext;

	@ExceptionHandler({Exception.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	SiebelDTO runtimeExceptionHandler(RuntimeException ex) {
		log.error("Failed to process request with ID = " + requestContext.getSiebelDTO().getRequestIdentifier() + "\n"
				+ "Content: " + requestContext.getSiebelDTO().getContent());
		log.error(ex.getLocalizedMessage(), ex);
		return
				SiebelDTO.builder()
						.errorCode("1")
						.errorMessage(ex.getLocalizedMessage())
						.requestIdentifier(requestContext.getSiebelDTO().getRequestIdentifier())
						.build();
	}
}
