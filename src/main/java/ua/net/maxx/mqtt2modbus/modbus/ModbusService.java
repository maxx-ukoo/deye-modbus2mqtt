package ua.net.maxx.mqtt2modbus.modbus;

import java.util.Map;
import ua.net.maxx.mqtt2modbus.config.Device;

public interface ModbusService {

    Map<String, String> getData(Device regDesc);
}
