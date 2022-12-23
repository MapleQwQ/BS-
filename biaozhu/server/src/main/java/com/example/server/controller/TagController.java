package com.example.server.controller;

import com.alibaba.fastjson.JSON;
import com.example.server.entity.Img;
import com.example.server.entity.Task;
import com.example.server.service.ImgService;
import com.example.server.service.TagService;
import com.example.server.service.TaskService;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import javax.swing.text.html.Option;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class TagController {
    private final TagService tagService;
    private final ImgService imgService;
    private final TaskService taskService;

    public TagController(TagService tagService, ImgService imgService, TaskService taskService) {
        this.tagService = tagService;
        this.imgService = imgService;
        this.taskService = taskService;
    }

    @GetMapping("/gettags/{imgid}")
    public List getTagsByImgId(@PathVariable Long imgid){
        return tagService.getTagsByImgId(imgid);
    }

    @PostMapping(path = "/newtags/{imgid}")
    public void newTags(@PathVariable("imgid") Long imgid,@RequestBody String listJSON){
        List data = JSON.parseArray(listJSON);
        System.out.println("imgId:"+imgid);
        System.out.println("controllerdata:"+data);
        tagService.newTags(imgid,data);
    }

    @GetMapping("/exportmarkers/{imgid}")
    public List exportMarkers(@PathVariable Long imgid) throws ParserConfigurationException, IOException, ParseException, TransformerException {
        List tags=tagService.getTagsByImgId(imgid);
        createfile(imgid,tags);
        return tags;
    }
    public void createfile(Long imgId,List tags) throws ParserConfigurationException, IOException, TransformerException, ParseException {
        Optional<Img> img=imgService.getImgById(imgId);
        Long taskId=img.get().getTaskId();
        Optional<Task> task=taskService.getTaskById(taskId);
        String imgName=img.get().getPictureAddress();
        String imgPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+taskId+"/"+imgName;
        String taskName=task.get().getTaskName();
        BufferedImage image = ImageIO.read(new File(imgPath));
        ColorModel color = image.getColorModel();
        int imageWidth=image.getWidth();
        int imageHeight=image.getHeight();
        int colorDepth=color.getPixelSize();


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();
        document.setXmlStandalone(true);
        Element annotation = document.createElement("annotation");
        Element folder = document.createElement("folder");
        Element filename = document.createElement("filename");
        Element source = document.createElement("source");
        Element size = document.createElement("size");
        Element segmented = document.createElement("segmented");
        folder.setTextContent("taskId:"+taskId);
        filename.setTextContent(imgName);
        Element database = document.createElement("database");
        database.setTextContent("bs.img");
        source.appendChild(database);
        Element width = document.createElement("width");
        width.setTextContent(imageWidth+"");
        Element height = document.createElement("height");
        height.setTextContent(imageHeight+"");
        Element depth = document.createElement("depth");
        depth.setTextContent(colorDepth+"");
        size.appendChild(width);
        size.appendChild(height);
        size.appendChild(depth);
        segmented.setTextContent("0");
        annotation.appendChild(folder);
        annotation.appendChild(filename);
        annotation.appendChild(source);
        annotation.appendChild(size);
        annotation.appendChild(segmented);

        for(int i=0;i<tags.size();i++){
            Map tag=(Map)tags.get(i);
            Element object = document.createElement("object");
            Element name=document.createElement("name");
            name.setTextContent((String) tag.get("tagName"));
            Element pose=document.createElement("pose");
            pose.setTextContent("Unspecified");
            Element truncated=document.createElement("truncated");
            truncated.setTextContent("0");
            Element difficult=document.createElement("difficult");
            difficult.setTextContent("0");
            Element bndbox=document.createElement("bndbox");
            Element xmin=document.createElement("xmin");
            Element ymin=document.createElement("ymin");
            Element xmax=document.createElement("xmax");
            Element ymax=document.createElement("ymax");
            Map position=(Map)tag.get("position");
            String sxMin=(String)position.get("x");
            String sxMax=(String)position.get("x1");
            String syMin=(String)position.get("y");
            String syMax=(String)position.get("y1");
            NumberFormat nf=NumberFormat.getPercentInstance();
            double xMin=nf.parse(sxMin).doubleValue()*imageWidth;
            double xMax=nf.parse(sxMax).doubleValue()*imageWidth;
            double yMin=nf.parse(syMin).doubleValue()*imageHeight;
            double yMax=nf.parse(syMax).doubleValue()*imageHeight;
            xmin.setTextContent((int)xMin+"");
            ymin.setTextContent((int)yMin+"");
            xmax.setTextContent((int)xMax+"");
            ymax.setTextContent((int)yMax+"");
            bndbox.appendChild(xmin);
            bndbox.appendChild(ymin);
            bndbox.appendChild(xmax);
            bndbox.appendChild(ymax);

            object.appendChild(name);
            object.appendChild(pose);
            object.appendChild(truncated);
            object.appendChild(bndbox);

            annotation.appendChild(object);
        }


        document.appendChild(annotation);

        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf=tff.newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT,"yes");
        tf.transform(new DOMSource(document), new StreamResult(new File("D:/学无止尽/大三上/BS/biaozhu/Pascal Voc/Annotations/"+taskId+"_"+imgName+".xml")));

        String oldPath="D:/学无止尽/大三上/BS/biaozhu/imgs/"+ taskId+"/"+imgName;
        String newPath="D:/学无止尽/大三上/BS/biaozhu/Pascal Voc/JPEGImages/"+taskId+"_"+imgName;
        File temp=new File(oldPath);
        File newTemp=new File(newPath);
        FileInputStream input = new FileInputStream(temp);
        FileOutputStream output = new FileOutputStream(newTemp);
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = input.read(b)) != -1) {
            output.write(b, 0, len);
        }
        output.flush();
        output.close();
        input.close();

        String trainPath="D:/学无止尽/大三上/BS/biaozhu/Pascal Voc/ImageSets/Main/train.txt";
        FileWriter fw=new FileWriter(trainPath,true);
        fw.write("\n"+taskId+"_"+imgName);
        fw.close();
        System.out.println("导出成功");
    }


}
