package br.com.kmpx.projectkafkaecommerce.consumer;

public interface ServiceFactory<T> {
	ConsumerService<T> create();
}
