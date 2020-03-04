package team.redrock.newapi.been;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.redrock.newapi.model.Course;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: Shiina18
 * @date: 2019/3/21 20:19
 * @description:
 */
@Component
@Slf4j
public class CourseFactory {

    private static final int ONE_COURSE_LINE_NUM = 6;

    private static final int NUM_AND_NAME_IN_COURSE_ORDER = 1;
    private static final int CLASS_ROOM_IN_COURSE_ORDER = 2;
    private static final int RAW_WEEK_IN_COURSE_ORDER = 3;
    private static final int TEACHER_AND_TYPE_IN_COURSE_ORDER = 4;

    private static final int CLASS_ROOM_TRIE_LENGTH = 3;

    private static final String ONE_COURSE_END_LINE = "选课学生名单";


    public Course[] createCourse(String[] oneCourse, int day, int lesson) {
        Set<Course> courseSet = new LinkedHashSet<>();
        if (oneCourse.length > 1) {
            int flag = 1;
            for (int z = 0; z < oneCourse.length / ONE_COURSE_LINE_NUM; z++) {
                try {
                    Course course = new Course();
                    String classRoom = oneCourse[flag + CLASS_ROOM_IN_COURSE_ORDER];
                    String rawWeek = oneCourse[flag + RAW_WEEK_IN_COURSE_ORDER];
                    course.parseNumAndName(oneCourse[flag + NUM_AND_NAME_IN_COURSE_ORDER]);
                    course.parseRawLessonAndDay(lesson, day);
                    course.setClassroom(classRoom.substring(CLASS_ROOM_TRIE_LENGTH));
                    course.parseTeacherAndType(oneCourse[flag + TEACHER_AND_TYPE_IN_COURSE_ORDER]);

                    course.parseRawWeek(rawWeek);

                    flag += 5;
                    courseSet.add(course);


                    while (!ONE_COURSE_END_LINE.equals(oneCourse[flag])) {
                        flag++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return courseSet.toArray(new Course[0]);
                }

                    flag++;

            }
        }
        return courseSet.toArray(new Course[0]);
    }

}
