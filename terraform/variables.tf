variable "availability_zones" {
    description = "zonas de disponibilidade"
    type = list(string)
    default = [
        "us-east-1a",
        "us-east-1b",
        "us-east-1c"
    ]
}

# quando cria uma variavel sem valor nenhum 
# a mesma deve ser passada quando for dar o terraform apply 
# terraform apply -var "my_public_ip=172.31.208.1/32"
# se nao passar o terraform apply irá solicitar o valor da variavel 
variable "my_public_ip" {}