package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.TagDao;
import com.moekr.blog.data.entity.Tag;
import com.moekr.blog.logic.vo.TagVo;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.TagDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TagService {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<TagVo> getTags(){
        return tagDao.findAll().stream().map(TagVo::new).collect(Collectors.toList());
    }

    public TagVo getTag(String tagId){
        Tag tag = tagDao.findById(tagId);
        ToolKit.assertNotNull(tagId, tag);
        return new TagVo(tag);
    }

    @Transactional
    public TagVo updateTag(String tagId, TagDto tagDto){
        Tag tag = tagDao.findById(tagId);
        if(tag == null){
            ToolKit.assertPattern(tagId, PATTERN);
            tag = new Tag();
            tag.setId(tagId);
        }
        BeanUtils.copyProperties(tagDto, tag);
        return new TagVo(tagDao.save(tag));
    }

    @Transactional
    public void deleteTag(String tagId){
        Tag tag = tagDao.findById(tagId);
        ToolKit.assertNotNull(tagId, tag);
        tagDao.delete(tag);
    }
}
