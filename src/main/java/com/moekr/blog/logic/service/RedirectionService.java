package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.RedirectionDAO;
import com.moekr.blog.data.entity.Redirection;
import com.moekr.blog.logic.vo.RedirectionVO;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.RedirectionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedirectionService {
    private final RedirectionDAO redirectionDAO;

    @Autowired
    public RedirectionService(RedirectionDAO redirectionDAO){
        this.redirectionDAO = redirectionDAO;
    }

    @Transactional
    public RedirectionVO createOrUpdateRedirection(RedirectionDTO redirectionDTO){
        Redirection redirection = redirectionDAO.findById(redirectionDTO.getId());
        if(redirection == null){
            redirection = new Redirection();
        }
        BeanUtils.copyProperties(redirectionDTO, redirection);
        return new RedirectionVO(redirectionDAO.save(redirection));
    }

    public List<RedirectionVO> getRedirections(){
        return redirectionDAO.findAll().stream().map(RedirectionVO::new).collect(Collectors.toList());
    }

    public RedirectionVO getRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        return new RedirectionVO(redirection);
    }

    @Transactional
    public void deleteRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirectionDAO.delete(redirection);
    }

    @Transactional
    public RedirectionVO viewRedirection(String redirectionId){
        Redirection redirection = redirectionDAO.findById(redirectionId);
        ToolKit.assertNotNull(redirectionId, redirection);
        redirection.setViews(redirection.getViews() + 1);
        return new RedirectionVO(redirectionDAO.save(redirection));
    }
}
