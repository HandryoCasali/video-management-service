package br.com.tech.challenge.videomanagementservice.dataprovider;

import br.com.tech.challenge.videomanagementservice.domain.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepository {
    private final DynamoDbTable<Video> videoTable;
    public VideoRepository(DynamoDbEnhancedClient enhancedClient, @Value("${spring.cloud.aws.dynamodb.table-name}") String tableName) {
        this.videoTable = enhancedClient.table(tableName, TableSchema.fromBean(Video.class));
    }

    public void save(Video video) {
        videoTable.putItem(video);
    }

    public List<Video> findAllByUsuarioId(String usuarioId) {
        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(usuarioId)
                        .build()))
                .build();

        return videoTable.query(query)
                .items()
                .stream()
                .toList();
    }

    public Optional<Video> findByUsuarioIdAndVideoId(String usuarioId, String videoId) {
        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(usuarioId)
                        .sortValue(videoId)
                        .build()))
                .build();

        var video = videoTable.query(query)
                .items()
                .stream();

        return video
                .findFirst();

    }

    public void deleteVideo(Video video){
        videoTable.deleteItem(video);
    }
}
