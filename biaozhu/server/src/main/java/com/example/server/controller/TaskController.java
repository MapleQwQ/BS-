package com.example.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.server.entity.Img;
import com.example.server.entity.Tag;
import com.example.server.entity.Task;
import com.example.server.service.ImgService;
import com.example.server.service.TagService;
import com.example.server.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.*;

@RestController
public class TaskController {
    private final TaskService taskService;
    private final ImgService imgService;
    private final TagService tagService;
    private final TagController tagController;

    public TaskController(TaskService taskService, ImgService imgService, TagService tagService, TagController tagController) {
        this.taskService = taskService;
        this.imgService = imgService;
        this.tagService = tagService;
        this.tagController = tagController;
    }

    @GetMapping("/tasks")
    List allTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/task/{taskId}")
    Optional<Task> getTaskById(@PathVariable Long taskId){
        return taskService.getTaskById(taskId);
    }

    @GetMapping("/exportcoco/{taskId}")
    public Optional<Task> exportCoco(@PathVariable Long taskId) throws IOException {
        Optional<Task> task=taskService.getTaskById(taskId);
        createCoco(taskId);
        return task;
    }

    @GetMapping("/exportpv/{taskId}")
    public Optional<Task> exportPv(@PathVariable Long taskId) throws IOException, ParserConfigurationException, ParseException, TransformerException {
        Optional<Task> task=taskService.getTaskById(taskId);
        List imgs=imgService.getAllByTask(taskId);
        for(int i=0;i<imgs.size();i++){
            Img img=(Img) imgs.get(i);
            Long imgId=img.getImgId();
            List tags=tagService.getTagsByImgId(imgId);
            tagController.createfile(imgId,tags);
        }
        return task;
    }

    @PutMapping(path="/tasks/{taskId}")
    public Task updateTask(
            @PathVariable Long taskId,@Valid @RequestBody Task newTask){
        return taskService.updateById(taskId,newTask);
    }

    @PostMapping("/tasks")
    public void newTask(@RequestBody Task task){
        taskService.addTask(task);
    }

    public void createCoco(Long taskId) throws IOException {
        Optional<Task> task = taskService.getTaskById(taskId);
        String taskName = task.get().getTaskName();
        String taskPath = "D:/学无止尽/大三上/BS/biaozhu/COCO/" + taskName + "/";
        File file1 = new File(taskPath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String oldPath = "D:/学无止尽/大三上/BS/biaozhu/imgs/" + taskId + "/";
        File a = new File(oldPath);
        String[] imgsFile = a.list();
        File temp = null;
        for (int i = 0; i < imgsFile.length; i++) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + imgsFile[i]);
            } else {
                temp = new File(oldPath + File.separator + imgsFile[i]);
            }
            if (temp.isFile()) {
                String fileName = taskPath + "/" + (temp.getName()).toString();
                File testFile = new File(fileName);
                if (!testFile.exists()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(fileName);
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
            }
        }

        List images=new ArrayList<>();
        List annotations=new ArrayList<>();
        List categories=new ArrayList<>();
        Map result=new LinkedHashMap();
        List imgs = imgService.getAllByTask(taskId);
        for(int i=0;i<imgs.size();i++)
        {
            Img aa=(Img) imgs.get(i);
            String imgName=aa.getPictureAddress();
            String imgPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+taskId+"/"+imgName;
            System.out.println(imgPath);
            BufferedImage image = ImageIO.read(new File(imgPath));
            int imageWidth=image.getWidth();
            int imageHeight=image.getHeight();

            Map aimg=new LinkedHashMap();
            aimg.put("file_name",aa.getPictureAddress());
            aimg.put("url",imgPath);
            aimg.put("height",imageHeight);
            aimg.put("width",imageWidth);
            images.add(aimg);

            Long aimgId=aa.getImgId();
            List tags=tagService.getTagsByImgId(aimgId);
            for(int j=0;j<tags.size();j++){
                Map t=(Map)tags.get(j);
                Map cat=new LinkedHashMap();
                cat.put("id",t.get("tag"));
                cat.put("name",t.get("tagName"));
                categories.add(cat);

                Map ann=new LinkedHashMap();
                List bb=new ArrayList<>();
                Map position=(Map) t.get("position");
                bb.add(position.get("x"));
                bb.add(position.get("x1"));
                bb.add(position.get("y"));
                bb.add(position.get("y1"));
                ann.put("segmentation","[[x1, y1, x2, y1, x2, y2, x1, y2]]");
                ann.put("area",240.0);
                ann.put("iscrowd",0);
                ann.put("image_id",aa.getImgId());
                ann.put("bbox",bb);
                ann.put("category_id",t.get("tag"));
                ann.put("id",t.get("uuid"));
                annotations.add(ann);
            }

        }
        result.put("images",images);
        result.put("annotations",annotations);
        result.put("categories",categories);

        Tool tool=new Tool();

        JSONObject json =new JSONObject(result);

        String instancePath = "D:/学无止尽/大三上/BS/biaozhu/COCO/annotation/instances_" + taskName + ".json";
        File nfile=new File(instancePath);
        if (!nfile.exists()) {
            nfile.createNewFile();
        }

        FileOutputStream fileOutputStream=new FileOutputStream(nfile);//实例化FileOutputStream
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
        BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
        String jsonString=json.toString();//将jsonarray数组转化为字符串
        String JsonString=tool.stringToJSON(jsonString);//将jsonarrray字符串格式化
        bufferedWriter.write(JsonString);//将格式化的jsonarray字符串写入文件
        bufferedWriter.flush();//清空缓冲区，强制输出数据
        bufferedWriter.close();//关闭输出流

//        FileWriter fw = new FileWriter(instancePath);
//        fw.write(json);
//        fw.flush();


        
    }
}
