package com.cdy.sample_server;

import org.springframework.boot.context.properties.PropertyMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author pancc
 * @version 1.0
 */
public class PropertyTest {


    private static Container allNullContainer;
    private static Container container;

    static {
        allNullContainer = new Container().setLocation(null).setIntegers(null);
        container = new Container().setLocation(" xs").setIntegers(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));
    }
    
    public static void main(String[] args) {
        PropertyTest test = new PropertyTest();
        test.testNormal();
        test.testMapper();
    }
    
    public void testNormal() {
        normal(allNullContainer);
        normal(container);
    }

    void testMapper() {
        mapper(allNullContainer);
        mapper(container);
    }

    private void normal( Container container) {
        String local = "";
        List<String> binaries = new ArrayList<>();

        if (container != null) {
            if (container.getLocation() != null) {
                local = container.getLocation().trim();
            }
            if (container.getIntegers() != null) {
                container.getIntegers().stream().map(Integer::toBinaryString).collect(Collectors.toCollection(() -> binaries));
            }
        }

        System.out.println("local = " + local);
        System.out.println("binaries = " + binaries);
    }

    private void mapper( Container container) {
        StringBuilder local = new StringBuilder();
        List<String> binaries = new ArrayList<>();

        PropertyMapper mapper = PropertyMapper.get();

/*        mapper.from(container).whenNonNull().to(c -> {
            mapper.from(c.getLocation()).whenNonNull().as(String::trim).to(local::append);
            mapper.from(c.getIntegers()).whenNonNull().to(is -> is.stream().map(Integer::toBinaryString).collect(Collectors.toCollection(() -> binaries)));
        });*/
        mapper.from(container).whenNonNull().as(Container::getLocation).whenNonNull().as(String::trim).to(local::append);
        mapper.from(container).whenNonNull().as(Container::getIntegers).whenNonNull().to(is -> is.stream().map(Integer::toBinaryString).collect(Collectors.toCollection(() -> binaries)));

        System.out.println("local = " + local.toString());
        System.out.println("binaries = " + binaries);
    }
}