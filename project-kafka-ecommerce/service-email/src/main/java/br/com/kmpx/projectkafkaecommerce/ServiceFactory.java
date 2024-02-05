package br.com.kmpx.projectkafkaecommerce;

public interface ServiceFactory<T> {
	ConsumerService<T> create();
}
