package net.elghz.userservice.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
//
//@Component
//public class feignInterceptor implements RequestInterceptor {
//
//    @Override
//    public void apply(RequestTemplate template) {
//
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        System.out.println("***********************");
//        System.out.println(authentication.getClass().getName());
//        System.out.println("***********************");
//    }
//}
