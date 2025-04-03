package br.com.tech.challenge.videomanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateVideoDto(
        @NotBlank String status
){ }
