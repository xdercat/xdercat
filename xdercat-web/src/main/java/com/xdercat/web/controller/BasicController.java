package com.xdercat.web.controller;

import com.alibaba.dubbo.common.json.Jackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bibom on 8/29/17.
 */
public class BasicController {

    private static final Logger log = LoggerFactory.getLogger(BasicController.class);

    /**
     * 返回状态key
     */
    public static final String STATUS_KEY = "status";

    /**
     * 返回码key
     */
    public static final String CODE_KEY = "code";

    /**
     * 返回提示信息key
     */
    public static final String MSG_KEY = "msg";

    /**
     * 返回结果key
     */
    public static final String DATA_KEY = "data";


    /**
     * 返回状态
     */
    public interface Status {

        public static final boolean FALSE = false;

        public static final boolean TRUE = true;
    }

    public Map<String, Object> response(Object data, String msg, Integer code, Boolean status) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CODE_KEY, code);
        resultMap.put(MSG_KEY, msg);
        resultMap.put(STATUS_KEY, status);
        resultMap.put(DATA_KEY, data != null ? data : new Object());
        return resultMap;
    }

    public String responseJson(Object data, String msg, Integer code, Boolean status) {
        Map<String, Object> resultMap = response(data, msg, code, status);
        ObjectMapper jackson = Jackson.getObjectMapper();
        String json = "";
        try {
            json = jackson.writeValueAsString(resultMap);
        } catch (JsonProcessingException e) {
            log.warn("reponseJson exception:{}" + e.toString());
        }
        return StringUtils.isEmpty(json) ? "[]" : json;
    }
}
