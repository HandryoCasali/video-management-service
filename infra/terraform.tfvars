alb_listener_arn             = "arn:aws:elasticloadbalancing:us-east-1:561266514983:loadbalancer/app/shared-alb/2c4784301d8bbad1"
alb_sg_id                    = "sg-0f158052653f58c64"
alb_dns                      = "internal-shared-alb-1828749500.us-east-1.elb.amazonaws.com"
subnet_ids                   = ["subnet-02dfefca38889217f", "subnet-064e90a098c98514f"]
vpc_id                       = "vpc-00540ae73d895d3d3"

region                       = "us-east-1"
spring_profile               = "prod"
dynamo_table                 = "videos"
video_uploaded_queue_name    = "video_uploaded"
notification_queue_name      = "notification"
labsrole_arn                 = "arn:aws:iam::561266514983:role/LabRole"
video_cluster_name           = "video-cluster"