package br.com.tech.challenge.videomanagementservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateVideoDto(
        @NotBlank String userId,
        @NotBlank String videoId,
        String zipPath
){ }
