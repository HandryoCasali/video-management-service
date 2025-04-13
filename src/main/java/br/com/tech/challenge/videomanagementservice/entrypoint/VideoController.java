package br.com.tech.challenge.videomanagementservice.entrypoint;

import br.com.tech.challenge.videomanagementservice.dto.CreateVideoDto;
import br.com.tech.challenge.videomanagementservice.dto.UpdateVideoDto;
import br.com.tech.challenge.videomanagementservice.dto.VideoDto;
import br.com.tech.challenge.videomanagementservice.usecase.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid CreateVideoDto dto){
        videoService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VideoDto>> getByUserId(@RequestHeader @NotBlank String usuarioId){
        var videos = videoService.findAllByUsuarioId(usuarioId).stream().map(VideoDto::new).toList();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto> getById(@PathVariable @NotBlank String videoId, @NotBlank @RequestHeader String usuarioId){
        var video = new VideoDto(videoService.findAllByUsuarioIdVideoId(usuarioId, videoId));
        return ResponseEntity.ok(video);
    }

    @PutMapping
    public ResponseEntity<Void> update(
            @RequestBody @Valid UpdateVideoDto dto) throws JsonProcessingException {
        videoService.update(dto.userId(), dto.videoId(), dto.zipPath());
        return ResponseEntity.accepted().build();
    }
}
