package br.com.tech.challenge.videomanagementservice.usecase;

import br.com.tech.challenge.videomanagementservice.domain.Video;

public interface NotificationCreatedGateway {
    void notification(Video video);

}
