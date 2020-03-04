package team.redrock.newapi.model.response;

import lombok.Data;
import team.redrock.newapi.config.AttributeConfig;
import team.redrock.newapi.model.Course;

import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/21 18:09
 * @description:
 */
@Data
public class CourseResponseEntity extends ResponseEntity<List<Course>> {

    private static final String TERM = AttributeConfig.getTerm();

    private static final String VERSION = AttributeConfig.getVersion();

    private final String term = TERM;

    private final String version = VERSION;

    private String stuNum;

    private int nowWeek;

    private boolean success;
    public CourseResponseEntity(){
        super();
        this.success = true;
    }
}
