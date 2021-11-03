#!/bin/bash
# chmod +xxx ./run-terraform.sh

echo "destroying infrastructure"
echo "Finding my public ip address..."
MY_PUBLIC_IP="$(curl -s ipinfo.io/ip)"
echo "... your public ip is $MY_PUBLIC_IP"

echo "terraform destroy "
terraform destroy -var "my_public_ip=$MY_PUBLIC_IP/32"