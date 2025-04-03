package br.com.tech.challenge.videomanagementservice.usecase;

import br.com.tech.challenge.videomanagementservice.dataprovider.SqsPublisherService;
import br.com.tech.challenge.videomanagementservice.dataprovider.VideoRepository;
import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository repository;
    private final SqsPublisherService sqsPublisherService;

    public void save(String usuarioId, String videoId){
        var optVideo = repository.findByUsuarioIdAndVideoId(usuarioId, videoId);
        optVideo.ifPresent(
                (c)-> {throw new RuntimeException("Video de id: "+videoId+" já adicionado");});
        var video = new Video();
        video.setUsuarioId(usuarioId);
        video.setVideoId(videoId);
        video.setCreatedAt(LocalDateTime.now());
        video.setStatus(VideoStatus.RECEBIDO);
        saveAndNotification(video);
    }

    public void update(String usuarioId, String videoId, String status){
        var video =  repository.findByUsuarioIdAndVideoId(usuarioId, videoId).orElseThrow(()->
                new RuntimeException("Video de id: "+videoId+" não encontrado"));
        video.setStatus(VideoStatus.valueOf(status));
        video.setUpdatedAt(LocalDateTime.now());
        saveAndNotification(video);
    }

    public List<Video> findAllByUsuarioId(String usuarioId){
        return repository.findAllByUsuarioId(usuarioId);
    }

    public Video findAllByUsuarioIdVideoId(String usuarioId, String videoId){
        return repository.findByUsuarioIdAndVideoId(usuarioId, videoId).orElseThrow(
                        ()-> new RuntimeException("Video de id: "+videoId+ " não encontrado")
                );
    }

    private void saveAndNotification(Video video){
        try {
            repository.save(video);
            sqsPublisherService.sendMessage(video.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
            repository.deleteVideo(video);
            throw new RuntimeException(e);
        }
    }
}
