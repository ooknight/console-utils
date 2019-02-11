package test;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.PropertyNamingStrategy;
import com.github.ooknight.utils.console.serializer.SerializerFeature;
import lombok.Data;
import org.junit.Test;

public class Runner {

    @Test
    public void test() {
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setName("sample");
        String str = Inspector.string(sample, SerializerFeature.USE_SINGLE_QUOTES);
        System.out.println(str);
    }

    @Test
    public void test2() {
        String p = "myFirstName";
        System.out.println(PropertyNamingStrategy.CamelCase.translate(p));
        System.out.println(PropertyNamingStrategy.KebabCase.translate(p));
        System.out.println(PropertyNamingStrategy.PascalCase.translate(p));
        System.out.println(PropertyNamingStrategy.SnakeCase.translate(p));
    }

    @Data
    private static class Sample extends Bean {

        private Long id;
        private String name;
    }

    private static class Bean {

    }
}
