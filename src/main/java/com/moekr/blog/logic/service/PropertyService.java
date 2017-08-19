package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.PropertyDao;
import com.moekr.blog.data.entity.Property;
import com.moekr.blog.logic.vo.PropertyVo;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.util.enums.Properties;
import com.moekr.blog.web.dto.PropertyDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private final PropertyDao propertyDao;

    @Autowired
    public PropertyService(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
        checkProperties();
    }

    public List<PropertyVo> getProperties(){
        return propertyDao.findAll().stream().map(PropertyVo::new).collect(Collectors.toList());
    }

    public PropertyVo getProperty(String propertyId){
        Property property = propertyDao.findById(propertyId);
        ToolKit.assertNotNull(propertyId, property);
        return new PropertyVo(property);
    }

    @Transactional
    public PropertyVo updateProperty(String propertyId, PropertyDto propertyDto){
        Property property = propertyDao.findById(propertyId);
        ToolKit.assertNotNull(propertyId, property);
        BeanUtils.copyProperties(propertyDto, property);
        return new PropertyVo(propertyDao.save(property));
    }

    public Map<String, String> getPropertiesAsMap(){
        return ToolKit.iterableToMap(getProperties(), PropertyVo::getId, PropertyVo::getValue);
    }

    private void checkProperties(){
        for(Properties properties : Properties.values()){
            Property property = propertyDao.findById(properties.getId());
            if(property == null){
                property = new Property();
                BeanUtils.copyProperties(properties, property);
                propertyDao.save(property);
            }
        }
    }
}
