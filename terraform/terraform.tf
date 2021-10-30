terraform {
  backend "s3" {
      bucket = "lucksjb-terraform-state"
      key = "terraform-state-my-application"
      region = "us-east-1"    
      profile = "terraform"
  }
}