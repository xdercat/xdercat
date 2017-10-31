package com.xdercat.test;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bibom on 10/31/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest extends TestCase implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
