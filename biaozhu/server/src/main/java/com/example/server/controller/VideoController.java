package com.example.server.controller;


import com.alibaba.fastjson.JSON;
import com.example.server.service.ImgService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class VideoController {
    ImgService imgService;

    public VideoController(ImgService imgService) {
        this.imgService = imgService;
    }

    @CrossOrigin
    @PostMapping(value = "/uploadvideo/{taskid}")
    @ResponseBody
    public Map<String,String> savaVideotest(@PathVariable("taskid") Long taskid,@RequestParam("file") MultipartFile file, @RequestParam int interval)
            throws IllegalStateException {
//        List file = JSON.parseArray(listJSON);
        Map<String,String> resultMap = new HashMap<>();
        try{
            String coverPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+ taskid+"/";
            File file1 = new File(coverPath);
            if(!file1.exists()){
                file1.mkdir();
            }
//            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
//            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
//                    .toLowerCase();
//            // 重构文件名称
//            System.out.println("前端传递的保存路径："+SavePath);
//            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
//            String newVidoeName = pikId + "." + fileExt;
            String newVidoeName=file.getOriginalFilename();
//            System.out.println("重构文件名防止上传同名文件："+newVidoeName);
            //保存视频
            File fileSave = new File(coverPath, newVidoeName);
            file.transferTo(fileSave);
            String imgPath=getVideoCoverImg(fileSave,interval,taskid,newVidoeName);
            File defile = new File(coverPath+newVidoeName);
            if(defile.delete()){
                System.out.println(defile.getName() + " 文件已被删除！");
            }else{
                System.out.println("文件删除失败！");
            }
            resultMap.put("newVidoeName",newVidoeName);
            resultMap.put("resCode","200");
            return  resultMap;
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("resCode","400");
            return  resultMap ;
        }
    }

    private String getVideoCoverImg(File video,int interval,Long taskId,String videoName) {
        System.out.println("进入截图状态");
        String coverPicPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+ taskId+"/";
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();

            // 截取的帧图片
            int i = 0;
            int FrameLength = ff.getLengthInFrames();
            Long FrameTime=ff.getLengthInTime();
            Long f1=FrameLength*1000L*1000L;
            System.out.println("f1:"+f1);
            System.out.println("FrameTime:"+FrameTime);
            Long FrameRate=f1/FrameTime;
            Frame f;
            int index = 3;
            int[] choose=new int[100000000];
            int choosel=0;

            System.out.println("FrameLength:"+FrameLength);
            System.out.println("interval:"+interval);
            System.out.println("FrameRate"+FrameRate);
            for(i=1;i<FrameLength;i+=FrameRate*interval){
//                System.out.println("choosel"+choosel+"  choose[choosel]"+i);
                choose[choosel]=i;
                choosel++;
            }
            int choosel2=0;
            i=0;
            while (i < FrameLength) {
//                System.out.println("i"+i);
                f = ff.grabImage();
                i++;
//                System.out.println("choose[choosel2]"+choose[choosel2]);
                if(i==choose[choosel2]){
                    System.out.println("choose[choosel2]"+i);
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage srcImage = converter.getBufferedImage(f);
                    File picFile = new File(coverPicPath+videoName+"_第"+choosel2*interval+"秒"+".jpg");
                    ImageIO.write(srcImage, "jpg", picFile);

                    String imgPath2=videoName+"_第"+choosel2*interval+"秒"+".jpg";
                    int state=0;
                    imgService.newImg(taskId,imgPath2,state);
                    choosel2++;
                }
            }
            ff.stop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return coverPicPath;
    }
}