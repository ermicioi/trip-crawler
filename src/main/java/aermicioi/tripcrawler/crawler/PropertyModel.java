package aermicioi.tripcrawler.crawler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class PropertyModel {

    private final String url;
    private final String photo;
    private final String name;
    private final BigDecimal price;

}
