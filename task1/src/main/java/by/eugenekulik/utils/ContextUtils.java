package by.eugenekulik.utils;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;

import java.util.Set;

public class ContextUtils {

    private ContextUtils(){}

    public static <T> T getBean(Class<T> tClass) {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(tClass);
        if (beans.isEmpty()) {
            throw new IllegalStateException("CDI bean not found for type: " + tClass);
        }
        Bean<?> bean = beanManager.resolve(beans);
        return tClass.cast(beanManager.getReference(bean, tClass, beanManager.createCreationalContext(bean)));
    }


}
