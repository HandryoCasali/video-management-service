package br.com.tech.challenge.videomanagementservice.dataprovider;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.dto.UploadedVideoDto;
import br.com.tech.challenge.videomanagementservice.mapper.VideoMapper;
import br.com.tech.challenge.videomanagementservice.usecase.NotificationCreatedGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@RequiredArgsConstructor
@Service
public class SqsVideoUploadedPublish implements NotificationCreatedGateway {
    private final SqsClient sqsClient;

    @Value("${sqs.video-uploaded.queue-url}")
    private String queueUrl;

    @Override
    public void notification(Video video) {
        try{
            String message = VideoMapper.uploadedVideoToJson(new UploadedVideoDto(video.getUsuarioId(), video.getVideoId(), video.getVideoUrl()));
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}

