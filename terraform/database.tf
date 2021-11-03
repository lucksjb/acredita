/****
  PAGINA DO MODULO DB :
  https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest  

  EXEMPLO COMPLETO MYSQL 
  https://github.com/terraform-aws-modules/terraform-aws-rds/blob/master/examples/complete-mysql/main.tf

  Documentação oficial:
  https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest
  
****/

module "db" {
  source  = "terraform-aws-modules/rds/aws"
  # version = "3.3.0"
  version = "~> 3.0"

  # insert the 29 required variables here
  identifier           = "luck-rds"    // nome do servidor rds
  engine               = "mysql"       // tipo de banco 
  engine_version       = "8.0.23"      // versao do mysql 
  major_engine_version = "8.0"         // duas primeiras casas da versao do mysql 
  family               = "mysql8.0"    // familia do banco de dados  https://github.com/terraform-aws-modules/terraform-aws-rds/blob/v3.3.0/examples/complete-mysql/main.tf
  instance_class       = "db.t2.micro" // tipo de maquina que vai estar instalado o servidor 
  allocated_storage    = "20"          // tamanho do hd em GB

  name     = "dados_db" // nome do banco de dados
  username = "admin"
  password = "Lck123**"
  port     = 3306

  // backup 
  maintenance_window = "Thu:03:30-Thu:05:30" // janela de manutençao do banco 
  backup_window      = "05:30-06:30"         // horario em que sera feito o backup 
  storage_type       = "gp2"                 // tipo de media para backup 
  multi_az           = false               // se vai ter replicas paradas em outras zonas de disponibilidade

  skip_final_snapshot = true
  deletion_protection = false

  // segurança e rede 
  vpc_security_group_ids = [aws_security_group.permite-database.id]

  // cria uma lista dos id de todas as subnets 
  // subnet_ids             = flatten(chunklist(aws_subnet.redes_privadas.*.id, 1))
  subnet_ids             = flatten(chunklist(aws_subnet.redes_privadas.*.id, 1))
 // subnet_ids = ["subnet-08855b31c0c787d6e", "subnet-0d3d42ac5f1d109ba", "subnet-0df741433229f7af9"]

}

output "subnets" {
  value = join(",", flatten(chunklist(aws_subnet.redes_privadas.*.id, 1)) )
}
