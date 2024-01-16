package com.codewranglers.workflowmanager;

import com.codewranglers.workflowmanager.controllers.AuthenticationController;
import com.codewranglers.workflowmanager.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/logout", "/index", "/unauthorized", "/error", "/images");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.equals("/") || path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdminWhitelisted(String path) {
        return path.startsWith("/admin");
    }

    private static boolean isManagerWhitelisted(String path) {
        return path.startsWith("/manager");
    }

    private static boolean isMemberWhitelisted(String path) {
        return path.startsWith("/member");
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(isWhitelisted(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
//        Boolean isAdminPath = isAdminWhitelisted(request.getRequestURI());

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (user.getRole() == 1 && (isManagerWhitelisted(request.getRequestURI()) || isWhitelisted(request.getRequestURI()))) {
            return true;
        }

        if (user.getRole() == 2 && (isMemberWhitelisted(request.getRequestURI())) || isWhitelisted(request.getRequestURI())){
            return true;
        }

        if (user.getRole() == 3 && (isAdminWhitelisted(request.getRequestURI())) || isWhitelisted(request.getRequestURI())){
            return true;
        }

        response.sendRedirect("/unauthorized");
        return false;







    }
}
