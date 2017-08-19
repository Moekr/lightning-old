package com.moekr.blog.web.controller.api;

import com.moekr.blog.logic.service.TagService;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public Map<String, Object> getTags(){
        return ToolKit.assemblyResponseBody(tagService.getTags());
    }

    @GetMapping("/tag/{tagId}")
    public Map<String, Object> getTag(@PathVariable String tagId){
        return ToolKit.assemblyResponseBody(tagService.getTag(tagId));
    }

    @PutMapping("/tag/{tagId}")
    public Map<String, Object> updateTag(@PathVariable String tagId, @RequestBody @Valid TagDto tagDto){
        return ToolKit.assemblyResponseBody(tagService.updateTag(tagId, tagDto));
    }

    @DeleteMapping("/tag/{tagId}")
    public Map<String, Object> deleteTag(@PathVariable String tagId){
        return ToolKit.assemblyResponseBody(tagService.deleteTag(tagId));
    }
}
