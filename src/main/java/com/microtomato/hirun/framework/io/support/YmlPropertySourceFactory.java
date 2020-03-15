package com.microtomato.hirun.framework.io.support;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Properties;

/**
 * Yaml 配置文件工厂
 *
 * @author Steven
 * @date 2020-03-15
 */
public class YmlPropertySourceFactory extends DefaultPropertySourceFactory {

    private static final String SUFFIX_YML = ".yml";
    private static final String SUFFIX_YAML = ".yaml";

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = (name == null) ? resource.getResource().getFilename() : name;
        assert sourceName != null;
        if (sourceName.endsWith(SUFFIX_YML) || sourceName.endsWith(SUFFIX_YAML)) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            Properties properties = factory.getObject();
            assert properties != null;
            return new PropertiesPropertySource(sourceName, properties);
        }
        return super.createPropertySource(name, resource);
    }

}
