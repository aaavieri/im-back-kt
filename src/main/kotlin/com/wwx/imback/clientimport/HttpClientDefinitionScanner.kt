package com.wwx.imback.clientimport

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinitionHolder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner

class HttpClientDefinitionScanner internal constructor(registry: BeanDefinitionRegistry) : ClassPathBeanDefinitionScanner(registry) {
    override fun isCandidateComponent(beanDefinition: AnnotatedBeanDefinition): Boolean {
        val metadata = beanDefinition.metadata
        return metadata.isIndependent && metadata.isInterface
    }

    public override fun doScan(vararg basePackages: String): Set<BeanDefinitionHolder> {
        val beanDefinitions = super.doScan(*basePackages)
        if (beanDefinitions.isEmpty()) {
            logger.warn("No MyBatis mapper was found in '" + basePackages.contentToString() + "' package. Please check your configuration.")
        } else {
            processBeanDefinitions(beanDefinitions)
        }
        return beanDefinitions
    }

    private fun processBeanDefinitions(beanDefinitions: Set<BeanDefinitionHolder>) {
        var definition: GenericBeanDefinition
        for (holder in beanDefinitions) {
            definition = holder.beanDefinition as GenericBeanDefinition
            if (logger.isDebugEnabled) {
                logger.debug("Creating MapperFactoryBean with name '" + holder.beanName
                        + "' and '" + definition.beanClassName + "' mapperInterface")
            }
            // the mapper interface is the original class of the bean
// but, the actual class of the bean is MapperFactoryBean
            definition.constructorArgumentValues.addGenericArgumentValue(definition.beanClassName!!) // issue #59
            definition.setBeanClass(HttpClientFactoryBean::class.java)
        }
    }
}