package com.uuuuuuuuuuuuuuu.util.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebUtils extends org.springframework.web.util.WebUtils {

    /**
     * 获取{@link HttpServletRequest}
     * @return HttpServletRequest
     */
    public static HttpServletRequest obtainHttpServletRequest() {
        return obtainRequestAttributes().getRequest();
    }

    /**
     * 获取 {@link ServletRequestAttributes}
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes obtainRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取  {@link HttpServletResponse}
     * @return HttpServletResponse
     */
    public static HttpServletResponse obtainHttpServletResponse() {
        return obtainRequestAttributes().getResponse();
    }

    /**
     * 获取 sessionId
     * @return sessionId
     */
    public static String obtainSessionId() {
        return obtainRequestAttributes().getSessionId();
    }

    /**
     * 获取 {@link HttpSession}
     * @return HttpSession
     */
    public static HttpSession obtainSession() {
        return obtainHttpServletRequest().getSession();
    }


    /**
     * springmvc获取Applicationcontext
     * @return
     */
    public static WebApplicationContext webApplicationContext() {
        ServletContext servletContext = obtainHttpServletRequest().getSession().getServletContext();
        return (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

}