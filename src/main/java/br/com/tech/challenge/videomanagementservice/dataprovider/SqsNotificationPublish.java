package br.com.tech.challenge.videomanagementservice.dataprovider;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.mapper.VideoMapper;
import br.com.tech.challenge.videomanagementservice.usecase.NotificationGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class SqsNotificationPublish implements NotificationGateway {

    private final SqsClient sqsClient;

    @Value("${sqs.notification.queue-url}")
    private String queueUrl;

    @Override
    public void notification(Video video) {
        try{
            String message = VideoMapper.domainToJson(video);
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}