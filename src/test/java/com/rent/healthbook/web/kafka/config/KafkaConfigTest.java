package com.rent.healthbook.web.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = KafkaConfig.class)
class KafkaConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testProducerFactoryBeanExists() {
        assertThat(context.getBean(ProducerFactory.class)).isNotNull();
    }

    @Test
    void testKafkaTemplateBeanExists() {
        KafkaTemplate<String, Object> kafkaTemplate = context.getBean(KafkaTemplate.class);
        assertThat(kafkaTemplate).isNotNull();
    }

    @Test
    void testProfileEventsTopicBeanExists() {
        NewTopic topic = context.getBean("profileEventsTopic", NewTopic.class);
        assertThat(topic).isNotNull();
        assertThat(topic.name()).isEqualTo("profile-events");
        assertThat(topic.numPartitions()).isEqualTo(1);
        assertThat(topic.replicationFactor()).isEqualTo((short) 1);
    }

    @Test
    void testUserValidationRequestsTopicBeanExists() {
        NewTopic topic = context.getBean("userValidationRequestsTopic", NewTopic.class);
        assertThat(topic).isNotNull();
        assertThat(topic.name()).isEqualTo("user-validation-requests");
        assertThat(topic.numPartitions()).isEqualTo(1);
        assertThat(topic.replicationFactor()).isEqualTo((short) 1);
    }
}