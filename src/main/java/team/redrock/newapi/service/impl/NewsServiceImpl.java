package team.redrock.newapi.service.impl;

import com.google.gson.GsonBuilder;
import team.redrock.newapi.config.AttributeConfig;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Article;
import team.redrock.newapi.model.JwFile;
import team.redrock.newapi.model.JwImage;
import team.redrock.newapi.service.NewsService;
import team.redrock.newapi.util.HttpUtil;
import team.redrock.newapi.util.UnicodeUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/24 3:23
 * @description:
 */
@Service
public class NewsServiceImpl implements NewsService {

    private static final String JWZX_NEWS_LIST_URL = AttributeConfig.getDomain() + "/data/json_files.php?pageNo=${}&pageSize=20&searchKey=";

    private static final String JWZX_NEWS_CONTENT_URL = AttributeConfig.getDomain() + "/fileShowContent.php?id=${}";

    private static final String REPLACE_PARAM_REGEX = "\\$\\{}";

    private static final Pattern PARAM_PATTERN = Pattern.compile("\"fileId\":(\\d+).*?\"title\":\"(.*?)\".*?\"pubTime\":\"(.*?)\".*?\"readCount\":(\\d+)");

    private static final Pattern MAIN_PANEL_PATTERN = Pattern.compile("mainPanel\"><h3>(.*?)</h3>.*?<p style=\"text-align: center;\">(.*?)</p>(.*?)</div>");

    private static final Pattern IMG_PATTERN = Pattern.compile("src=\"(.*?)\"");

    private static final Pattern P_PATTERN = Pattern.compile("<p.*?>(.*?)</p>");

    private static final Pattern FILE_LIST_PATTERN = Pattern.compile("附件：(.*?)</ul>");

    private static final Pattern FILE_PATTERN = Pattern.compile("id=(\\d+).*?blank>(.*?)<");


    @Override
    public List<Article> parseNewsListJson(int page) throws NetWorkException {

        List<Article> articleList = new ArrayList<>(20);

        System.out.println(JWZX_NEWS_LIST_URL.replaceAll(REPLACE_PARAM_REGEX, String.valueOf(page)));

        String json = HttpUtil.sendGet(JWZX_NEWS_LIST_URL.replaceAll(REPLACE_PARAM_REGEX, String.valueOf(page)));
        if (json == null) {
            throw new NetWorkException();
        }

        json = UnicodeUtil.decode(json);

        Matcher matcher = PARAM_PATTERN.matcher(json);

        while (matcher.find()) {
            String id = matcher.group(1);
            String title = matcher.group(2);
            String time = matcher.group(3);
            String readCount = matcher.group(4);
            Article article = new Article(id, title,time, readCount);
            articleList.add(article);
        }
        return articleList;
    }

    @Override
    public Article findArticle(String id) {
        Article article = null;
        String html = HttpUtil.sendGet(JWZX_NEWS_CONTENT_URL.replaceAll(REPLACE_PARAM_REGEX, id));
        html = html.replaceAll("[\t]", "");
        Matcher matcher = MAIN_PANEL_PATTERN.matcher(html);
        if (matcher.find()) {
            String title = matcher.group(1);
            String subtitle = matcher.group(2);
            String contentHtml = matcher.group(3);
            article = new Article(id, title, subtitle);
            StringBuilder sb = new StringBuilder();
            Matcher m1 = P_PATTERN.matcher(contentHtml);
            while (m1.find()) {
                String content = m1.group(1);
                if (!content.contains("<img") || content.contains("Smashy_029.png")) {
                    content = content.replaceAll("<.*?>", "").replaceAll("&nbsp;", " ").replaceAll("附件：", "");
                } else {
                    Matcher m = IMG_PATTERN.matcher(content);
                    StringBuilder sb1 = new StringBuilder();
                    while (m.find()) {
                        String imgKey = m.group(1).substring(9).replaceAll("\\\\", "_");
                        sb1.append("${").append(imgKey).append("}");
                    }
                    content = sb1.toString();
                }
                sb.append(content).append("\n");
            }
            article.setContent(sb.toString().replaceAll("\\n+", "\n"));
            List<JwFile> fileList = parseFileHtml(contentHtml);
            article.setFiles(fileList);
        }
        return article;
    }


    @Override
    public JwFile getJwFile(String id) {
        JwFile jwFile = null;
        InputStream in = null;
        try {
            URL url = new URL(AttributeConfig.getDomain() + "/fileDownLoadAttach.php?id=" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            in = con.getInputStream();
            String disposition = con.getHeaderField("Content-Disposition");
            String length = con.getHeaderField("Content-Length");
            String type = con.getHeaderField("Content-Type");
            Map<String, String> headerMap = new HashMap<>(3);
            headerMap.put("Content-Disposition", disposition);
            headerMap.put("Content-Length", length);
            headerMap.put("Content-Type", type);
            jwFile = new JwFile();
            jwFile.setInputStream(in);
            jwFile.setHeader(headerMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jwFile;
    }

    @Override
    public JwImage getJwImage(String key) {
        JwImage jwImage = new JwImage();
        try {
            URL url = new URL(AttributeConfig.getDomain() + "/filesImg/" + key.replaceAll("_", "/"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            jwImage.setInputStream(con.getInputStream());
            jwImage.setType(con.getHeaderField("Content-Type"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jwImage;
    }

    private List<JwFile> parseFileHtml(String html) {
        List<JwFile> fileList = new ArrayList<>();
        Matcher m = FILE_LIST_PATTERN.matcher(html);
        if (m.find()) {
            Matcher m2 = FILE_PATTERN.matcher(m.group(1));
            while (m2.find()) {
                String fileId = m2.group(1);
                String name = m2.group(2).replaceAll("\\(.*?\\)", "");
                JwFile jwFile = new JwFile();
                jwFile.setId(fileId);
                jwFile.setName(name);
                saveFileInfo(jwFile);
                fileList.add(jwFile);
            }
        }
        return fileList;
    }

    private void saveFileInfo(JwFile jwFile) {

    }

}
