package mongodb.demo.util.pagination;

import org.springframework.stereotype.Component;

@Component
public class MongoPaginationUtil {

    public int calculateLength(int count,int pageSize){
        if(count < pageSize){
            return 1;
        }else if(count % pageSize != 0){
            return count/pageSize + 1;
        }else {
            return count/pageSize;
        }
    }

    public MongoPage getPage(String strPage,int pageSize,int length){
        int page;
        try {
            page = Integer.parseInt(strPage) - 1;
        }catch (NumberFormatException e){
            page = 0;
        }
        if(page < 0 || page >= length){
            page = 0;
        }
        return MongoPage
                .builder()
                .size(pageSize)
                .skip(page * pageSize)
                .build();
    }
}
