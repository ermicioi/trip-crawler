package aermicioi.tripcrawler.crawler;

import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@ToString
public class PropertyModel {

    private final String photo;
    private final String name;
    private final BigDecimal price;

}
