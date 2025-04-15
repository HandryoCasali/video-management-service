package br.com.tech.challenge.videomanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateVideoDto(
        @NotBlank String userId,
        @NotBlank String videoId,
        String zipPath
){ }
