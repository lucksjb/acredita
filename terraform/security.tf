resource "aws_security_group" "permite-ssh" {
  vpc_id = aws_vpc.vpc_principal.id
  name   = "luck_my_security_group_permite_ssh"

    ingress = [ {
      // recebe esse ip via variable:terraform apply -var "my_public_ip=$MY_PUBLIC_IP/32"
      // onde o  MY_PUBLIC_IP="$(curl -s ipinfo.io/ip)"
      cidr_blocks =  ["${var.my_public_ip}"] 
      
      description = "permitir acesso SSH as instancias ec2 que estiverem nesse security_group"
      from_port = 22
      to_port = 22
      protocol = "tcp"
      self = true
      ipv6_cidr_blocks = []
      prefix_list_ids = []
      security_groups = []
    } ]
}

resource "aws_security_group" "permite-database" {
  vpc_id = aws_vpc.vpc_principal.id
  name   = "luck_my_security_group_permite_acesso_banco_de_dados"

    ingress = [ {
      // cidr_blocks = [ "131.100.139.88/32" ] // ip ta fixo (meuip.com) o ideal é ser dinamico

      // recebe esse ip via variable:terraform apply -var "my_public_ip=$MY_PUBLIC_IP/32"
      // onde o  MY_PUBLIC_IP="$(curl -s ipinfo.io/ip)"
      cidr_blocks =  ["${var.my_public_ip}"] 
      description = "permitir acesso ao servidor de banco de dados a todos que estiverem nesse security_group"
      from_port = 3306
      to_port = 3306
      protocol = "tcp"
      self = true  // todos que estiverem nesse security group terão acesso a essas regras
      ipv6_cidr_blocks = []
      prefix_list_ids = []
      security_groups = []
    } ]
}

// regra para permitir que as instancias EC2 saiam para a internet
// (pra conseguir baixar as imagens docker)
resource "aws_security_group" "permite-acessar-internet" {
  vpc_id = "${aws_vpc.vpc_principal.id}"
  name = "luck_permite_acessar_internet"
  egress = [ {
    cidr_blocks = [ "0.0.0.0/0" ] 
    description = ""
    from_port = 0 // porta 0 permite acessar de qualquer porta 
    to_port = 0 // porta 0 permite acessar até qualquer porta 
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    protocol = "-1"  // qualquer protocolo
    security_groups = [ ]
    self = false 
  } ]
}


// regra para permitir que as instancias EC2 se comuniquem entre si 
// nas portas do swarm 
resource "aws_security_group" "cluster_communication" {
  vpc_id = "${aws_vpc.vpc_principal.id}"
  name = "luck_cluster_communication"
  ingress = [ {
    from_port = 2377
    to_port = 2377
    protocol = "tcp"
    self = true

    cidr_blocks = [ ]
    description = ""
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    security_groups = [ ]
  },
  {
    from_port = 7946
    to_port = 7946
    protocol = "tcp"
    self = true

    cidr_blocks = [ ]
    description = ""
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    security_groups = [ ]
  },
  {
    from_port = 7946
    to_port = 7946
    protocol = "udp"
    self = true

    cidr_blocks = [ ]
    description = ""
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    security_groups = [ ]
  },
  {
    from_port = 4789
    to_port = 4789
    protocol = "udp"
    self = true

    cidr_blocks = [ ]
    description = ""
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    security_groups = [ ]
  }]

  egress = [ {
    cidr_blocks = [ "0.0.0.0/0" ] 
    description = ""
    from_port = 0 // porta 0 permite acessar de qualquer porta 
    to_port = 0 // porta 0 permite acessar até qualquer porta 
    ipv6_cidr_blocks = [ ]
    prefix_list_ids = [ ]
    protocol = "-1"  // qualquer protocolo
    security_groups = [ ]
    self = false 
  } ]
}


resource "aws_security_group" "allow_portainer" {
  vpc_id = aws_vpc.vpc_principal.id
  name   = "allow_portainer"

  ingress = [{
    cidr_blocks      = ["${var.my_public_ip}"] // ex:
      # 172.31.208.1/32 diz que é somente este ip (no caso externo) que consegue acessar via ssh

    description      = "permitir ao portainer na porta 9000 "
    from_port        = 9000
    to_port          = 9000
    protocol         = "tcp"
    ipv6_cidr_blocks = []
    prefix_list_ids  = []
    security_groups  = []
    self             = true
  }]
}
