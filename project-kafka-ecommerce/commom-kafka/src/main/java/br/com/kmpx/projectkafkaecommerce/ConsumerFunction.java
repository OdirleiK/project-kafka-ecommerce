package br.com.kmpx.projectkafkaecommerce;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
	void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
