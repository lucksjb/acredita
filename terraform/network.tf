/*****
   Define uma VPC que irá conter todas as nossas subnets
*****/
resource "aws_vpc" "vpc_principal" {
  cidr_block = "192.168.0.0/16"
  
  tags = {
    "Name" = "luck_vpc"
  }
}

/*****
   Define nossas subnets que irao conter todas as nossas instancias EC2/RDS/etc
*****/
resource "aws_subnet" "redes_privadas" {
    count = 3
    vpc_id = aws_vpc.vpc_principal.id
    // https://codez.deedx.cz/posts/dynamic-subnet-calculation-cidr-terraform/#:~:text=Terraform%20provides%20handy%20function%20called%20cidrsubnet%20which%20is,individual%20parameters%20and%20explains%20how%20the%20calculation%20works.
    cidr_block = cidrsubnet(aws_vpc.vpc_principal.cidr_block,8, count.index + 10) 
    availability_zone       = var.availability_zones[count.index]

    tags = {
        "Name" = "luck_private_subnet_1"
    }
}


resource "aws_subnet" "redes_publicas" {
    count = 3
    vpc_id = aws_vpc.vpc_principal.id
    cidr_block = cidrsubnet(aws_vpc.vpc_principal.cidr_block,8,count.index + 20 )
    availability_zone       = var.availability_zones[count.index]
    map_public_ip_on_launch = true  // se a rede é publica ou privada 

    tags = {
        "Name" = "luck_public_subnet_1"
    }
}




/*****
   aws_internet_gateway , aws_route_table e aws_route_table_association
   servem para dara acesso a internet das maquinas ec2
*****/
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc_principal.id
}

resource "aws_route_table" "route_igw" {
  vpc_id = aws_vpc.vpc_principal.id

  route = [{
    cidr_block = "0.0.0.0/0" // sempre que for pra qualquer endereço fora da rede 
    gateway_id = "${aws_internet_gateway.igw.id}"
    carrier_gateway_id = ""
    destination_prefix_list_id = ""
    egress_only_gateway_id = ""
    instance_id = ""
    ipv6_cidr_block = ""
    local_gateway_id = ""
    nat_gateway_id = ""
    network_interface_id = ""
    transit_gateway_id = ""
    vpc_endpoint_id = ""
    vpc_peering_connection_id = ""
  }]
}

resource "aws_route_table_association" "route_table_association" {
  count = 3
  route_table_id = aws_route_table.route_igw.id
  subnet_id      = element(aws_subnet.redes_publicas.*.id, count.index)
  //https://www.terraform.io/docs/language/functions/element.html
}

