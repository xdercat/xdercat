package com.xdercat.web.controller;

import com.xdercat.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by bibom on 10/31/17.
 */
@Controller
public class IndexController {

    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        String content = indexService.hello();
        System.out.print(content);
        return "index";
    }
}
