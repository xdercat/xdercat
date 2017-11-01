package com.xdercat.test.service;

import com.xdercat.service.IndexService;
import com.xdercat.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by bibom on 11/1/17.
 */
public class IndexServiceTest extends BaseTest {

    @Autowired
    IndexService indexService;

    @Test
    public void helloTest() {
        String content = indexService.hello();
        System.out.print(content);
    }
}
