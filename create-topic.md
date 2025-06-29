docker exec -it kafka bash

kafka-topics --create --bootstrap-server localhost:9092 --topic account-topic --partitions 3 --replication-factor 1


kafka-topics --delete --bootstrap-server localhost:9092 --topic account-topic --partitions 3 --replication-factor 1