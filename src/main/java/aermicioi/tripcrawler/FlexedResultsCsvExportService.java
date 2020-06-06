package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.PropertyModel;
import aermicioi.tripcrawler.crawler.SearchResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.FmtLocalDate;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import javax.validation.constraints.NotNull;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

@Validated
@Slf4j
public class FlexedResultsCsvExportService {

    private static final CellProcessor[] PROCESSORS = new CellProcessor[]{
            new org.supercsv.cellprocessor.constraint.NotNull(),
            new FmtLocalDate(),
            new FmtLocalDate(),
            new FmtNumber("###,###.00")
    };

    private static final String[] HEADERS = new String[]{
            "Property", "Check In", "Check Out", "Price"
    };

    @SneakyThrows(IOException.class)
    public void export(@NotNull final List<SearchResult> results, final Writer writer) {
        try (final CsvListWriter csvListWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE)) {
            csvListWriter.writeHeader(HEADERS);
            results.forEach(result ->
                    result.getProperties().forEach(p -> {
                        export(p, result.getCheckInDate(), result.getCheckOutDate(), csvListWriter);
                    }));
        }
    }

    @SneakyThrows(IOException.class)
    private void export(final PropertyModel property, final LocalDate checkIn, final LocalDate checkOut, CsvListWriter writer) {
        writer.write(List.of(
                property.getName(),
                checkIn,
                checkOut,
                property.getPrice()
        ), PROCESSORS);
    }

}
