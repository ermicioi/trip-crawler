package aermicioi.tripcrawler.crawler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SearchResult {

    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final List<PropertyModel> properties;

}
