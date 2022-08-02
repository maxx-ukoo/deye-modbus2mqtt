# deye-modbus2mqtt
Initialy created as bridge between DEYE inverter and MQTT broker
Tested with: Deye SUN-12K-SG04LP3-EU

# Current features
- pull DEYE registers every 5 seconds
- push data to MQTT
- push data to InfluxDB

# TODO
- Implement reading data for more that 1 register (Hi/Low values)

#Install on PRI
- prepare jar (mvn  package)
- prepare config file
- copy jar and  config file to any directory
- create service [How to convert jar file to service](https://dzone.com/articles/run-your-java-application-as-a-service-on-ubuntu)

# Grafana dashboard example

![Grafana dashboard example](docs/dashboard_example.png "Grafana dashboard example")

