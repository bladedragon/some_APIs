package team.redrock.newapi.service.impl;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Grade;
import team.redrock.newapi.service.ExamService;
import team.redrock.newapi.util.HttpUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/25 19:45
 * @description:
 */
@Service
public class ExamServiceImpl implements ExamService {

    private static final String URL = "http://hongyan.cqupt.edu.cn/api/verify";

    private static final Pattern EXAM_PATTERN = Pattern.compile("类>(.*?)<.*?程名>(.*?)<.*?绩>(.*?)<");

    @Override
    public boolean authentication(String stuNum, String idNum) {
        Map<String, String> param = new LinkedHashMap<>(2);
        param.put("stuNum", stuNum);
        param.put("idNum", idNum);
        String res = HttpUtil.sendPost(URL, param);
        return res.contains("200");
    }

    @Override
    public List<Grade> findGrade(String stuNum) throws NetWorkException {

        List<Grade> gradeList = new LinkedList<>();

        String xml = HttpUtil.sendGet("http://172.22.181.30:81/search.php?type=qmcj&xh=" + stuNum);
        if (xml != null) {
            Matcher m = EXAM_PATTERN.matcher(xml);
            while (m.find()) {
                String property = m.group(1);
                String course = m.group(2);
                String gradeNum = m.group(3);
                Grade grade = new Grade(stuNum, property, course, gradeNum);
                gradeList.add(grade);
            }
            return gradeList;
        }
        throw new NetWorkException();
    }

}
