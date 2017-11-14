package com.xdercat.web.controller;

import com.xdercat.domain.defined.TestDO;
import com.xdercat.service.IndexService;
import com.xdercat.web.constant.DOConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by bibom on 10/31/17.
 */
@Controller
public class IndexController extends BasicController {

    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        String content = indexService.hello();
        System.out.print(content);
        return "index";
    }

    @RequestMapping(value = "/example/api/test", method = RequestMethod.GET)
    public String test() {
        TestDO testDO = indexService.test();
        return responseJson(testDO, "获取成功", DOConstant.ResponseCode.SUCCESS, Status.TRUE);
    }
}
