package team.redrock.newapi.model;

import lombok.Data;

import java.io.InputStream;

/**
 * @author: Shiina18
 * @date: 2019/3/24 20:09
 * @description:
 */
@Data
public class JwImage {
private String name;
private String type;
private InputStream inputStream;
}
