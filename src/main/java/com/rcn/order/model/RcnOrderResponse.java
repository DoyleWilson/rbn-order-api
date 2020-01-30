package com.rcn.order.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
public class RcnOrderResponse {

	private String farmId;
	private String responseMsg;
}
