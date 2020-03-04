package team.redrock.newapi.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: Shiina18
 * @date: 2019/3/21 18:23
 * @description:
 */
public class StreamUtil {
    public static String readStream(InputStream in) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        in, StandardCharsets.UTF_8
                )
        );
        StringBuilder sb = new StringBuilder();
        String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return sb.toString();
    }

    public static void writeAndFlush(OutputStream out, String data) {
        try {
            out.write(data.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
