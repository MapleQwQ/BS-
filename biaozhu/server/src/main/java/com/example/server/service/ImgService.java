package com.example.server.service;

import com.example.server.dao.ImgRepository;
import com.example.server.entity.Img;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImgService {
    private final ImgRepository imgRepository;

    public ImgService(ImgRepository imgRepository) {
        this.imgRepository = imgRepository;
    }

    public void newImg(Long taskid, String imgPath ,int state) {
        Img img=new Img();
        img.setTaskId(taskid);
        img.setPictureAddress(imgPath);
        img.setImgState(state);
        imgRepository.save(img);
    }

    public List getAllByTask(Long taskid) {
        return imgRepository.findImgByTaskId(taskid);
    }


    public Optional<Img> getImgById(Long imgid) {
        return imgRepository.findById(imgid);
    }

    public void updateImgState(Long imgId){
        imgRepository.findById(imgId)
                .map(img ->{
                    img.setImgState(1);
                    return imgRepository.save(img);
                }).orElseThrow(() -> new ArticleNotFoundException(imgId));
    }
}
