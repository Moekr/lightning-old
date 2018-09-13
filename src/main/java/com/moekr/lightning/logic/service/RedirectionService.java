package com.moekr.lightning.logic.service;

import com.moekr.lightning.data.entity.Redirection;
import com.moekr.lightning.data.repository.RedirectionRepository;
import com.moekr.lightning.logic.vo.RedirectionVO;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.web.dto.RedirectionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RedirectionService {
    private static final Sort SORT = Sort.by(Sort.Order.asc("id"));
    private final RedirectionRepository repository;

    @Autowired
    public RedirectionService(RedirectionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RedirectionVO createOrUpdateRedirection(RedirectionDTO redirectionDTO) {
        Redirection redirection = repository.findById(redirectionDTO.getId()).orElse(null);
        if (redirection == null) {
            redirection = new Redirection();
        }
        BeanUtils.copyProperties(redirectionDTO, redirection);
        return new RedirectionVO(repository.save(redirection));
    }

    public List<RedirectionVO> retrieveRedirections(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, SORT);
        return repository.findAll(pageable).map(RedirectionVO::new).getContent();
    }

    public RedirectionVO retrieveRedirection(String id) {
        Redirection redirection = repository.findById(id).orElse(null);
        ToolKit.assertNotNull(id, redirection);
        return new RedirectionVO(redirection);
    }

    @Transactional
    public void deleteRedirection(String id) {
        Redirection redirection = repository.findById(id).orElse(null);
        ToolKit.assertNotNull(id, redirection);
        repository.delete(redirection);
    }

    @Transactional
    public RedirectionVO viewRedirection(String id) {
        Redirection redirection = repository.findById(id).orElse(null);
        ToolKit.assertNotNull(id, redirection);
        redirection.setViews(redirection.getViews() + 1);
        return new RedirectionVO(repository.save(redirection));
    }
}
