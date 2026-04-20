package villanidev.berlinclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BerlinClockApplication {
    public static void main(String[] args) {
        SpringApplication.run(BerlinClockApplication.class, args);
    }
}
