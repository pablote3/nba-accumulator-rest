package com.rossotti.basketball.app.gateway;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.messaging.Message;

public class GameCorrelationStrategy implements CorrelationStrategy {
	// key put into header and used by correlation strategy to link several Message<Product> to appropriate oneMessage<Order>
	public static final String CORRELATION_KEY = "orderId";
	// key of parameter meaning if splitted element is the last element needed to aggregate
	public static final String LAST_KEY = "isLast";
	@Override
	public Object getCorrelationKey(Message<?> message) {
		if (!message.getHeaders().containsKey(CORRELATION_KEY)) {
			throw new IllegalStateException("Message splitted by order splitter must contain orderId header. Present headers " + "were: "+ message.getHeaders());
		}
		return message.getHeaders().get(CORRELATION_KEY);
	}
}