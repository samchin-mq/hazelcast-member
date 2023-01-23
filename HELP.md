# Getting Started

docker build -t hazelcast-docker .
minikube image build -t hazelcast-docker .
minikube image load hazelcast-docker
kubectl create deployment hazelcast-docker --image=hazelcast-docker:latest
kubectl expose deployment hazelcast-docker --type=ClusterIP --port=8080
kubectl get services hazelcast-docker
minikube service hazelcast-docker

minikube service management-center
### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.10/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.10/maven-plugin/reference/html/#build-image)

