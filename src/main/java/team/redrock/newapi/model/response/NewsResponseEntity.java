package team.redrock.newapi.model.response;

import lombok.Data;

/**
 * @author: Shiina18
 * @date: 2019/3/24 3:15
 * @description:
 */
@Data
public class NewsResponseEntity<E> extends ResponseEntity<E> {
    private String id;
    private Integer page;

    public NewsResponseEntity(int page) {
        super();
        this.info = SUCCESS;
        this.page = page;
    }

    public NewsResponseEntity(String id) {
        super();
        this.info = SUCCESS;
        this.id = id;
    }
}
