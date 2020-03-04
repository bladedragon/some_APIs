package team.redrock.newapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:50
 * @description:
 */
@Component
public class AttributeConfig {
    private static String term;

    private static String version;

    private static String domain;

    public static String getDomain() {
        return domain;
    }

    @Value("${cqupt.term}")
    public void setTerm(String term){
        System.out.println(term);
        AttributeConfig.term = term;
    }

    @Value("${cqupt.domain}")
    public void setDomain(String domain){
        AttributeConfig.domain=domain;
    }

    @Value("${cqupt.version}")
    public void setVersion(String version){
        AttributeConfig.version =version;
    }

    public static String getTerm(){
        return term;
    }

    public static String getVersion(){
        return version;
    }
}
