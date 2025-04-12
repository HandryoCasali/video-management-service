
resource "aws_lb_target_group" "video_mgmt_tg" {
  name     = "video-mgmt-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = var.vpc_id
  target_type = "ip"
  health_check {
    path = "/video-management/health"
  }
}

resource "aws_lb_listener_rule" "video_mgmt_rule" {
  listener_arn = data.aws_lb_listener.lb-listener.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.video_mgmt_tg.arn
  }

  condition {
    path_pattern {
      values = ["/video-management/*"]
    }
  }
}
