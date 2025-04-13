package br.com.tech.challenge.videomanagementservice.dto;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;

import java.time.LocalDateTime;

public record VideoDto(
        String usuarioId,
        String videoId,
        String videoFileName,
        VideoStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public VideoDto(Video video){
        this(
            video.getUsuarioId(),
            video.getVideoId(),
            video.getVideoFileName(),
            video.getStatus(),
            video.getCreatedAt(),
            video.getUpdatedAt()
        );
    }
}
