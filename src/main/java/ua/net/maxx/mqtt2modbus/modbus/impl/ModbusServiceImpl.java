package ua.net.maxx.mqtt2modbus.modbus.impl;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ua.net.maxx.mqtt2modbus.config.Device;
import ua.net.maxx.mqtt2modbus.config.ValueType;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModbusServiceImpl implements ModbusService {

    private static Logger logger = LogManager.getLogger();

    private final SerialParameters portParams;
    private final AbstractModbusMaster master;

    public ModbusServiceImpl(SerialParameters portParams) throws Exception {
        this.portParams = portParams;
        this.master = new ModbusSerialMaster(portParams);
        this.master.setTimeout(1000);
        this.master.setRetries(1);
        master.connect();
    }

    private Register[] readMultipleRegisters(int unitID, int address, int count) throws ModbusException {
        // code = 3
        return master.readMultipleRegisters(unitID, address, count);
    }

    private InputRegister[] readInputRegisters(int unitID, int address, int count) throws ModbusException {
        // code = 4
        return master.readInputRegisters(unitID, address, count);
    }

    @Override
    public Map<String, String> getData(Device device) {
        logger.debug("Start getting data for device: {}", device.getDeviceId());
        Map<String, String> data = new HashMap<>();
        String deviceTopic = device.getTopic();
        device.getData().forEach(description -> {
            try {
                if (description.getType() == 3) {
                    logger.debug("Start getting data for registers with start: {}", description.getStartRegister());
                    Register[] registersData = readMultipleRegisters(device.getDeviceId(), description.getStartRegister(), description.getNumberRegisters());
                    logger.debug("Got data for registers with start: {}", description.getStartRegister());
                    description.getRegisters().forEach(register -> {
                        int off = register.getOffset();
                        ValueType valueType = register.getType();
                        BigDecimal byteValue = BigDecimal.ZERO;
                        switch (valueType) {
                            case S16:
                                byteValue = BigDecimal.valueOf(registersData[off].toShort());
                                break;
                            case U16:
                                byteValue = BigDecimal.valueOf(registersData[off].toUnsignedShort());
                                break;
                        }
                        BigDecimal value = byteValue.multiply(register.getFactor());
                        String topic = deviceTopic + description.getTopic() + register.getTopic();
                        data.put(topic, value.toString());
                    });
                }
            } catch (Exception e) {
                logger.error("Error on get modbus data: {}", e.getMessage());
            }
        });
        logger.debug("Finish getting data for device: {}", device.getDeviceId());
        return data;
    }

}
