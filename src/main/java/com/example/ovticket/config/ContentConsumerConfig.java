package com.example.ovticket.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.WorkerStateChangeListener;
import com.amazonaws.services.kinesis.metrics.interfaces.MetricsLevel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Configuration
//@ComponentScan(
//        useDefaultFilters = false,
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ContentBusHandler.class)
//        },
//        basePackages = {"com.example"})
@EnableConfigurationProperties(ContentConsumerConfig.ConsumerProperties.class)
@Import(AWSBaseConfig.class)
@Slf4j
public class ContentConsumerConfig {

    private final String workerId;

    public ContentConsumerConfig(@Value("${HOSTNAME:}") String hostname,
                                 @Value("${USER:}") String username) {
        this.workerId = StringUtils.defaultIfEmpty(hostname, username);
    }

//    @Bean
//    Map<String, List<ContentBusHandler>> handlersByTable(List<ContentBusHandler> allHandlers) {
//        return allHandlers.stream()
//                .collect(Collectors.toMap(ContentBusHandler::forType, handler -> {
//                    ArrayList<ContentBusHandler> handlers = new ArrayList<>();
//                    handlers.add(handler);
//                    return handlers;
//                }, (existingList, nwList) -> {
//                    existingList.addAll(nwList);
//                    return existingList;
//                }));
//    }

    @Data
    @ConfigurationProperties(prefix = "aws.consumer")
    public static class ConsumerProperties {

        private String consumerGroup;

        private List<KinesisStreamProperties> kinesisStreams = new ArrayList<>();

        @Data
        static class KinesisStreamProperties {
            String streamName;

            String start = "TRIM_HORIZON";
        }
    }

    @Bean
    @ConditionalOnMissingBean
    AmazonDynamoDB amazonDynamoDB(AWSStaticCredentialsProvider credentialsProvider) {
        return AmazonDynamoDBClient.builder()
                .disableEndpointDiscovery()
                .withCredentials(credentialsProvider)
                .build();
    }

//    @Bean
//    Map<String, Worker> buildWorkers(AWSStaticCredentialsProvider credentialsProvider,
//                                     AmazonDynamoDB dynamoDBClient,
//                                     ConsumerProperties consumerProperties,
//                                     IRecordProcessorFactory contentBusDispatcherFactory) {
//
//        AmazonCloudWatch cloudWatchClient = AmazonCloudWatchClient.builder()
//                .withCredentials(credentialsProvider)
//                .withRegion(AWSBaseConfig.REGION)
//                .build();
//
//        Map<String, Worker> kinesisStreamWorkers = consumerProperties.kinesisStreams.stream()
//                .collect(
//                        Collectors.toMap(
//                                ConsumerProperties.KinesisStreamProperties::getStreamName,
//                                properties -> workerForKinesisStream(
//                                        credentialsProvider,
//                                        contentBusDispatcherFactory,
//                                        dynamoDBClient,
//                                        cloudWatchClient,
//                                        consumerProperties.consumerGroup + "." + properties.getStreamName(),
//                                        properties.getStreamName(),
//                                        properties.getStart(),
//                                        null)
//                        ));
//
//        kinesisStreamWorkers.forEach(this::startWorker);
//        return kinesisStreamWorkers;
//    }

//    @Bean
//    ContentBusDispatcherFactory contentBusDispatcherFactory(StreamMessageConverter streamMessageConverter,
//                                                            Map<String, List<ContentBusHandler>> handlersByTable) {
//        log.warn("Found {} ContentBusHandlers", handlersByTable.values().stream().mapToLong(Collection::size).sum());
//        return new ContentBusDispatcherFactory(streamMessageConverter, handlersByTable);
//    }

    public Worker workerForKinesisStream(AWSStaticCredentialsProvider credentialsProvider,
                                         IRecordProcessorFactory contentBusDispatcherFactory,
                                         AmazonDynamoDB dynamoDBClient,
                                         AmazonCloudWatch cloudWatchClient,
                                         String consumerName,
                                         String streamName,
                                         @Nullable WorkerStateChangeListener workerStateChangeListener) {
        KinesisClientLibConfiguration consumerConfiguration = new KinesisClientLibConfiguration(
                consumerName,
                streamName,
                credentialsProvider,
                workerId
        )
                .withRegionName(AWSBaseConfig.REGION)
                .withMetricsLevel(MetricsLevel.NONE)
                .withBillingMode(BillingMode.PAY_PER_REQUEST);

        return new Worker.Builder()
                .recordProcessorFactory(contentBusDispatcherFactory)
                .config(consumerConfiguration)
                .dynamoDBClient(dynamoDBClient)
                .cloudWatchClient(cloudWatchClient)
                .workerStateChangeListener(workerStateChangeListener)
                .build();
    }


    private void startWorker(String label, Worker worker) {
        log.info("Starting Scheduler");
        Thread schedulerThread = new Thread(worker);
        schedulerThread.setDaemon(true);
        schedulerThread.setName(label);
        schedulerThread.start();
    }
}
