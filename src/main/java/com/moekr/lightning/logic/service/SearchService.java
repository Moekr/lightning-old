package com.moekr.lightning.logic.service;

import com.moekr.lightning.data.dao.ArticleDAO;
import com.moekr.lightning.logic.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final ArticleDAO articleDAO;

    @Autowired
    public SearchService(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public List<ArticleVO> searchArticle(String key) {
        return articleDAO.search(key).stream().map(ArticleVO::new).collect(Collectors.toList());
    }
}
