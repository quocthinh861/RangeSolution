package io.mesoneer.interview_challenges;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;

import static org.assertj.core.api.Assertions.*;

public class RangeTest {

  @Test
  public void should_create_range() {
    Range range = Range.of(5, 50);
    assertThat(range.lowerbound()).isEqualTo(5);
    assertThat(range.upperbound()).isEqualTo(50);
  }

  @Test
  public void should_throw_error__when_create_with_lowerbound_bigger_than_upperbound() {
    try {
      Range.of(500, 1);
      fail("Should not allow creating a invalid Range");
    } catch(IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void closed_range_should_contain_both_bounds_and_all_elements_in_between() {
    Range closedRange = Range.of(5, 50);

    assertThat(closedRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(closedRange.contains(4)).isEqualTo( false);

    assertThat(closedRange.contains(5)).isEqualTo( true);

    assertThat(closedRange.contains(42)).isEqualTo( true);

    assertThat(closedRange.contains(50)).isEqualTo( true);

    assertThat(closedRange.contains(10000)).isEqualTo( false);
    assertThat(closedRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
  public void range_should_be_state_independent() {
    Range range1 = Range.of(5, 10);
    Range range2 = Range.of(11, 20);

    assertThat(range1.contains(10)).isEqualTo( true);
    assertThat(range2.contains(10)).isEqualTo( false);
  }

  @Test
  public void open_range_should_contain_all_elements_except_bounds() {
    Range openRange = Range.open(5, 50);

    assertThat(openRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(openRange.contains(4)).isEqualTo( false);

    assertThat(openRange.contains(5)).isEqualTo( false);

    assertThat(openRange.contains(42)).isEqualTo( true);

    assertThat(openRange.contains(50)).isEqualTo( false);

    assertThat(openRange.contains(10000)).isEqualTo( false);
    assertThat(openRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
  public void closed_open_range_should_contain_lowerbound_and_all_elements_in_between() {
    Range closedOpenRange = Range.closedOpen(5, 50);

    assertThat(closedOpenRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(closedOpenRange.contains(4)).isEqualTo( false);

    assertThat(closedOpenRange.contains(5)).isEqualTo( true);

    assertThat(closedOpenRange.contains(42)).isEqualTo( true);

    assertThat(closedOpenRange.contains(50)).isEqualTo( false);

    assertThat(closedOpenRange.contains(10000)).isEqualTo( false);
    assertThat(closedOpenRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
  public void open_closed_range_should_contain_upperbound_and_all_elements_in_between() {
    Range openClosedRange = Range.openClosed(5, 50);

    assertThat(openClosedRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(openClosedRange.contains(4)).isEqualTo( false);

    assertThat(openClosedRange.contains(5)).isEqualTo( false);

    assertThat(openClosedRange.contains(42)).isEqualTo( true);

    assertThat(openClosedRange.contains(50)).isEqualTo( true);

    assertThat(openClosedRange.contains(10000)).isEqualTo( false);
    assertThat(openClosedRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
  public void closed_range_should_contain_all_elements_in_between() {
    Range closedRange = Range.closed(5, 50);

    assertThat(closedRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(closedRange.contains(4)).isEqualTo( false);

    assertThat(closedRange.contains(5)).isEqualTo( true);

    assertThat(closedRange.contains(42)).isEqualTo( true);

    assertThat(closedRange.contains(50)).isEqualTo( true);

    assertThat(closedRange.contains(10000)).isEqualTo( false);
    assertThat(closedRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
    public void generic_range_should_work_with_any_comparable_type() {
        Range<Integer> numbers = Range.of(1, 10);
        assertThat(numbers.contains(1)).isEqualTo(true);
        assertThat(numbers.contains(10)).isEqualTo(true);
        assertThat(numbers.contains(5)).isEqualTo(true);
        assertThat(numbers.contains(0)).isEqualTo(false);
        assertThat(numbers.contains(11)).isEqualTo(false);

        Range<String> text = Range.open("abc", "xyz");
        assertThat(text.contains("abc")).isEqualTo(false);
        assertThat(text.contains("xyz")).isEqualTo(false);
        assertThat(text.contains("mno")).isEqualTo(true);
        assertThat(text.contains("ABC")).isEqualTo(false);
        assertThat(text.contains("XYZ")).isEqualTo(false);

        Range<BigDecimal> decimals = Range.open(BigDecimal.valueOf(1), new BigDecimal(10));
        assertThat(decimals.contains(BigDecimal.valueOf(1))).isEqualTo(false);
        assertThat(decimals.contains(BigDecimal.valueOf(10))).isEqualTo(false);
        assertThat(decimals.contains(BigDecimal.valueOf(5))).isEqualTo(true);
        assertThat(decimals.contains(BigDecimal.valueOf(1.1))).isEqualTo(true);
        assertThat(decimals.contains(BigDecimal.valueOf(2))).isEqualTo(true);

        Range<ChronoLocalDate> dates = Range.closed(LocalDate.of(2016, Month.SEPTEMBER, 11),
                LocalDate.of(2017, Month.JUNE, 30));
        assertThat(dates.contains(LocalDate.of(2016, Month.SEPTEMBER, 11))).isEqualTo(true);
        assertThat(dates.contains(LocalDate.of(2017, Month.JUNE, 30))).isEqualTo(true);
        assertThat(dates.contains(LocalDate.of(2017, Month.JUNE, 29))).isEqualTo(true);
        assertThat(dates.contains(LocalDate.of(2016, Month.SEPTEMBER, 10))).isEqualTo(false);
        assertThat(dates.contains(LocalDate.of(2017, Month.JULY, 1))).isEqualTo(false);
    }

    @Test
    public void less_than_range_should_contain_all_elements_less_than_upperbound() {
        Range lessThan = Range.lessThan(10);

        assertThat(lessThan.contains(Integer.MIN_VALUE)).isEqualTo( true);
        assertThat(lessThan.contains(4)).isEqualTo( true);

        assertThat(lessThan.contains(10)).isEqualTo( false);

        assertThat(lessThan.contains(42)).isEqualTo( false);

        assertThat(lessThan.contains(10000)).isEqualTo( false);
        assertThat(lessThan.contains(Integer.MAX_VALUE)).isEqualTo( false);
    }

    @Test
    public void greater_than_range_should_contain_all_elements_greater_than_lowerbound() {
        Range greaterThan = Range.greaterThan(10);

        assertThat(greaterThan.contains(Integer.MIN_VALUE)).isEqualTo( false);
        assertThat(greaterThan.contains(4)).isEqualTo( false);

        assertThat(greaterThan.contains(10)).isEqualTo( false);

        assertThat(greaterThan.contains(42)).isEqualTo( true);

        assertThat(greaterThan.contains(10000)).isEqualTo( true);
        assertThat(greaterThan.contains(Integer.MAX_VALUE)).isEqualTo( true);
    }

    @Test
    public void at_least_range_should_contain_all_elements_greater_than_or_equal_to_lowerbound() {
        Range atLeast = Range.atLeast(10);

        assertThat(atLeast.contains(Integer.MIN_VALUE)).isEqualTo( false);
        assertThat(atLeast.contains(4)).isEqualTo( false);

        assertThat(atLeast.contains(10)).isEqualTo( true);

        assertThat(atLeast.contains(42)).isEqualTo( true);

        assertThat(atLeast.contains(10000)).isEqualTo( true);
        assertThat(atLeast.contains(Integer.MAX_VALUE)).isEqualTo( true);
    }

    @Test
    public void at_most_range_should_contain_all_elements_less_than_or_equal_to_upperbound() {
        Range atMost = Range.atMost(10);

        assertThat(atMost.contains(Integer.MIN_VALUE)).isEqualTo( true);
        assertThat(atMost.contains(4)).isEqualTo( true);

        assertThat(atMost.contains(10)).isEqualTo( true);

        assertThat(atMost.contains(42)).isEqualTo( false);

        assertThat(atMost.contains(10000)).isEqualTo( false);
        assertThat(atMost.contains(Integer.MAX_VALUE)).isEqualTo( false);
    }

    @Test
    public void all_range_should_contain_all_elements() {
        Range all = Range.all();

        assertThat(all.contains(Integer.MIN_VALUE)).isEqualTo( true);
        assertThat(all.contains(4)).isEqualTo( true);

        assertThat(all.contains(10)).isEqualTo( true);

        assertThat(all.contains(42)).isEqualTo( true);

        assertThat(all.contains(10000)).isEqualTo( true);
        assertThat(all.contains(Integer.MAX_VALUE)).isEqualTo( true);
    }

    @Test
    public void convert_to_string() {
        Range range = Range.of(5, 50);
        assertThat(range.toString()).isEqualTo("[5, 50]");

        range = Range.open(5, 50);
        assertThat(range.toString()).isEqualTo("(5, 50)");

        range = Range.closedOpen(5, 50);
        assertThat(range.toString()).isEqualTo("[5, 50)");

        range = Range.openClosed(5, 50);
        assertThat(range.toString()).isEqualTo("(5, 50]");

        range = Range.lessThan(50);
        assertThat(range.toString()).isEqualTo("(Infinitive, 50)");

        range = Range.greaterThan(5);
        assertThat(range.toString()).isEqualTo("(5, Infinitive)");

        range = Range.atLeast(5);
        assertThat(range.toString()).isEqualTo("[5, Infinitive)");

        range = Range.atMost(50);
        assertThat(range.toString()).isEqualTo("(Infinitive, 50]");

        Range<ChronoLocalDate> dates = Range.closed(LocalDate.of(2016, Month.SEPTEMBER, 11),
                LocalDate.of(2017, Month.JUNE, 30));
        assertThat(dates.toString()).isEqualTo("[2016-09-11, 2017-06-30]");

    }

    @Test
    public void parse_from_string() {
        Range<Integer> range = Range.parse("[5, 50]", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.upperbound()).isEqualTo(50);

        range = Range.parse("(5, 50)", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.upperbound()).isEqualTo(50);

        range = Range.parse("[5, 50)", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.upperbound()).isEqualTo(50);

        range = Range.parse("(5, 50]", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.upperbound()).isEqualTo(50);
        assertThat(range.contains(5)).isEqualTo(false);
        assertThat(range.contains(50)).isEqualTo(true);

        range = Range.parse("(Infinitive, 50)", Integer::parseInt);
        assertThat(range.upperbound()).isEqualTo(50);
        assertThat(range.contains(Integer.MIN_VALUE)).isEqualTo(true);

        range = Range.parse("(5, Infinitive)", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.contains(Integer.MAX_VALUE)).isEqualTo(true);

        range = Range.parse("[5, Infinitive)", Integer::parseInt);
        assertThat(range.lowerbound()).isEqualTo(5);
        assertThat(range.contains(Integer.MAX_VALUE)).isEqualTo(true);

        range = Range.parse("(Infinitive, 50]", Integer::parseInt);
        assertThat(range.upperbound()).isEqualTo(50);
        assertThat(range.contains(Integer.MIN_VALUE)).isEqualTo(true);

        Range<ChronoLocalDate> localDateRange = Range.parse("[2016-09-11, 2017-06-30]", LocalDate::parse);
        assertThat(localDateRange.lowerbound()).isEqualTo(LocalDate.of(2016, Month.SEPTEMBER, 11));
        assertThat(localDateRange.upperbound()).isEqualTo(LocalDate.of(2017, Month.JUNE, 30));
        assertThat(localDateRange.contains(LocalDate.of(2016, Month.SEPTEMBER, 11))).isEqualTo(true);
    }
}
