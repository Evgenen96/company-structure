package com.epam.departmentmicroservice.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class EmployeeClientFallbackFactory implements FallbackFactory<EmployeeClient> {

    @Override
    public EmployeeClient create(Throwable cause) {
        return new EmployeeClientFallback(cause);
    }
}
