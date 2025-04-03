package br.com.tech.challenge.videomanagementservice.dataprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class SqsPublisherService {

    private final SqsClient sqsClient;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendMessage(String message) {
        System.out.println(queueUrl);
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();

        sqsClient.sendMessage(sendMessageRequest);
    }
}