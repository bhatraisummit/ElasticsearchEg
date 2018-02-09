/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knittech.elasticsearcheg.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author Sumit
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    protected Class<?>[] getRootConfigClasses() {
return null;    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
return new Class[]{WebConfig.class};    }

    @Override
    protected String[] getServletMappings() {
return new String[]{"/"};    }
    
}
