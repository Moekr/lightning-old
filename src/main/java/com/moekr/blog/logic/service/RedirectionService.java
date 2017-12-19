package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.RedirectionDao;
import com.moekr.blog.data.entity.Redirection;
import com.moekr.blog.logic.vo.RedirectionVo;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.RedirectionDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedirectionService {
    private final RedirectionDao redirectionDao;

    @Autowired
    public RedirectionService(RedirectionDao redirectionDao){
        this.redirectionDao = redirectionDao;
    }

    @Transactional
    public RedirectionVo createOrUpdateRedirection(RedirectionDto redirectionDto){
        Redirection redirection = redirectionDao.findById(redirectionDto.getId());
        if(redirection == null){
            redirection = new Redirection();
        }
        BeanUtils.copyProperties(redirectionDto, redirection);
        return new RedirectionVo(redirectionDao.save(redirection));
    }

    public List<RedirectionVo> getRedirections(){
        return redirectionDao.findAll().stream().map(RedirectionVo::new).collect(Collectors.toList());
    }

    public RedirectionVo getRedirection(String redirectionId){
        Redirection redirection = redirectionDao.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        return new RedirectionVo(redirection);
    }

    @Transactional
    public void deleteRedirection(String redirectionId){
        Redirection redirection = redirectionDao.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirectionDao.delete(redirection);
    }

    @Transactional
    public RedirectionVo viewRedirection(String redirectionId){
        Redirection redirection = redirectionDao.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirection.setViews(redirection.getViews() + 1);
        return new RedirectionVo(redirectionDao.save(redirection));
    }
}
