
resource "aws_ecs_task_definition" "video_mgmt_task" {
  family                   = "video-management"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = var.labsrole_arn
  task_role_arn            = var.labsrole_arn
  depends_on = [
    aws_cloudwatch_log_group.ecs_video_management
  ]

  container_definitions = jsonencode([
    {
      name      = "video-management"
      image     = var.image_url
      essential = true
      portMappings = [{
        containerPort = 8080
        protocol      = "tcp"
      }]
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = var.spring_profile
        },
        {
          name  = "DYNAMO_TABLE_NAME"
          value = var.dynamo_table
        },
        {
          name  = "SQS_QUEUE_URL"
          value = data.aws_sqs_queue.notification.url
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/video-management"
          awslogs-region        = var.region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_ecs_cluster" "video_cluster" {
  name = "video-cluster"
}

resource "aws_ecs_service" "video_mgmt_service" {
  name            = "video-management"
  cluster         = aws_ecs_cluster.video_cluster.id
  task_definition = aws_ecs_task_definition.video_mgmt_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets         = var.subnet_ids
    security_groups = [aws_security_group.app_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.video_mgmt_tg.arn
    container_name   = "video-management"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener_rule.video_mgmt_rule]
}
