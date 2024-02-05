package br.com.kmpx.projectkafkaecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {
	String getConsumerGroup();
	String getTopic();
	void parse(ConsumerRecord<String, Message<Email>> record);
}
