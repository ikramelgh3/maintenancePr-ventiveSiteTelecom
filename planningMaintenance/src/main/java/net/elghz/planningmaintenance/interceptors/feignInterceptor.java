package net.elghz.planningmaintenance.interceptors;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
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
////        System.out.println("***********************");
////        System.out.println(authentication.getClass().getName());
////        System.out.println("***********************");
//        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
//        String jwtAccessToken =jwtAuthenticationToken.getToken().getTokenValue();
//        System.out.println("***********************");
//        System.out.println("le acces token est :"+jwtAccessToken);
//        template.header("Authorization" , "Bearer" +jwtAccessToken);
//    }
//}
