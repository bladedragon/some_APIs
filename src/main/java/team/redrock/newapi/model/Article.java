package team.redrock.newapi.model;

import lombok.Data;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/24 3:07
 * @description:
 */
@Data
public class Article {
    private String id;
    private String title;
    private String date;
    private String readCount;
    private String author;
    private String content;
    private List<JwFile> files;

    public Article(String id, String title, String subtitle) {
        this.id=id;
        this.title=title;
        Matcher m = Pattern.compile("时间：(.*?)发布人：(.*)阅读数：(\\d+)").matcher(subtitle);
        if(m.find()){
            this.date=m.group(1).trim();
            this.author=m.group(2).trim();
            this.readCount =m.group(3).trim();
        }
    }

    public Article(String id, String title, String time, String readCount) {
        this.id=id;
        this.title=title;
        this.date=time;
        this.readCount =readCount;
    }
}
