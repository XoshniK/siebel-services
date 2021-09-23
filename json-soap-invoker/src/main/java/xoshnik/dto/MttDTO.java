package xoshnik.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MttDTO {

	private String phone;

	private String date;

	private String status;

	private String callid;

	private String answer;

	@JsonProperty("order_number")
	private String orderNumber;
	@JsonProperty("request_id")
	private String requestId;

}
