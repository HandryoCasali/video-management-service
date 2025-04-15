package br.com.tech.challenge.videomanagementservice.mapper;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;
import br.com.tech.challenge.videomanagementservice.dto.CreateVideoDto;
import br.com.tech.challenge.videomanagementservice.dto.UploadedVideoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

public class VideoMapper {
    public static Video dtoToDomain(CreateVideoDto dto){
        return new Video(
            dto.usuarioId(),
            dto.videoId(),
            dto.videoFileName(),
            dto.videoUrl(),
            null,
            VideoStatus.RECEBIDO,
            LocalDateTime.now(),
            null
        );
    }

    public static String domainToJson(Video video) throws JsonProcessingException {
        return toJson(video);
    }

    public static String uploadedVideoToJson(UploadedVideoDto uploadedVideoDto) throws JsonProcessingException {
        return toJson(uploadedVideoDto);
    }

    private static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(obj);
    }
}