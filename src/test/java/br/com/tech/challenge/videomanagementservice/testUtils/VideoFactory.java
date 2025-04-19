package br.com.tech.challenge.videomanagementservice.testUtils;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;

import java.time.LocalDateTime;

public class VideoFactory {
    public static Video createVideo(){
        return new Video("userId", "videoId", "videoFileName", "videoUrl", "zipPath", VideoStatus.RECEBIDO, LocalDateTime.now(), LocalDateTime.now());
    }
}

