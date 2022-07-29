package ua.net.maxx.mqtt2modbus.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import java.math.BigDecimal;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.net.maxx.mqtt2modbus.config.InfluxDbConfig;
import ua.net.maxx.mqtt2modbus.timer.DataListener;

public class InfluxDBStorageService implements DataListener {

    private static Logger logger = LogManager.getLogger();

    private final InfluxDBClient influxDBClient;
    private final WriteApiBlocking writeApi;

    public InfluxDBStorageService(InfluxDbConfig config) {
        this.influxDBClient = InfluxDBClientFactory.create(config.getUrl(), config.getToken().toCharArray(), config.getOrg(), config.getBucket());
        this.writeApi = influxDBClient.getWriteApiBlocking();
    }

    @Override
    public void publish(String topic, String value) {
        Point point = Point.measurement(topic.replace("/", "_"))
                .addField("value", new BigDecimal(value))
                .time(Instant.now(), WritePrecision.MS);
        writeApi.writePoint(point);
    }
}
