package team.redrock.newapi.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.newapi.annotation.RedrockCache;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Grade;
import team.redrock.newapi.model.response.ExamGradeResponseEntity;
import team.redrock.newapi.model.response.ResponseEntity;
import team.redrock.newapi.service.ExamService;
import team.redrock.newapi.service.impl.ExamServiceImpl;
import team.redrock.newapi.util.HttpUtil;
import team.redrock.newapi.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:42
 * @description:
 */
@RestController
@Slf4j
public class ExamController {

    @Autowired
    private ExamService examService = new ExamServiceImpl();

    @PostMapping("/examGrade")
    @RedrockCache
    public ResponseEntity examGrade(String stuNum, String idNum) throws NetWorkException {
        if (StringUtil.isNotEmpty(stuNum, idNum)) {
            log.info("学号[{}]查看考试成绩，解析教务在线", stuNum);
            if (examService.authentication(stuNum, idNum)) {
                List<Grade> gradeList = examService.findGrade(stuNum);
                return new ExamGradeResponseEntity(stuNum, idNum, gradeList);
            } else {
                //authentication error
                return ResponseEntity.AUTHENTICATION_ERROR_ENTITY;
            }
        }
        return ResponseEntity.PARAM_ERROR;
    }

}
