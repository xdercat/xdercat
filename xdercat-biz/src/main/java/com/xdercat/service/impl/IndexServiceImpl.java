package com.xdercat.service.impl;

import com.xdercat.service.IndexService;
import org.springframework.stereotype.Service;

/**
 * Created by bibom on 11/1/17.
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {
    @Override
    public String hello() {
        return "hello man";
    }
}
