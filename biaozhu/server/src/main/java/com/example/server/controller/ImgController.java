package com.example.server.controller;

import com.example.server.entity.Img;
import com.example.server.entity.Task;
import com.example.server.service.ImgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ImgController {

    ImgService imgService;

    public ImgController(ImgService imgService) {
        this.imgService = imgService;
    }

    @ResponseBody
    @RequestMapping("/uploadimgs/{taskid}")
    public void uploadPics(@PathVariable("taskid") Long taskid,HttpServletRequest request,
                           @RequestParam("file") MultipartFile[] file) {
//        System.out.println("taskid:"+taskid);
        if (file != null && file.length > 0) {
            for (MultipartFile temp : file) {
                //将文件先保存下来
                try{
                    String strPath = "D:/学无止尽/大三上/BS/biaozhu/imgs/"+ taskid+"/";

                    File file1 = new File(strPath);
                    if(!file1.exists()){
                        file1.mkdir();
                    }
                    String str=temp.getOriginalFilename();
                    String str2=str.substring(0,str.length()-4);
                    String newName=str2+".jpg";
                    String imgPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+ taskid+"/"+newName;
                    temp.transferTo(new File(imgPath));
//                    String imgPath2="../../../imgs/"+ taskid+"/"+temp.getOriginalFilename();
                    String imgPath2=newName;
                    int state=0;
                    imgService.newImg(taskid,imgPath2,state);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/showimgs/{taskid}")
    List all(@PathVariable Long taskid) {
        return imgService.getAllByTask(taskid);
    }

    @GetMapping("/showimg/{imgid}")
    Optional<Img> getOne(@PathVariable Long imgid) {
        System.out.println(imgid);
        return imgService.getImgById(imgid);
    }

    @PutMapping(path="/changestate/{imgId}")
    public void updateImgState(
            @PathVariable Long imgId){
        imgService.updateImgState(imgId);
    }
}
