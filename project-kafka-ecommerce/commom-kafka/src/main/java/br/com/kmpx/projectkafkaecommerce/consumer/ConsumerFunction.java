package br.com.kmpx.projectkafkaecommerce.consumer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.Message;

public interface ConsumerFunction<T> {
	void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
