package aermicioi.tripcrawler;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@Getter
@ToString
public class FlexedTripSearchRequest {
    @NotBlank
    private final String location;

    @NotNull
    @FutureOrPresent
    private final LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private final LocalDate endDate;

    @Min(1)
    private final int lengthOfStay;

    @Min(1)
    private final int adultsCount;

    @NotNull
    @Singular
    private final Collection<@Min(0) Integer> children;

    @Min(1)
    private final int roomsCount;
}
