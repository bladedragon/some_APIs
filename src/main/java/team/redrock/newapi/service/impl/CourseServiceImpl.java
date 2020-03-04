package team.redrock.newapi.service.impl;

import org.springframework.stereotype.Service;
import team.redrock.newapi.been.CourseFactory;
import team.redrock.newapi.config.AttributeConfig;
import team.redrock.newapi.controller.CourseController;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Course;
import team.redrock.newapi.service.CourseService;
import team.redrock.newapi.util.HttpUtil;
import team.redrock.newapi.util.PatternUtil;
import team.redrock.newapi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/21 18:20
 * @description:
 */
@Service
public class CourseServiceImpl implements CourseService {

    private static final String ICS_BEGIN = "BEGIN:VCALENDAR\nCALSCALE:GREGORIAN\nVERSION:3.0\nMETHOD:PUBLISH\nX-WR-CALNAME:Redrock Schedule\nX-WR-TIMEZONE:Asia/Shanghai\nX-APPLE-CALENDAR-COLOR:#9933CC\n";
    private static final String ICS_END = "END:VCALENDAR\n";
    private static final String ICS_EVENT_BEGIN = "BEGIN:VEVENT\n";
    private static final String ICS_EVENT_END = "END:VEVENT\n";

    private static final String[] HASH_LESSON_TO_BEGIN_LESSON = new String[]{"T080000", "T101500", "T140000", "T161500", "T190000", "T205000"};
    private static final String[] HASH_LESSON_TO_END_LESSON = new String[]{"T094000", "T115500", "T154000", "T194500", "T204000", "T223000"};
    private static final long ONE_WEEK_SECOND = 604800;

    private static final Pattern TABLE_PATTERN = Pattern.compile("l'>(.*?)\"printTable\"");


    @Value("${cqupt.term-begin}")
    private long termBegin = 1551024000L;

    @Autowired
    private CourseFactory courseFactory = new CourseFactory();

    @Override
    public List<Course> parseCourseHtml(String stuNum) throws NetWorkException {
        List<Course> data = new LinkedList<>();
        String url = AttributeConfig.getDomain() + "/kebiao/kb_stu.php?xh=" + stuNum;
        String html = HttpUtil.sendGet(url);
        if (html == null) {
            throw new NetWorkException();
        }
        html = html.replaceAll("([\n\t])", "").replaceAll(" ", "");
        Matcher m = TABLE_PATTERN.matcher(html);
        if (m.find()) {
            Matcher m1 = PatternUtil.TR_PATTERN.matcher(m.group(1));
            if (m1.find()) {
                int week = 0;
                while (m1.find()) {
                    week++;
                    String courseStr = m1.group(1);
                    Matcher m2 = PatternUtil.TD_PATTERN.matcher(courseStr);
                    if (m2.find()) {
                        int lesson = 1;
                        while (m2.find()) {
                            String[] oneCourse = m2.group(1).replaceAll("<font.*?>", "").replaceAll("<.*?>", ";").split(";+");
                            Course[] courses = courseFactory.createCourse(oneCourse, lesson++, week);
                            if (courses != null&&courses.length!=0) {
                                data.addAll(Arrays.asList(courses));
                            }
                        }
                    }
                }
            }
        }
        return data;
    }

    @Override
    public int nowWeek() {
        long now = System.currentTimeMillis() / 1000;
        return (int) ((now - termBegin) / ONE_WEEK_SECOND) + 1;
    }

    @Override
    public String courseToIcs(List<Course> courseList) {
        StringBuilder sb = new StringBuilder(ICS_BEGIN);
        for (Course course : courseList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(termBegin * 1000));
            calendar.add(Calendar.DAY_OF_MONTH, course.getHashDay());
            int before = 1;
            for (Integer week : course.getWeek()) {
                calendar.add(Calendar.WEEK_OF_MONTH, week - before);
                before = week;
                sb.append(ICS_EVENT_BEGIN);
                String createTime = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(new Date());
                String date = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
                sb.append("DTSTART:").append(date).append(HASH_LESSON_TO_BEGIN_LESSON[course.getHashLesson()]).append("\n");
                sb.append("DTEND:").append(date).append(HASH_LESSON_TO_END_LESSON[course.getHashLesson()]).append("\n");
                sb.append("DTSTAMP:").append(createTime).append("\n");
                sb.append("UID:").append(UUID.randomUUID().toString()).append("\n");
                sb.append("CREATED:").append(createTime).append("\n");
                sb.append("DESCRIPTION:").append(StringUtil.arrayToString(new String[]{course.getTeacher(), course.getRawWeek(), course.getType()}, ", ")).append("\n");
                sb.append("LOCATION:@").append(course.getClassroom()).append("\n");
                sb.append("SUMMARY:").append(course.getCourse()).append("\n");
                sb.append(ICS_EVENT_END);
            }
        }
        sb.append(ICS_END);
        return sb.toString();
    }

}
