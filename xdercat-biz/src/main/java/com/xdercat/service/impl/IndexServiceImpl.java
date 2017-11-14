package com.xdercat.service.impl;

import com.xdercat.dal.generated.ExampleMapper;
import com.xdercat.domain.Example;
import com.xdercat.domain.ExampleExample;
import com.xdercat.domain.defined.TestDO;
import com.xdercat.service.IndexService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bibom on 11/1/17.
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    private static final Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private ExampleMapper exampleMapper;

    @Override
    public String hello() {
        return "hello man";
    }

    @Override
    public TestDO test() {
        TestDO testDO = new TestDO();
        ExampleExample exampleExample = new ExampleExample();
        exampleExample.createCriteria().andIdEqualTo(1);
        List<Example> exampleList = exampleMapper.selectByExample(exampleExample);
        if (CollectionUtils.isNotEmpty(exampleList)) {
            log.info("exampleList is not empty");
            Example example = exampleList.get(0);
            testDO.setId(example.getId());
            return testDO;
        }
        return null;
    }
}
