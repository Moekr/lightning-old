package com.moekr.lightning.logic.service;

import com.moekr.lightning.data.dao.PropertyDAO;
import com.moekr.lightning.data.entity.Property;
import com.moekr.lightning.logic.vo.PropertyVO;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.util.enums.Properties;
import com.moekr.lightning.web.dto.PropertyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private final PropertyDAO propertyDAO;

    @Autowired
    public PropertyService(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    public List<PropertyVO> getProperties() {
        return propertyDAO.findAll().stream().map(PropertyVO::new).collect(Collectors.toList());
    }

    public PropertyVO getProperty(String propertyId) {
        Property property = propertyDAO.findById(propertyId);
        ToolKit.assertNotNull(propertyId, property);
        return new PropertyVO(property);
    }

    @Transactional
    public PropertyVO updateProperty(String propertyId, PropertyDTO propertyDTO) {
        Property property = propertyDAO.findById(propertyId);
        ToolKit.assertNotNull(propertyId, property);
        BeanUtils.copyProperties(propertyDTO, property);
        return new PropertyVO(propertyDAO.save(property));
    }

    public Map<String, String> getPropertiesAsMap() {
        return ToolKit.iterableToMap(getProperties(), PropertyVO::getId, PropertyVO::getValue);
    }

    @PostConstruct
    private void checkProperties() {
        for (Properties properties : Properties.values()) {
            Property property = propertyDAO.findById(properties.getId());
            if (property == null) {
                property = new Property();
                BeanUtils.copyProperties(properties, property);
                propertyDAO.save(property);
            }
        }
    }
}
