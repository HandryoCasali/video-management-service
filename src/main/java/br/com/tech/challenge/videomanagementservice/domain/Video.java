package br.com.tech.challenge.videomanagementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Video {

    private String usuarioId;
    private String videoId;
    private String videoFileName;
    private String videoUrl;
    private String zipPath;
    private VideoStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("usuario_id")
    public String getUsuarioId(){
        return usuarioId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("video_id")
    public String getVideoId() {
        return videoId;
    }
}
