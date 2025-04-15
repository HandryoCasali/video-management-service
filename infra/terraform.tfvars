alb_listener_arn             = "arn:aws:elasticloadbalancing:us-east-1:010613378779:loadbalancer/app/shared-alb/d6f5464394ebecc0"
alb_sg_id                    = "sg-0cc8d977c6fb1913f"
alb_dns                      = "internal-shared-alb-946186929.us-east-1.elb.amazonaws.com"
subnet_ids                   = ["subnet-057134789571f98ba", "subnet-0a6ce4c526726b259"]
vpc_id                       = "vpc-0fbcd731011399125"
image_url                    = "010613378779.dkr.ecr.us-east-1.amazonaws.com/video-management:1.0"
region                       = "us-east-1"
spring_profile               = "prod"
dynamo_table                 = "videos"
video_uploaded_queue_name    = "video_uploaded"
notification_queue_name      = "notification"
labsrole_arn                 = "arn:aws:iam::010613378779:role/LabRole"
video_cluster_name           = "video-cluster"