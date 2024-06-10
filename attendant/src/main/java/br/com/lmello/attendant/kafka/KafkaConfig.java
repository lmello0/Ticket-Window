package br.com.lmello.attendant.kafka;

import br.com.lmello.common.customer.ProcessedCustomerDTO;
import br.com.lmello.common.customer.UnprocessedCustomerDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public Map<String, Object> produceConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

        return props;
    }

    @Bean
    public ProducerFactory<String, ProcessedCustomerDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(produceConfig());
    }

    @Bean
    public ConsumerFactory<String, UnprocessedCustomerDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(produceConfig(), new StringDeserializer(), new JsonDeserializer<>(UnprocessedCustomerDTO.class));
    }

    @Bean
    public KafkaListenerContainerFactory<?> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UnprocessedCustomerDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public KafkaTemplate<String, ProcessedCustomerDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
