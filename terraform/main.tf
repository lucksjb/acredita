/******
    Documentação do terraform:
    https://www.terraform.io/docs/language/state/index.html
*****/

provider "aws" { 
    region = "us-east-1"
    shared_credentials_file = "C/Users/Luck/.aws/"
    profile = "terraform"
}	
