package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogbackConfigXmlTest {

    @Test
    public void testPerformTask() throws Exception {
        LogbackConfigXml logbackConfigXml=new LogbackConfigXml();
        logbackConfigXml.performTask();
    }
}