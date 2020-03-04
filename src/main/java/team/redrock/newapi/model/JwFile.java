package team.redrock.newapi.model;

import lombok.Data;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Shiina18
 * @date: 2019/3/24 18:15
 * @description:
 */
@Data
public class JwFile {
    String id;
    String name;
    InputStream inputStream;
    Map<String,String> header;
}
