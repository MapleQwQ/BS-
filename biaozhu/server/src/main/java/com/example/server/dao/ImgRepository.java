package com.example.server.dao;

import com.example.server.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {
    @Query("SELECT s FROM Img s WHERE s.taskId=?1")
    List findImgByTaskId(Long taskid);
}
