resource "aws_key_pair" "par-de-chaves" {
  key_name   = "chave-para-aws"
  public_key = file("key/aCredita-ssh-ec2-key.pub")
}

resource "aws_instance" "instances" {
  count = 3 
  ami           = "ami-02e136e904f3da870"
  instance_type = "t2.micro"

  subnet_id = element(aws_subnet.redes_publicas.*.id, count.index)

  key_name = aws_key_pair.par-de-chaves.key_name

  vpc_security_group_ids = [aws_security_group.permite-ssh.id, aws_security_group.permite-acessar-internet.id]


  tags = {
    Name = "luck_ec2_instances"
  }
}


# captura os ips publicos, preenche o template em memoria 
data "template_file" "hosts" {
  template = file("./templates/hosts.tpl")

  vars = {
    PUBLIC_IP_0 = "${aws_instance.instances.*.public_ip[0]}"
    PUBLIC_IP_1 = "${aws_instance.instances.*.public_ip[1]}"
    PUBLIC_IP_2 = "${aws_instance.instances.*.public_ip[2]}"

    PRIVATE_IP_0 = "${aws_instance.instances.*.private_ip[0]}"
  }
}

# cria um arquivo com os dados do template preenchido
resource "local_file" "hosts" {
  content  = data.template_file.hosts.rendered
  filename = "./templates/hosts"
}

// para exibir 
// terraform output public_ips
// para exibir digitar 
// terraform output public_ips
output "public_ips" {
  value = join(",", aws_instance.instances.*.public_ip)
}

