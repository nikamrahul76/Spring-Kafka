
# Spring Kafka Example Application

This repository demonstrates a **Spring Boot** application integrating **Apache Kafka** to provide message-based communication. It includes a producer for sending messages, a consumer for processing messages, and advanced error handling with dead-letter topics.

---

## Features

1. **Kafka Producer and Consumer**:
    - Send and consume messages from Kafka topics.
2. **Dead-Letter Topic (DLT)**:
    - Handle errors with automatic retries and fallback to dead-letter topics.
3. **Manual Acknowledgment**:
    - Process Kafka messages manually for precise control.
4. **Spring Boot Integration**:
    - Leverages Spring Kafka’s configurations and annotations for seamless integration.

---

## Project Structure

### 1. **Producer Service**
- Handles message publishing to Kafka topics using `KafkaTemplate`.

### 2. **Consumer Service**
- Consumes messages from Kafka topics with `@KafkaListener`.
- Implements dead-letter topic handling with `@DltHandler`.

### 3. **Error Handling**
- Configured with `DefaultErrorHandler` for retries and fallback to DLT.

### 4. **Topic Configuration**
- Automatically creates topics, including the dead-letter topic.

### 5. **REST Controller**
- Exposes an endpoint for sending messages to Kafka.

---

## Technologies Used

- **Spring Boot**: Framework for building Java applications.
- **Spring Kafka**: Simplifies integration with Apache Kafka.
- **Apache Kafka**: Distributed event streaming platform.
- **Java 17**: Latest LTS version for enhanced features.

---

## Setup and Run

### Prerequisites
1. **Java 17**: Ensure Java 17 or higher is installed.
2. **Apache Kafka**:
    - Install and run Kafka on `localhost:9092` (default).
    - Ensure Zookeeper is running if using standalone Kafka.
3. **Maven**: Ensure Maven is installed.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/nikamrahul76/Spring-Kafka.git
   cd Spring-Kafka
   ```

2. Update `application.yml` if necessary:
   ```yaml
   spring:
     kafka:
       bootstrap-servers: localhost:9092
   ```

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the application on `http://localhost:8080`.

---

## Configuration Details

### `application.yml`
Configuration for Kafka producer, consumer, and default topics:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      enable-auto-commit: false
      group-id: my-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    template:
      default-topic: my-topic
```

---

## API Endpoints

### 1. Produce Message
- **Endpoint**: `POST /kafka/produce`
- **Request Body**:
   ```json
   "Hello Kafka!"
   ```
- **Response**:
   ```json
   "message has been Produce on Topic my-topic"
   ```

---

## How It Works

### **Kafka Producer**
The `ProducerService` publishes messages to the Kafka topic using `KafkaTemplate`.

```java
@Service
public class ProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
```

### **Kafka Consumer**
The `ConsumerService` listens to messages on the topic using `@KafkaListener` and processes them manually with acknowledgment.

```java
@Service
public class ConsumerService {
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
    }
}
```

### **Dead-Letter Topic Handling**
If processing fails, the message is sent to a **Dead-Letter Topic (DLT)**. The `@DltHandler` processes these messages.

```java
@DltHandler
public void dltHandle(ConsumerRecord<String, String> record) {
    System.out.println("DLT message: " + record.value());
}
```

### **Error Handling**
Configured with retries and fallback to the DLT using `DefaultErrorHandler`.

```java
@Bean
public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recovered = new DeadLetterPublishingRecoverer(kafkaTemplate,
        (record, exception) -> new TopicPartition(record.topic() + ".DLT", record.partition()));
    return new DefaultErrorHandler(recovered, new FixedBackOff(1000L, 3));
}
```

---

## Example Kafka Topics

1. **Main Topic**: `my-topic`
2. **Dead-Letter Topic**: `my-topic.DLT`

---

## Contributing

Contributions are welcome! Feel free to fork the repository, make changes, and submit pull requests.

---

## Author

[**Rahul Nikam**](https://github.com/nikamrahul76)

---