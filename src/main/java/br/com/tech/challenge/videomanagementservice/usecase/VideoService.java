package br.com.tech.challenge.videomanagementservice.usecase;

import br.com.tech.challenge.videomanagementservice.dataprovider.SqsNotificationPublish;
import br.com.tech.challenge.videomanagementservice.dataprovider.SqsVideoUploadedPublish;
import br.com.tech.challenge.videomanagementservice.dataprovider.VideoRepository;
import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;
import br.com.tech.challenge.videomanagementservice.dto.CreateVideoDto;
import br.com.tech.challenge.videomanagementservice.mapper.VideoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository repository;
    private final NotificationGateway notificationGateway;
    private final NotificationCreatedGateway notificationCreatedGateway;

    public void create(CreateVideoDto createVideoDto){
        repository.findByUsuarioIdAndVideoId(createVideoDto.usuarioId(), createVideoDto.videoId())
                .ifPresent(c->{
                             throw new RuntimeException("Video de id: "+createVideoDto.videoId()+" já adicionado");
                });

        var video = VideoMapper.dtoToDomain(createVideoDto);
        save(video);
    }

    public void update(String usuarioId, String videoId, String zipPath) throws JsonProcessingException {
        var video =  repository.findByUsuarioIdAndVideoId(usuarioId, videoId).orElseThrow(()->
                new RuntimeException("Video de id: "+videoId+" não encontrado"));

        VideoStatus status = StringUtils.hasText(zipPath)? VideoStatus.CONCLUIDO : VideoStatus.ERRO;
        video.setStatus(status);
        video.setUpdatedAt(LocalDateTime.now());

        repository.save(video);
        notificationGateway.notification(video);
    }

    public List<Video> findAllByUsuarioId(String usuarioId){
        return repository.findAllByUsuarioId(usuarioId);
    }

    public Video findAllByUsuarioIdVideoId(String usuarioId, String videoId){
        return repository.findByUsuarioIdAndVideoId(usuarioId, videoId).orElseThrow(
                        ()-> new RuntimeException("Video de id: "+videoId+ " não encontrado")
                );
    }

    private void save(Video video){
        try {
            repository.save(video);
            notificationGateway.notification(video);
            notificationCreatedGateway.notification(video);
        } catch (Exception e){
            System.out.println(e.getMessage());
            repository.deleteVideo(video);
            throw new RuntimeException(e);
        }
    }

}
