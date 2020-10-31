package xoshnik.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import xoshnik.dto.SiebelDTO;
import xoshnik.service.XmlConverter;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestAspect {

	private final XmlConverter xmlConverter;

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controller() {
		//pointcut
	}

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restController() {
		//pointcut
	}

	@SneakyThrows
	@Around("controller() || restController()")
	public SiebelDTO logMethodCall(ProceedingJoinPoint pjp) {
		for (Object arg : pjp.getArgs()) {
			if (arg instanceof SiebelDTO) {
				SiebelDTO dto = (SiebelDTO) arg;
				dto.setContent(StringUtils.newStringUtf8(Base64.decodeBase64(dto.getContent())));
				dto.setPropertySet(xmlConverter
						.convertToPropSet(dto.getContent()));
			}
		}
		Object result = pjp.proceed();
		if (result instanceof SiebelDTO) {
			SiebelDTO dto = (SiebelDTO) result;
			dto.setContent(Base64
					.encodeBase64String(StringUtils.getBytesUtf8(xmlConverter.convertToXml(dto.getPropertySet()))));
			return dto;
		}
		return null;
	}
}
