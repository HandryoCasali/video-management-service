package br.com.tech.challenge.videomanagementservice.usecase;

import br.com.tech.challenge.videomanagementservice.dataprovider.VideoRepository;
import br.com.tech.challenge.videomanagementservice.domain.Video;
import br.com.tech.challenge.videomanagementservice.domain.VideoStatus;
import br.com.tech.challenge.videomanagementservice.dto.CreateVideoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository repository;

    @Mock
    private NotificationGateway notificationGateway;

    @Mock
    private NotificationCreatedGateway notificationCreatedGateway;

    @InjectMocks
    private VideoService videoService;
    @Test
    void givenNewVideo_whenCreate_thenShouldSaveAndNotify() {
        // given
        CreateVideoDto createDto = new CreateVideoDto("user-1", "video-1", "video.mp4", "http://video-url.com");
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1")).thenReturn(Optional.empty());

        doNothing().when(repository).save(any(Video.class));
        doNothing().when(notificationGateway).notification(any(Video.class));
        doNothing().when(notificationCreatedGateway).notification(any(Video.class));

        // when
        videoService.create(createDto);

        // then
        verify(repository).save(any(Video.class));
        verify(notificationGateway).notification(any(Video.class));
        verify(notificationCreatedGateway).notification(any(Video.class));
    }

    @Test
    void givenExistingVideo_whenCreate_thenThrowException() {
        CreateVideoDto createDto = new CreateVideoDto("user-1", "video-1", "video.mp4", "http://video-url.com");
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1"))
                .thenReturn(Optional.of(mock(Video.class)));

        assertThrows(RuntimeException.class, () -> videoService.create(createDto));
    }

    @Test
    void givenValidVideo_whenUpdateWithZipPath_thenSetStatusConcluidoAndNotify() throws Exception {
        Video video = new Video();
        video.setUsuarioId("user-1");
        video.setVideoId("video-1");
        video.setStatus(VideoStatus.RECEBIDO);

        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1"))
                .thenReturn(Optional.of(video));
        doNothing().when(repository).save(any(Video.class));
        doNothing().when(notificationGateway).notification(any(Video.class));

        videoService.update("user-1", "video-1", "zip-path");

        assertEquals(VideoStatus.CONCLUIDO, video.getStatus());
        assertEquals("zip-path", video.getZipPath());
        verify(repository).save(video);
        verify(notificationGateway).notification(video);
    }

    @Test
    void givenValidVideo_whenUpdateWithoutZipPath_thenSetStatusErroAndNotify() throws Exception {
        Video video = new Video();
        video.setUsuarioId("user-1");
        video.setVideoId("video-1");
        video.setStatus(VideoStatus.RECEBIDO);

        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1"))
                .thenReturn(Optional.of(video));
        doNothing().when(repository).save(any(Video.class));
        doNothing().when(notificationGateway).notification(any(Video.class));

        videoService.update("user-1", "video-1", "");

        assertEquals(VideoStatus.ERRO, video.getStatus());
        assertEquals("", video.getZipPath());
        verify(repository).save(video);
        verify(notificationGateway).notification(video);
    }

    @Test
    void givenVideoDoesNotExist_whenUpdate_thenThrowException() {
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> videoService.update("user-1", "video-1", "zip-path"));
    }

    @Test
    void givenUserId_whenFindAllByUsuarioId_thenReturnVideos() {
        List<Video> videos = List.of(new Video(), new Video());
        when(repository.findAllByUsuarioId("user-1")).thenReturn(videos);

        List<Video> result = videoService.findAllByUsuarioId("user-1");

        assertEquals(2, result.size());
        verify(repository).findAllByUsuarioId("user-1");
    }

    @Test
    void givenVideoExists_whenFindByUsuarioIdVideoId_thenReturnVideo() {
        Video video = new Video();
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1")).thenReturn(Optional.of(video));

        Video result = videoService.findByUsuarioIdVideoId("user-1", "video-1");

        assertEquals(video, result);
    }

    @Test
    void givenVideoNotFound_whenFindByUsuarioIdVideoId_thenThrowException() {
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> videoService.findByUsuarioIdVideoId("user-1", "video-1"));
    }

    @Test
    void givenErrorOnSave_whenCreate_thenDeleteAndThrowException() {
        CreateVideoDto createDto = new CreateVideoDto("user-1", "video-1", "video.mp4", "http://video-url.com");
        when(repository.findByUsuarioIdAndVideoId("user-1", "video-1")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("fail")).when(repository).save(any(Video.class));

        assertThrows(RuntimeException.class, () -> videoService.create(createDto));
        verify(repository).deleteVideo(any(Video.class));
    }
}