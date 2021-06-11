package rainbow.kuzwlu;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.core.SimbotApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/21 14:22
 * @Version 1.0
 */

@SpringBootApplication
@SimbotApplication(@SimbotResource(value = "application.yml"))
public class RainbowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainbowApplication.class, args);
        SimbotApp.run(new RainbowApplication(),args);
    }

}
