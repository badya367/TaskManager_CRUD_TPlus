package org.tplus.taskManager.taskManager_crud.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;
import org.tplus.taskManager.taskManager_crud.kafka.KafkaClientProducer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки Kafka-клиентов (потребителей и продюсеров).
 * <p>
 * Содержит создание фабрик для KafkaListener, KafkaTemplate и ProducerFactory.
 * Настройки потребителя и продюсера берутся из application.properties.
 * </p>
 *
 * <p>Аннотация {@code @Configuration} указывает, что это конфигурационный класс Spring.</p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-03
 */
@Slf4j
@Configuration
public class KafkaConfig {

    /**
     * Идентификатор группы Kafka.
     */
    @Value("${t_plus_tasks_name}")
    private String groupId;

    /**
     * Адреса брокеров Kafka.
     */
    @Value("${t_plus_tasks.kafka.localhost}")
    private String servers;

    /**
     * Таймаут сессии для потребителя Kafka.
     */
    @Value("${t_plus_tasks.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;

    /**
     * Максимальный размер данных, которые потребитель может получить от одного раздела.
     */
    @Value("${t_plus_tasks.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;

    /**
     * Максимальное количество записей за один poll.
     */
    @Value("${t_plus_tasks.kafka.max.poll.records:1}")
    private String maxPollRecords;

    /**
     * Максимальный интервал между poll'ами.
     */
    @Value("${t_plus_tasks.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalsMs;

    /**
     * Топик по умолчанию для продюсера.
     */
    @Value("${t_plus_tasks.default_topic}")
    private String clientTopic;

    /**
     * Создает ConsumerFactory для обработки сообщений типа T.
     *
     * @param dtoClass класс ожидаемого типа сообщения
     * @param <T>      тип сообщения
     * @return фабрика для создания Kafka-потребителей
     */
    @Bean
    public <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> dtoClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, dtoClass.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<T> deserializer = new JsonDeserializer<>(dtoClass);
        deserializer.configure(props, false);

        DefaultKafkaConsumerFactory<String, T> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        factory.setValueDeserializer(deserializer);

        return factory;
    }

    /**
     * Создает фабрику контейнеров для слушателей Kafka.
     *
     * @param consumerFactory фабрика потребителей
     * @param <T>             тип сообщений
     * @return фабрика контейнеров
     */
    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    /**
     * Настраивает обработчик ошибок для потребителей Kafka.
     *
     * @return обработчик ошибок
     */
    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error("RetryListeners message = {}, offset = {}, deliveryAttempt = {}",
                    ex.getMessage(), record.offset(), deliveryAttempt);
        });
        return handler;
    }

    /**
     * Настраивает параметры фабрики контейнеров.
     *
     * @param consumerFactory фабрика потребителей
     * @param factory         контейнерная фабрика
     * @param <T>             тип сообщения
     */
    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    /**
     * Создает KafkaTemplate для отправки сообщений.
     *
     * @param producerFactory фабрика продюсеров
     * @param <T>             тип сообщений
     * @return KafkaTemplate для отправки
     */
    @Bean("client")
    public <T> KafkaTemplate<String, T> kafkaTemplate(@Qualifier("producerClientFactory") ProducerFactory<String, T> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Создает KafkaClientProducer, если включена настройка t1.kafka.producer.enable.
     *
     * @param template KafkaTemplate для отправки сообщений о статусе задачи
     * @return экземпляр KafkaClientProducer
     */
    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate<String, TaskStatusUpdateDto> template) {
        template.setDefaultTopic(clientTopic);
        return new KafkaClientProducer(template);
    }

    /**
     * Создает фабрику потребителей для TaskStatusUpdateDto.
     *
     * @return фабрика потребителей
     */
    @Bean("consumerListenerFactory")
    public ConsumerFactory<String, TaskStatusUpdateDto> consumerListenerFactory() {
        return createConsumerFactory(TaskStatusUpdateDto.class);
    }

    /**
     * Создает фабрику продюсеров.
     *
     * @param <T> тип отправляемых сообщений
     * @return фабрика продюсеров
     */
    @Bean
    public <T> ProducerFactory<String, T> producerClientFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
