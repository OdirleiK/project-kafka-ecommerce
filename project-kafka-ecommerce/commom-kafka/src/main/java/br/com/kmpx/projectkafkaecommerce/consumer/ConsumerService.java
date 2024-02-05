package br.com.kmpx.projectkafkaecommerce.consumer;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.Message;

public interface ConsumerService<T> {
	String getConsumerGroup();
	String getTopic();
	void parse(ConsumerRecord<String, Message<T>> record) throws IOException;
}
