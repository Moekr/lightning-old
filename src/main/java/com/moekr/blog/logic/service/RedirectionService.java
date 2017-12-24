package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.RedirectionDAO;
import com.moekr.blog.data.entity.Redirection;
import com.moekr.blog.logic.vo.RedirectionVO;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.RedirectionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "redirection")
public class RedirectionService {
    private final RedirectionDAO redirectionDAO;

    @Autowired
    public RedirectionService(RedirectionDAO redirectionDAO){
        this.redirectionDAO = redirectionDAO;
    }

    @Transactional
    @CachePut(key = "#redirectionDTO.id")
    public RedirectionVO createOrUpdateRedirection(RedirectionDTO redirectionDTO){
        Redirection redirection = redirectionDAO.findById(redirectionDTO.getId());
        if(redirection == null){
            redirection = new Redirection();
        }
        BeanUtils.copyProperties(redirectionDTO, redirection);
        return new RedirectionVO(redirectionDAO.save(redirection));
    }

    @Cacheable(key = "'redirectionList'")
    public List<RedirectionVO> getRedirections(){
        return redirectionDAO.findAll().stream().map(RedirectionVO::new).collect(Collectors.toList());
    }

    @Cacheable(key = "#redirectionId")
    public RedirectionVO getRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        return new RedirectionVO(redirection);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(key = "#redirectionId"), @CacheEvict(key = "'redirectionList'")})
    public void deleteRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirectionDAO.delete(redirection);
    }

    @Transactional
    @Caching(put = @CachePut(key = "#redirectionId"), evict = @CacheEvict(key = "'redirectionList'"))
    public RedirectionVO viewRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirection.setViews(redirection.getViews() + 1);
        return new RedirectionVO(redirectionDAO.save(redirection));
    }
}
