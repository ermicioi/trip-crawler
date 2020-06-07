package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.PropertyModel;
import aermicioi.tripcrawler.crawler.SearchResult;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlexedResultsCsvExportServiceIT {

    @Autowired
    private FlexedResultsCsvExportService service;

    @Test
    public void testExport() {
        final SearchResult searchResult1 = new SearchResult(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 1, 10),
                List.of(
                        PropertyModel.builder().name("Foo-1").price(BigDecimal.valueOf(1100.01)).url("http://foo.fo").build(),
                        PropertyModel.builder().name("Foo-2").price(BigDecimal.valueOf(1101.1)).url("http://foo.fo").build()));
        final SearchResult searchResult2 = new SearchResult(
                LocalDate.of(2019, 1, 2),
                LocalDate.of(2019, 1, 11),
                List.of(
                        PropertyModel.builder().name("Foo-1").price(BigDecimal.valueOf(1100.01)).url("http://foo.fo").build(),
                        PropertyModel.builder().name("Foo-2").price(BigDecimal.valueOf(1101.1)).url("http://foo.fo").build(),
                        PropertyModel.builder().name("Foo-3").price(BigDecimal.valueOf(11.123)).url("http://foo.fo").build()));

        final StringWriter writer = new StringWriter();
        service.export(List.of(searchResult1, searchResult2), writer);

        final List<String> lines = writer.toString().lines().collect(Collectors.toUnmodifiableList());
        assertThat(lines.get(0)).isEqualTo("\"Property\",\"Check In\",\"Check Out\",\"Price\",\"Url\"");
        assertThat(lines.get(1)).isEqualTo("\"Foo-1\",\"2019-01-01\",\"2019-01-10\",\"1,100.01\",\"http://foo.fo\"");
        assertThat(lines.get(2)).isEqualTo("\"Foo-2\",\"2019-01-01\",\"2019-01-10\",\"1,101.10\",\"http://foo.fo\"");
        assertThat(lines.get(3)).isEqualTo("\"Foo-1\",\"2019-01-02\",\"2019-01-11\",\"1,100.01\",\"http://foo.fo\"");
        assertThat(lines.get(4)).isEqualTo("\"Foo-2\",\"2019-01-02\",\"2019-01-11\",\"1,101.10\",\"http://foo.fo\"");
        assertThat(lines.get(5)).isEqualTo("\"Foo-3\",\"2019-01-02\",\"2019-01-11\",\"11.12\",\"http://foo.fo\"");
    }
}