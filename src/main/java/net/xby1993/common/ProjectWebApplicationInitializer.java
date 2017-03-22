package net.xby1993.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

/**
 * Created by taojw 
 */
public class ProjectWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	System.out.println("Web容器启动啦");
    }
}
