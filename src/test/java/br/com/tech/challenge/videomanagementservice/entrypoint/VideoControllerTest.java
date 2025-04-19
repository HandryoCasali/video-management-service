package br.com.tech.challenge.videomanagementservice.entrypoint;

import br.com.tech.challenge.videomanagementservice.testUtils.VideoFactory;
import br.com.tech.challenge.videomanagementservice.dto.CreateVideoDto;
import br.com.tech.challenge.videomanagementservice.dto.UpdateVideoDto;
import br.com.tech.challenge.videomanagementservice.dto.VideoDto;
import br.com.tech.challenge.videomanagementservice.usecase.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {
    @Mock
    private VideoService service;
    @InjectMocks
    private VideoController controller;

    @Test
    void test_add(){
        CreateVideoDto dto = new CreateVideoDto("userId", "1234", "fileName", "videoUrl");

        var response = controller.add(dto);

        assertAll(
                ()-> verify(service, times(1)).create(dto),
                ()-> assertEquals(HttpStatus.CREATED, response.getStatusCode())
            );
    }

    @Test
    void test_getByUserId(){
        var videos = List.of(
                VideoFactory.createVideo(),
                VideoFactory.createVideo()
        );
        when(service.findAllByUsuarioId("user123")).thenReturn(videos);
        var response = controller.getByUserId("user123");
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertEquals(2, response.getBody().size())
        );
    }

    @Test
    void test_getById(){
        var video = VideoFactory.createVideo();
        when(service.findByUsuarioIdVideoId("userId", "videoId")).thenReturn(video);

        ResponseEntity<VideoDto> response = controller.getById("videoId", "userId");

        assertAll(
                ()-> verify(service, times(1)).findByUsuarioIdVideoId("userId", "videoId"),
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertEquals("userId", response.getBody().usuarioId())
        );
    }

    @Test
    void test_update() throws JsonProcessingException {
        UpdateVideoDto dto = new UpdateVideoDto("userId", "videoId", "zipPath");

        var response = controller.update(dto);

        assertAll(
                ()-> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
                ()-> verify(service, times(1)).update("userId", "videoId", "zipPath")
        );
    }

    @Test
    void test_updateWhenThrowException() throws JsonProcessingException {
        UpdateVideoDto dto = new UpdateVideoDto("userId", "videoId", "zipPath");
        JsonProcessingException  exception= new JsonProcessingException("Erro ao processar JSON") {};
        doThrow(exception).when(service).update("userId", "videoId", "zipPath");
        assertThrows(JsonProcessingException.class, ()-> controller.update(dto));

    }
}
