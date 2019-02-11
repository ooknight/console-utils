package test;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.PropertyNamingStrategy;
import com.github.ooknight.utils.console.serializer.Feature;
import lombok.Data;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Runner {

    @Test
    public void test() {
        Hero hero = new Hero();
        hero.setId(1L);
        hero.setName("ooknight");
        hero.setSex(Sex.MALE);
        hero.setOpponent(hero);
        hero.setDate(LocalDate.of(1982, 12, 5));
        hero.setDatetime(LocalDateTime.of(2019, 2, 12, 13, 14, 15, 16));
        System.out.println(Inspector.string(hero, Feature.USE_SINGLE_QUOTES));
        System.out.println(Inspector.string(hero, Feature.WRITE_ENUM_USING_NAME));
        System.out.println(Inspector.string(hero, Feature.WRITE_ENUM_USING_NAME, Feature.USE_SINGLE_QUOTES));
    }

    @Test
    public void test2() {
        String p = "myFirstName";
        System.out.println(PropertyNamingStrategy.CAMEL.translate(p));
        System.out.println(PropertyNamingStrategy.KEBAB.translate(p));
        System.out.println(PropertyNamingStrategy.PASCAL.translate(p));
        System.out.println(PropertyNamingStrategy.SNAKE.translate(p));
    }

    private enum Sex {
        MALE, FEMALE
    }

    @Data
    private static class Hero extends Bean {

        private Long id;
        private String name;
        private Sex sex;
        private Hero opponent;
        private LocalDate date;
        private LocalDateTime datetime;
    }

    private static class Bean {

    }
}
