package com.schedule.common.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 应用上下文获取bean辅助类
 * @author 887961
 * @Date 2016年11月2日 下午3:18:54
 */
@Component
public final class ContextHelper implements ApplicationContextAware {
    private ApplicationContext context;
    
    private static ContextHelper helper = new ContextHelper();
    
    private ContextHelper() {}
    
    public static ContextHelper getInstance() {
        return helper;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        helper.context = context;
    }
    
    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    public <T> T getBean(String name){
        return (T) context.getBean(name);
    }
}

