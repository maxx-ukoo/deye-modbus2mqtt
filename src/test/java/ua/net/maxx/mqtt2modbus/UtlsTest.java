package ua.net.maxx.mqtt2modbus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Test;

public class UtlsTest {

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    @Test
    public void test() {
        System.out.println(byteToHex((byte) 0xfe));

        System.out.println(new BigDecimal(Float.intBitsToFloat((0x46a3 << 16) + 0x24ee)));
        System.out.println(Float.intBitsToFloat((0x46a3 << 16) + 0x24ee));

        System.out.println(new BigDecimal(Float.intBitsToFloat((0x46d8 << 16) + 0xf018)));
        System.out.println(Float.intBitsToFloat((0x46d8 << 16) + 0xf018));


    }


}
