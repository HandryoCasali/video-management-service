
variable "alb_listener_arn" {}
variable "alb_sg_id" {}
variable "alb_dns" {}
variable "subnet_ids" {
  type = list(string)
}
variable "vpc_id" {}
variable "image_url" {}
variable "region" {}
variable "spring_profile" {}
variable "dynamo_table" {}
variable "video_uploaded_queue_name" {}
variable "notification_queue_name" {}
variable "labsrole_arn" {}
variable "video_cluster_name" {}