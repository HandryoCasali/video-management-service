terraform {
  required_version = ">= 1.3.0"

  backend "s3" {
    bucket = "terraform-state-fiapx"
    key    = "infra-ecs-app1/terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}
