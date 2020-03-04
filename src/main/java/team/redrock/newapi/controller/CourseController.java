package team.redrock.newapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import team.redrock.newapi.annotation.RedrockCache;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Course;
import team.redrock.newapi.model.response.CourseResponseEntity;
import team.redrock.newapi.model.response.ResponseEntity;
import team.redrock.newapi.service.CourseService;
import team.redrock.newapi.service.impl.CourseServiceImpl;


import java.io.*;
import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:42
 * @description:
 */
@RestController
@Slf4j
public class CourseController {
    @Autowired
    CourseService courseService = new CourseServiceImpl();

    //@RedrockCache(minDuration = 60 * 2, maxDuration = 60 * 3)
    @PostMapping("/kebiao")
    public ResponseEntity courseJson(@RequestParam(value = "stu_num", required = false) String stuNum, @RequestParam(value = "stuNum", required = false) String stuNum2) throws NetWorkException {
        if (stuNum == null || stuNum.length() != 10) {
            stuNum = stuNum2;
        }

        log.info("student:[{}]查找课表，解析教务在线", stuNum);

        List<Course> courseList = null;
        courseList = courseService.parseCourseHtml(stuNum);
        int nowWeek = courseService.nowWeek();
        CourseResponseEntity entity = new CourseResponseEntity();
        entity.setStuNum(stuNum);
        entity.setNowWeek(nowWeek);
        entity.setData(courseList);
//        }
        return entity;
    }

    @RedrockCache(minDuration = 60 * 20, maxDuration = 60 * 30)
    @GetMapping("/kebiao_ics")
    public String courseIcs(@RequestParam("xh") String stuNum) throws NetWorkException {
        log.info("student:[{}]查找ics课表，解析教务在线", stuNum);

        List<Course> courseList = courseService.parseCourseHtml(stuNum);
        return courseService.courseToIcs(courseList);
    }

    @GetMapping(value = "/schoolCalendar", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] schoolCalendar() {
        File calendarFile = new File("/data/calendar.png");
        FileInputStream in = null;
        try {
            in = new FileInputStream(calendarFile);
            byte[] bytes = new byte[in.available()];
            in.read(bytes, 0, in.available());
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
