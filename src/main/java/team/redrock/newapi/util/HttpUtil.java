package team.redrock.newapi.util;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author: Shiina18
 * @date: 2019/3/21 18:22
 * @description:
 */
 @Slf4j
public class HttpUtil {
    public static String sendPost(String url, Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String result = sb.toString();
        return sendPost(url, result.substring(0, result.length() - 1));
    }

    public static String sendPost(String urlStr, String data) {
        String result = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            StreamUtil.writeAndFlush(con.getOutputStream(), data);
            result = StreamUtil.readStream(con.getInputStream());
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String sendGet(String urlStr) {
        String result = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            result = StreamUtil.readStream(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
