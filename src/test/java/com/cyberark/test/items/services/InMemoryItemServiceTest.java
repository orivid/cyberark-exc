package com.cyberark.test.items.services;

import com.cyberark.test.TestApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApp.class}, initializers = ConfigFileApplicationContextInitializer.class)
public class InMemoryItemServiceTest {

    //@Test
    public void fakeTest1() {
        Assertions.assertTrue(true);
    }

    //@Test
    public void fakeTest2() {
        Assertions.assertTrue(true);
    }

}