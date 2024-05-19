# 📖 Porting Manual

## 1. NginX &nbsp;&nbsp;&nbsp;![Static Badge](https://img.shields.io/badge/NginX-v1.18.0-blue?logo=nginx&logoColor=%23009639)

1. Nginx 설치

```
sudo apt-get install -y nginx
```

2. Config 설정

```
server {
	listen [::]:443 ssl ipv6only=on;
	listen 443 ssl;

	ssl_certificate /etc/letsencrypt/live/mugit.site/fullchain.pem;
	ssl_certificate_key /etc/letsencrypt/live/mugit.site/privkey.pem;
	include /etc/letsencrypt/options-ssl-nginx.conf;
	ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

	location /swagger {
		proxy_pass http://10.103.232.98:8080;
	}

	location /files {
		proxy_pass http://10.109.95.13:8090;
		proxy_set_header Host $host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		proxy_set_header X-Forwarded-Proto $scheme;
		client_max_body_size 100M;
	}

	location /api {
		proxy_pass http://10.103.232.98:8080;
	}

	location /sse {
		proxy_pass http://10.101.193.165:8100;
		proxy_set_header Connection '';
		proxy_http_version 1.1;
		proxy_buffering off;
		proxy_cache off;
		chunked_transfer_encoding off;
	}

	location / {
		proxy_pass http://10.98.8.232:3000;
	}

}

```

<br><br>

## 2. Jenkins&nbsp;&nbsp;&nbsp;![Static Badge](https://img.shields.io/badge/jenkins-v2.441-blue?logo=jenkins&logoColor=%23D24939)

1. Jenkins 설치

```
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update

sudo apt-get install jenkins
```

<br><br>

## 3. Kubernetes &nbsp;&nbsp;&nbsp;![Static Badge](https://img.shields.io/badge/Kubernetes-v1.29.2-blue?logo=kubernetes)

1. Docker Engine 설치

```
# Add Docker's official GPG key:
sudo apt-get update

sudo apt-get install ca-certificates curl

sudo install -m 0755 -d /etc/apt/keyrings

sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc

sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

# install the Docker Packages:
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

2. cri-dockerd 설치

```
wget https://github.com/Mirantis/cri-dockerd/releases/download/v0.3.11/cri-dockerd-0.3.11.amd64.tgz

tar -xvzf cri-dockerd-0.3.11.amd64.tgz

mv cri-dockerd/cri-dockerd /usr/local/bin

cri-dockerd
```

3. Kubernetes 설치

```
sudo swapoff -a && sed -i '/ swap / s/^/#/' /etc/fstab

sudo apt-get update

sudo apt-get install -y apt-transport-https ca-certificates curl gpg

curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

sudo apt-get update

sudo apt-get install -y kubelet kubeadm kubectl

sudo apt-mark hold kubelet kubeadm kubectl
```

4. Node 설정

```
kubeadm init
# 에러 발생 시
# sudo rm /etc/containerd/config.toml
# sudo systemctl restart containerd
# sudo kubeadm init

mkdir -p $HOME/.kube

sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl apply -f https://reweave.azurewebsites.net/k8s/v1.29/net.yaml

kubectl taint nodes --all node-role.kubernetes.io/control-plane-
```

<br><br>

<br><br>

## 4. 서버 설치

1. /mugit/yaml 폴더의 이름에 맞게 apply

```
kubectl apply -f [서버 파일 이름]
```

<br><br>
