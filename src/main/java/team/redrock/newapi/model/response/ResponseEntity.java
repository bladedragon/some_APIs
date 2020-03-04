package team.redrock.newapi.model.response;

import lombok.Data;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:46
 * @description:
 */
@Data
public class ResponseEntity<T> {

    public static final ResponseEntity NETWORK_ERROR = new ResponseEntity(10010,"network error");
    protected static final String SUCCESS = "success";


    public static final ResponseEntity AUTHENTICATION_ERROR_ENTITY = new ResponseEntity(201,"authentication error");
    public static final ResponseEntity PARAM_ERROR = new ResponseEntity(10001,"param error");
    protected int status;

    protected String info;

    protected T data;

    public ResponseEntity() {
        this.status = 200;
    }

    public ResponseEntity(int status,String info){
        this.status=status;
        this.info=info;
    }


}
