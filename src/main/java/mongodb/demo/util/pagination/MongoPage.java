package mongodb.demo.util.pagination;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MongoPage {

    private int size;

    private int skip;
}
