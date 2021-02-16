package com.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "jordanfeignclient")
public interface PortFeignClient {
    @GetMapping("/getPort")
    public String getPort();
}
