#API-ORDER-SERVICE
#Docker Build
    docker build -f Dockerfile -t galaxytraining/order-service:1.0.0 . 
### DOCKER RUN
    docker run -d \
    --name order-service \
    -p 8082:8082 \
    -e CONFIG_SCHEMA_REGISTRY_URL=PLAINTEXT://192.168.1.48:19092 \
    -e CONFIG_KAFKA_BROKERS=//192.168.1.48:19092 \
    -e MYSQL_DB=mysql://localhost:3306/orderdb \
     galaxytraining/order-service:1.0.0

# DOCKER RUN EN WINDOWS (POWERSHELL)

docker run -d `
  --name order-service `
-p 8082:8082 `
  -e CONFIG_SCHEMA_REGISTRY_URL=http://192.168.1.48:8081 `
-e CONFIG_KAFKA_BROKERS=PLAINTEXT://192.168.1.48:19092 `
  -e MYSQL_DB=mysql://host.docker.internal:3306/orderdb `
galaxytraining/order-service:1.0.0
