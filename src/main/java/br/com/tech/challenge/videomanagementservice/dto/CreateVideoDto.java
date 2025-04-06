package br.com.tech.challenge.videomanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVideoDto(
        @NotBlank String usuarioId,
        @NotBlank String videoId,
        @NotBlank String videoFileName,
        @NotBlank String videoUrl
){ }
