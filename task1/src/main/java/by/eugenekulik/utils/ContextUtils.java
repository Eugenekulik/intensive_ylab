package by.eugenekulik.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.Set;

@ApplicationScoped
@NoArgsConstructor
public class ContextUtils {


    private BeanManager beanManager;

    @Inject
    public ContextUtils(BeanManager beanManager){
        this.beanManager = beanManager;
    }

    public <T> T getBean(Class<T> tClass) {
        Set<Bean<?>> beans = beanManager.getBeans(tClass);
        if (beans.isEmpty()) {
            throw new IllegalStateException("CDI bean not found for type: " + tClass);
        }
        Bean<?> bean = beanManager.resolve(beans);
        return tClass.cast(beanManager.getReference(bean, tClass, beanManager.createCreationalContext(bean)));
    }


}
