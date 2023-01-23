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


#VM options for local: 
-Dspring.profiles.active=local -Dserver.port=8081


#local API
http://localhost:8081/betline/preDump --load 1k record from resources 
http://localhost:8081/betline/load?number=10000 --generate 10k records
http://localhost:8081/betline/predicate?lottery=11X5JSC&game=11X5-DX2&drawNumber=2023001 --query with predicate
http://localhost:8081/betline/settle?lottery=11X5JSC&drawNumber=2023001 --update with entryProcessor
http://localhost:8081/betline/settleWithExec?lottery=11X5JSC&drawNumber=2023002 --update with Runnable Executor (no result, will be instant complete but the job will run in background)
http://localhost:8081/betline/settleWithSubmit?lottery=11X5JSC&drawNumber=2023002 --update with Callable Executor (all members)
http://localhost:8081/betline/settleWithSingleSubmit?lottery=11X5JSC&drawNumber=2023002 --update with Callable Executor (partition owned members)