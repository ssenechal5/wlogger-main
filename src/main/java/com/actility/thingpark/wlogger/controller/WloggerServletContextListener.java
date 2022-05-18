package com.actility.thingpark.wlogger.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WloggerServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.getSessionCookieConfig().setHttpOnly(true);
        ctx.getSessionCookieConfig().setSecure(true);
    }
}
