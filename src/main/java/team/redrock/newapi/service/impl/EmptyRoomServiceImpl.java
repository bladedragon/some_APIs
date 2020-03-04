package team.redrock.newapi.service.impl;

import org.springframework.stereotype.Service;
import team.redrock.newapi.config.AttributeConfig;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.service.EmptyRoomService;
import team.redrock.newapi.util.HttpUtil;
import team.redrock.newapi.util.PatternUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/25 21:25
 * @description:
 */
@Service
public class EmptyRoomServiceImpl implements EmptyRoomService {

    private static final String[] SECTION_TIME_REF = {"12", "34", "56", "78", "910", "1112"};

    private static final String[][] ROOM_ARRAY = {
            {}, {},
            //2教
            {"2305", "2306", "2308", "2309", "2310", "2311", "2314", "2315", "2316", "2201", "2202", "2205", "2206", "2207", "2208", "2209", "2214", "2215", "2216", "2217", "2100", "2101", "2102", "2105", "2106", "2107", "2108", "2109", "2115", "2116", "2117", "2503", "2506", "2507", "2508", "2509", "2510", "2511", "2401", "2402", "2405", "2406", "2408", "2409", "2410", "2411", "2414", "2415", "2416", "2301", "2302"},
            //3教
            {"3201", "3202", "3203", "3204", "3205", "3206", "3207", "3208", "3209", "3210", "3211", "3212", "3101", "3102", "3103", "3104", "3105", "3106", "3107", "3108", "3109", "3110", "3111", "3501", "3502", "3503", "3504", "3505", "3506", "3507", "3508", "3515", "3401", "3402", "3403", "3404", "3405", "3406", "3407", "3408", "3419", "3301", "3302", "3303", "3304", "3305", "3306", "3307", "3308", "3317"},
            //4教
            { "4101", "4102", "4103", "4104", "4105", "4106", "4107", "4501", "4502", "4503", "4504", "4505", "4506", "4507", "4508", "4509", "4510", "4511", "4512", "4513", "4514", "4515", "4516", "4517", "4401", "4402", "4403", "4404", "4405", "4406", "4407", "4408", "4409", "4410", "4411", "4412", "4413", "4414", "4415", "4416", "4417", "4301", "4302", "4303", "4304", "4305", "4306", "4307", "4308", "4309", "4310", "4311", "4312", "4313", "4314", "4315", "4316", "4317", "4201", "4202", "4203", "4204", "4205", "4206", "4207", "4208", "4209", "4210", "4211", "4212", "4213", "4214", "4215", "4216", "4217"},
            //5教
            {"5313", "5601", "5602", "5413", "5200", "5201", "5202", "5203", "5204", "5300", "5205", "5304", "5305", "5401", "5402", "5403", "5404", "5405", "5406"},
            {},
            {},
            //8教
            {"8321", "8322", "8131", "8132", "8133", "8134", "8141", "8142", "8143", "8144", "8151", "8152", "8121", "8122", "8123", "8251", "8124", "8252"},

    };

    private static final String[][] ROOM_SHENQING_ARRAY = {
            {"2101", "2102", "2115", "2116", "2117", "2201", "2202", "2215", "2216", "2217", "2301", "2302", "2315", "2316", "2401", "2402", "2415", "2416"},
            {"4101", "4102", "4103", "4104", "4105", "4106", "4107", "4401", "4402", "4403", "4404", "4405", "4406", "4407", "4408", "4409", "4411", "4412", "4413", "4414", "4415", "4416", "4417", "4501", "4502", "4503", "4504", "4505", "4506", "4507", "4508", "4509", "4510", "4511", "4512", "4513", "4514", "4515", "4516", "4517"}
    };

    private static final int BUILDING_2 = 2;

    private static final int BUILDING_4 = 4;

    private static final Pattern ROOM_NUM_PATTERN = Pattern.compile(">(\\d{4})<");

    private static final Pattern ROOM_NUM_WITH_SIZE_PATTERN = Pattern.compile(">(\\d{4})\\(\\d+\\)<");

    @Override
    public Set<String> findEmptyRoom(int weekDayNum, int[] sectionNumArray, int buildNum, int week) throws NetWorkException {
        Set<String> resSet = new TreeSet<>(Arrays.asList(ROOM_ARRAY[buildNum]));
        for (int section : sectionNumArray) {
            Set<String> classSet = findEmptyRoom(weekDayNum, section, buildNum, week);
            resSet.retainAll(classSet);
        }
        return resSet;
    }

    public Set<String> findEmptyRoom(int weekDayNum, int sectionNum, int buildNum, int week) throws NetWorkException {
        String res1 = HttpUtil.sendGet(AttributeConfig.getDomain() + "/kebiao/kb_zcsd.php?zcStart=" + week + "&zcEnd=" + week + "&xq=" + weekDayNum + "&js=" + SECTION_TIME_REF[sectionNum] + "&yxh=&type=kk");

        if (res1 == null) {
            throw new NetWorkException();
        }

        Matcher m1 = PatternUtil.TR_PATTERN.matcher(res1);
        Set<String> classSet = new TreeSet<>(Arrays.asList(ROOM_ARRAY[buildNum]));
        while (m1.find()) {
            String course = m1.group(1);
            Matcher m2 = ROOM_NUM_PATTERN.matcher(course);
            if (m2.find()) {
                classSet.remove(m2.group(1));
            }
        }

        if (buildNum == BUILDING_2 || buildNum == BUILDING_4) {
            String res2 = HttpUtil.sendGet(AttributeConfig.getDomain() + "/jssq/jssqEmptyRoom.php?action=jssq&reason=%E7%8F%AD%E7%BA%A7%E5%AD%A6%E7%94%9F%E6%B4%BB%E5%8A%A8&content=&dw=&fzr=&tel=&zc=" + week + "&xq=" + weekDayNum + "&sd=" + SECTION_TIME_REF[sectionNum]);

            if (res2 == null) {
                throw new NetWorkException();
            }

            Matcher m3 = ROOM_NUM_WITH_SIZE_PATTERN.matcher(res2);
            Set<String> set = new TreeSet<>(Arrays.asList(ROOM_SHENQING_ARRAY[buildNum / 3]));
            while (m3.find()) {
                String room = m3.group(1);
                if (room.charAt(0) == (char) (buildNum + 48)) {
                    set.remove(room);
                }
            }
            classSet.removeAll(set);
        }
        return classSet;
    }

//    public static void main(String[] args) {
//        new EmptyRoomServiceImpl().findEmptyRoom(3, 1, 2, 5);
//    }


    private static final void findAllRoomsInKebiao(int build) {
        //找所有的教室，以java数组代码打印出来
        String res = HttpUtil.sendGet(AttributeConfig.getDomain() + "/kebiao/index.php").replaceAll("\\s+", "");
        Matcher m1 = ROOM_NUM_PATTERN.matcher(res);
        Set<Integer> hashSet = new HashSet<>();
        while (m1.find()) {
            Integer i = Integer.parseInt(m1.group(1));
            if (i >= build * 1000 && i <= (build + 1) * 1000) {
                hashSet.add(i);
            }
        }
        StringBuilder sb = new StringBuilder("{");
        for (Integer i : hashSet) {
            sb.append("\"").append(i).append("\"").append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        System.out.println(sb.toString());
    }

    private static final void findAllRoomsInShenqing(int build) {
        String res = HttpUtil.sendGet(AttributeConfig.getDomain() + "/jssq/jssqEmptyRoom.php?action=jssq&reason=%E7%8F%AD%E7%BA%A7%E5%AD%A6%E7%94%9F%E6%B4%BB%E5%8A%A8&content=&dw=&fzr=&tel=&zc=11&xq=6&sd=12");
        Matcher m1 = ROOM_NUM_WITH_SIZE_PATTERN.matcher(res);
        StringBuilder sb = new StringBuilder("{");
        while (m1.find()) {
            String clazz = m1.group(1);
            if (clazz.charAt(0) == (char) (build + 48)) {
                sb.append("\"").append(m1.group(1)).append("\"").append(",");
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        System.out.println(sb.toString());
    }

//    public static void main(String[] args) {
//        findAllRoomsInShenqing(4);
//    }

}
