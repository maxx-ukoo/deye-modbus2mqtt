package ua.net.maxx.mqtt2modbus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Test;

public class UtlsTest {

    @Test
    public void test() {
        System.out.println(Instant.now().atOffset(ZoneId.systemDefault().getRules().getOffset(Instant.now())));
        System.out.println(Instant.now().atOffset(ZoneId.systemDefault().getRules().getOffset(Instant.now())).toInstant());
    }
}
