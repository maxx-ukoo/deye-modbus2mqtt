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
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import ua.net.maxx.mqtt2modbus.config.Device;
import ua.net.maxx.mqtt2modbus.config.ValueType;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModbusServiceImpl implements ModbusService {

    private static Logger logger = LogManager.getLogger();
    private AtomicInteger errorCount = new AtomicInteger(0);

    private final AbstractModbusMaster master;

    public ModbusServiceImpl(SerialParameters portParams) throws Exception {
        this.master = new ModbusSerialMaster(portParams);
        this.master.setTimeout(5000);
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
        if (errorCount.get() > 1) {
            logger.error("Errors count: {}", errorCount.get());
        }
        if (errorCount.get() > 5) {
            try {
                logger.error("Found 5 errors, master isConnect={}", master.isConnected());
                master.disconnect();
                master.connect();
            } catch (Exception e) {
                logger.error("Can't reopen port", e);
            }
        }
        logger.debug("Start getting data for device: {}", device.getDeviceId());
        Map<String, String> data = new HashMap<>();
        String deviceTopic = device.getTopic();
        device.getData().forEach(description -> {
            try {
                logger.debug("Start getting data for registers with type {}, start: {}", description.getType(), description.getStartRegister());
                AtomicReference<InputRegister[]> registersData = new AtomicReference<InputRegister[]>();
                if (description.getType() == 4) {
                    registersData.set(readInputRegisters(device.getDeviceId(), description.getStartRegister(), description.getNumberRegisters()));
                }
                if (description.getType() == 3) {
                    registersData.set(readMultipleRegisters(device.getDeviceId(), description.getStartRegister(), description.getNumberRegisters()));
                }
                logger.debug("Got data for registers with start: {}", description.getStartRegister());
                description.getRegisters().forEach(register -> {
                    int off = register.getOffset();
                    ValueType valueType = register.getType();
                    BigDecimal byteValue = BigDecimal.ZERO;
                    switch (valueType) {
                        case S16:
                            byteValue = BigDecimal.valueOf(registersData.get()[off].toShort());
                            break;
                        case U16:
                            byteValue = BigDecimal.valueOf(registersData.get()[off].toUnsignedShort());
                            break;
                        case FLOAT32:
                            logger.debug("Got data for registers with start: {}", description.getStartRegister());
                            byteValue = new BigDecimal(Float.intBitsToFloat((registersData.get()[off].getValue() << 16) + registersData.get()[off + 1].getValue()));
                    }
                    BigDecimal value = byteValue.multiply(register.getFactor());
                    String topic = deviceTopic + description.getTopic() + register.getTopic();
                    if (deviceTopic.equals("/grid_meter") && (register.getTopic().contains("import") || register.getTopic().contains("export"))) {
                        StringJoiner sj = new StringJoiner(" ", "[", "]");
                        byte[] bytes = registersData.get()[off].toBytes();
                        for (int i=0; i<bytes.length; i++) {
                            sj.add(byteToHex(bytes[i]));
                        }
                        bytes = registersData.get()[off + 1].toBytes();
                        for (int i=0; i<bytes.length; i++) {
                            sj.add(byteToHex(bytes[i]));
                        }
                        logger.debug("Energy debug: {} => {}", topic, sj.toString());
                    }
                    data.put(topic, value.toString());
                });
                errorCount.set(0);

            } catch (Exception e) {
                errorCount.addAndGet(1);
                logger.error("Error on get modbus data", e);
            }
        });
        logger.debug("Finish getting data for device: {}", device.getDeviceId());
        return data;
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

}
