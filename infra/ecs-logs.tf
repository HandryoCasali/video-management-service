resource "aws_cloudwatch_log_group" "ecs_video_management" {
  name              = "/ecs/video-management"
  retention_in_days = 7
}