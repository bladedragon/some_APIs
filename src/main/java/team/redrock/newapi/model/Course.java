package team.redrock.newapi.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import team.redrock.newapi.util.DateUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/21 18:18
 * @description:
 */
@Data
public class Course {

    private static final String SINGLE = "single";
    private static final String DOUBLE = "double";
    private static final String ALL = "all";

    @SerializedName("hash_day")
    private int hashDay;
    @SerializedName("hash_lesson")
    private int hashLesson;
    @SerializedName("begin_lesson")
    private int beginLesson;
    @SerializedName("course_num")
    private String courseNum;
    private String day;
    private String lesson;
    private String course;
    private String teacher;
    private String classroom;
    private String rawWeek;
    private String weekModel;
    private int weekBegin = 99;
    private int weekEnd = 0;
    private String type;
    private int period;
    private List<Integer> week;

    public Course() {
        this.week = new LinkedList<>();
    }

    public void parseRawLessonAndDay(int lesson, int day) {
        this.hashDay = day - 1;
        this.hashLesson = lesson / 2 + lesson / 5;
        this.beginLesson = 2 * (lesson - lesson / 3) - 1;
        this.lesson = DateUtil.getLesson(lesson);
        this.day = DateUtil.getWeek(day);
    }

    public void parseRawWeek(String rawWeek) {
        this.rawWeek = rawWeek;
        String[] weeks = rawWeek.split(",");
        for (String week : weeks) {

            int periodIndex = week.indexOf("节连上");
            if (periodIndex != -1) {
                String periodStr = week.substring(periodIndex - 1, periodIndex);
                this.rawWeek=week.substring(0,periodIndex-1);
                this.period = Integer.parseInt(periodStr);
            } else {
                this.period = 2;
            }

            int step = 1;
            if (week.contains("单周")) {
                this.weekModel = SINGLE;
                step = 2;
            } else if (week.contains("双周")) {
                this.weekModel = DOUBLE;
                step = 2;
            } else {
                this.weekModel = ALL;
            }

            int index = week.indexOf("-");
            int split = week.indexOf("周");
            if (index != -1) {
                int a = Integer.parseInt(week.substring(0, index));
                int b = Integer.parseInt(week.substring(index + 1, split));

                for (int i = a; i <= b; i += step) {
                    this.week.add(i);
                }
                this.setWeekBegin(a);
                this.setWeekEnd(b);
            } else {
                int day = Integer.parseInt(week.substring(0, split));
                this.week.add(day);
                this.setWeekBegin(day);
                this.setWeekEnd(day);
            }
        }
    }

    public void parseNumAndName(String rawStr) {
        int index = rawStr.indexOf('-');
        this.courseNum = rawStr.substring(0, index);
        this.course = rawStr.substring(index + 1);
    }

    public void parseTeacherAndType(String rawStr) {
        int index = rawStr.indexOf("修");
        if(index==-1){
            index = rawStr.indexOf("实践");
            this.teacher=rawStr.substring(0,index);
            this.type="实践";
        }else{
        this.teacher = rawStr.substring(0, index - 1);
        this.type = rawStr.substring(index - 1, index + 1);
    }}

    private void setWeekBegin(int begin) {
        if (begin < weekBegin) {
            this.weekBegin = begin;
        }
    }

    private void setWeekEnd(int end) {
        if (end > weekEnd) {
            this.weekEnd = end;
        }
    }

}

