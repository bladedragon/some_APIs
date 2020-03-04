package team.redrock.newapi.service;

import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Article;
import team.redrock.newapi.model.JwFile;
import team.redrock.newapi.model.JwImage;

import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/24 3:12
 * @description:
 */
public interface NewsService {

    /**
     * 解析教务在线公告的json
     *
     * @param page 第几页
     * @return 新闻的list
     * @throws NetWorkException 网络不通畅
     */
    List<Article> parseNewsListJson(int page) throws NetWorkException;

    /**
     * 根据id查找具体公告内容
     *
     * @param id 文章id
     * @return 文章
     */
    Article findArticle(String id);

    /**
     * 获得教务在线的附件
     *
     * @param id 附件id
     * @return 附件
     */
    JwFile getJwFile(String id);

    /**
     * 获得教务在线的图片
     *
     * @param key 图片的key
     * @return 图片信息
     */
    JwImage getJwImage(String key);
}
