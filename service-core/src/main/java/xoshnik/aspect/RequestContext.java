package xoshnik.aspect;

import javax.annotation.ManagedBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;
import xoshnik.dto.SiebelDTO;

@Getter
@Setter(AccessLevel.PACKAGE)
@ManagedBean
@RequestScope
public class RequestContext {

	private SiebelDTO siebelDTO;
}