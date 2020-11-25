package com.epam.employeemicroservice.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DepartmentClientFallbackFactory implements FallbackFactory<DepartmentClient> {

    @Override
    public DepartmentClient create(Throwable cause) {
        return new DepartmentClientFallback(cause);

    }
}
