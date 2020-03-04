package team.redrock.newapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import team.redrock.newapi.annotation.RedrockCache;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Article;
import team.redrock.newapi.model.JwFile;
import team.redrock.newapi.model.JwImage;
import team.redrock.newapi.model.response.NewsResponseEntity;
import team.redrock.newapi.model.response.ResponseEntity;
import team.redrock.newapi.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:43
 * @description:
 */
@RestController
@RequestMapping("/jwNews")
@Slf4j
public class NewsController {

    @Autowired
    private NewsService newsService;


    //@RedrockCache(minDuration = 60 * 2,maxDuration = 60*3)
    @GetMapping("/list")
    public ResponseEntity jwNewsList(int page) {
        log.info("有人查看了[{}]页的公告，解析教务在线",page);
        ResponseEntity<List<Article>> res = new NewsResponseEntity<>(page);
        if (page > 0) {
            List<Article> articleList = null;
            try {
                articleList = newsService.parseNewsListJson(page);
                System.out.println(articleList);
                res.setData(articleList);
            } catch (NetWorkException e) {
                log.error(e.getMessage()+"\n"+e.toString());
                e.printStackTrace();
                res = null;
            }
        }
        return res;
    }

    @GetMapping("/content")
    @RedrockCache(minDuration = 20,maxDuration = 30)
    public ResponseEntity jwNewsContent(String id) {
        log.info("有人查看了id:[{}]的公告，解析教务在线");
        ResponseEntity<Article> res = new NewsResponseEntity<>(id);
        Article article = newsService.findArticle(id);
        res.setData(article);
        return res;
    }

    @GetMapping("/image")
    public void jwNewsImage(String key, HttpServletResponse response) {
        if (key != null) {
            try {
                JwImage jwImage = newsService.getJwImage(key);
                response.setHeader("Content-Type", jwImage.getType());
                IOUtils.copy(jwImage.getInputStream(), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/file")
    public void jwNewsFile(String id, HttpServletResponse response) {
        if (id != null && !"".equals(id)) {
            JwFile file = newsService.getJwFile(id);
            OutputStream out = null;
            InputStream in = null;
            try {
                out = response.getOutputStream();
                in = file.getInputStream();
                for (Map.Entry<String, String> entry : file.getHeader().entrySet()) {
                    response.setHeader(entry.getKey(), entry.getValue());
                }
                IOUtils.copy(in, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
