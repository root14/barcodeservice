package com.root14.barcodeservice;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Map;

/**
 * A utility class for dynamically registering and retrieving beans at runtime
 * using a local {@link DefaultListableBeanFactory}.
 *
 * <p>This class allows you to register new beans programmatically with optional
 * property values and retrieve them by name or type. It is useful in scenarios
 * where bean definitions need to be created on-the-fly outside the standard
 * Spring configuration lifecycle.</p>
 *
 * <p>Note: This class manages its own isolated {@link DefaultListableBeanFactory}
 * and does not integrate with the main Spring application context.</p>
 */
public class DynamicBeanManager {
    private final DefaultListableBeanFactory beanFactory;

    /**
     * Constructs a new {@code DynamicBeanManager} with its own internal
     * {@link DefaultListableBeanFactory}.
     */
    public DynamicBeanManager() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    /**
     * Registers a new bean definition with the given name, type, and optional properties.
     *
     * @param beanName   the name of the bean to register
     * @param beanClass  the class of the bean
     * @param properties a map of property names and values to set on the bean (maybe null or empty)
     * @param <T>        the type of the bean
     */
    public <T> void registerBean(String beanName, Class<T> beanClass, Map<String, Object> properties) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);

        if (properties != null && !properties.isEmpty()) {
            MutablePropertyValues mpv = new MutablePropertyValues();
            properties.forEach(mpv::add);
            beanDefinition.setPropertyValues(mpv);
        }

        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * Retrieves a bean by its type.
     *
     * @param clazz the class type of the desired bean
     * @param <T>   the type of the bean
     * @return the bean instance
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *         if no bean of the specified type is found
     */
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    /**
     * Retrieves a bean by its name.
     *
     * @param beanName the name of the bean
     * @return the bean instance
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *         if no bean with the specified name is found
     */
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    /**
     * Checks whether a bean with the specified name is registered.
     *
     * @param beanName the name of the bean
     * @return {@code true} if the bean exists; {@code false} otherwise
     */
    public boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }
}
