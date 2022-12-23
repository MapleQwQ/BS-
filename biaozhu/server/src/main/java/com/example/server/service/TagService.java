package com.example.server.service;

import com.example.server.dao.TagRepository;
import com.example.server.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ImgService imgService;

    public TagService(TagRepository tagRepository, ImgService imgService) {
        this.tagRepository = tagRepository;
        this.imgService = imgService;
    }

    public List getTagsByImgId(Long imgid) {
        List<Tag> tags=tagRepository.getByImgId(imgid);
        List result=new ArrayList<>();
        for(int i=0;i<tags.size();i++)
        {
            Tag tag=tags.get(i);
            Map map = new LinkedHashMap();
            Map position=new LinkedHashMap();
            map.put("tag",tag.getTag());
            map.put("tagName",tag.getTagName());
            position.put("x",tag.getX());
            position.put("x1",tag.getX1());
            position.put("y",tag.getY());
            position.put("y1",tag.getY1());
            map.put("position",position);
            map.put("uuid",tag.getUuid());
            result.add(map);
        }
        return result;
    }

    public void newTags(Long imgid,List data) {
        int j;
        for(j=0;j<data.size();j++){
            Map map=(Map)data.get(j);
            Tag tag=new Tag();
            tag.setTag((String)map.get("tag"));
            tag.setTagName((String)map.get("tagName"));
            tag.setUuid((String)map.get("uuid"));
            Map position=(Map)map.get("position");
            tag.setX((String)position.get("x"));
            tag.setY((String)position.get("y"));
            tag.setX1((String)position.get("x1"));
            tag.setY1((String)position.get("y1"));
            tag.setImgId(imgid);
            tagRepository.save(tag);
        }
        imgService.updateImgState(imgid);
    }
}
