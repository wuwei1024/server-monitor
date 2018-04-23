package com.test.controller;

import com.test.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping("/getServerInfo")
    public Map<String, Object> getServerInfo() {
        return dataService.getServerInfo();
    }
}
