package timer;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class EventTimerTest {

    @Test
    void test() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR, 10);
        System.out.println(calendar.getTime());
    }
}
