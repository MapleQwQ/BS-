package com.example.server.dao;

import com.example.server.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List getByImgId(Long imgid);
}
