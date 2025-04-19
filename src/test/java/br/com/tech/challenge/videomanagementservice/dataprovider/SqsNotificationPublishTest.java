package br.com.tech.challenge.videomanagementservice.dataprovider;

import br.com.tech.challenge.videomanagementservice.testUtils.VideoFactory;
import br.com.tech.challenge.videomanagementservice.domain.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SqsNotificationPublishTest {

    @Mock
    private SqsClient sqsClient;
    private final String QUEUE_URL = "https://fake-queue-url";
    @InjectMocks
    private SqsNotificationPublish notificationPublish;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(notificationPublish,"queueUrl", QUEUE_URL);
    }

    @Test
    void shouldSendMessageToSqs() {
        Video video = VideoFactory.createVideo();

        notificationPublish.notification(video);

        var captor = forClass(SendMessageRequest.class);

        verify(sqsClient).sendMessage(captor.capture());
        SendMessageRequest requestSent = captor.getValue();

        assertEquals("https://fake-queue-url", requestSent.queueUrl());
        assertTrue(requestSent.messageBody().contains("userId"));
    }
}
