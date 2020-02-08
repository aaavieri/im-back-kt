package com.wwx.imback.clientimport

import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.MetadataReader
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.core.type.filter.AnnotationTypeFilter

class HttpClientRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(HttpClientScan::class.java.name))
        val scanner = HttpClientDefinitionScanner(registry)
        val annotationClass = annoAttrs!!.getClass<Annotation>("annotationClass")
        if (Annotation::class.java != annotationClass) {
            scanner.addIncludeFilter(AnnotationTypeFilter(annotationClass))
        } else {
            scanner.addIncludeFilter { _: MetadataReader, _: MetadataReaderFactory -> true }
        }
        scanner.addExcludeFilter { metadataReader: MetadataReader, _: MetadataReaderFactory ->
            val className = metadataReader.classMetadata.className
            className.endsWith("package-info")
        }
        scanner.addExcludeFilter { metadataReader: MetadataReader, _: MetadataReaderFactory ->
            val className = metadataReader.classMetadata.className
            className == HttpClientRegistrar::class.java.name
        }
        val packages = mutableListOf(*annoAttrs.getStringArray("value"))
        packages.addAll(listOf(*annoAttrs.getStringArray("basePackages")))
        scanner.doScan(*packages.toTypedArray())
    }
}