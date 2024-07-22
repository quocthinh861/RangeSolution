package io.mesoneer.interview_challenges;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class Range<T extends Comparable<T>> {

    private Bounder<T> lowerbound, upperbound;

    private Inclusive inclusive;

    private enum Inclusive {
        START, END, BOTH, NONE
    }

    public static class Query {
        public String value;
        public String range;
    }

    public static class Response {
        public Boolean result;
        public String message;

        public Response() {
        }

        public Response(Boolean result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    private static final class Bounder<T extends Comparable<T>> {
        private final Optional<T> value;
        private static final Bounder POSITIVE_INFINITY = new Bounder(Optional.empty());
        private static final Bounder NEGATIVE_INFINITY = new Bounder(Optional.empty());

        public Bounder(Optional<T>  value) {
            this.value = value;
        }

        public Bounder(T value) {
            this(Optional.of(value));
        }

        public static <T extends Comparable<T>> Bounder<T> positiveInfinity() {
            return POSITIVE_INFINITY;
        }

        public static <T extends Comparable<T>> Bounder<T> negativeInfinity() {
            return NEGATIVE_INFINITY;
        }

        public int compareTo(Bounder<T> other) {
            if (this == POSITIVE_INFINITY) {
                return other == POSITIVE_INFINITY ? 0 : 1;
            }
            if (this == NEGATIVE_INFINITY) {
                return other == NEGATIVE_INFINITY ? 0 : -1;
            }
            if (other == POSITIVE_INFINITY) {
                return -1;
            }
            if (other == NEGATIVE_INFINITY) {
                return 1;
            }
            return getValue().compareTo(other.getValue());
        }

        public T getValue() {
            return value.get();
        }

        public String toString() {
            return value.map(Object::toString).orElse("Infinitive");
        }
    }

    /**
     * Helper methods
     */
    public Boolean isBigger(Bounder<T> t1, Bounder<T> t2) {
        return t1.compareTo(t2) > 0;
    }

    public Boolean isSmaller(Bounder<T> t1, Bounder<T> t2) {
        return t1.compareTo(t2) < 0;
    }

    public String toString() {
        switch (inclusive) {
            case NONE:
                return "(" + lowerbound + ", " + upperbound + ")";
            case BOTH:
                return "[" + lowerbound + ", " + upperbound + "]";
            case END:
                return "(" + lowerbound + ", " + upperbound + "]";
            case START:
            default:
                return "[" + lowerbound + ", " + upperbound + ")";
        }
    }

    public static <T extends Comparable<T>> Range<T> parse(String rangeString, Function<String, T> converter) {
        Character start = rangeString.charAt(0);
        Character end = rangeString.charAt(rangeString.length() - 1);

        String[] values = Arrays.stream(rangeString.substring(1, rangeString.length() - 1).split(","))
                .map(String::trim)
                .toArray(String[]::new);

        if (values.length != 2) {
            throw new IllegalArgumentException("Range string must contain exactly two values");
        }

        Bounder<T> lowerbound = values[0].equals("Infinitive") ? Bounder.negativeInfinity() : new Bounder<T>(converter.apply(values[0]));
        Bounder<T> upperbound = values[1].equals("Infinitive") ? Bounder.positiveInfinity() : new Bounder<T>(converter.apply(values[1]));

        switch (start) {
            case '(':
                switch (end) {
                    case ')':
                        return new Range<>(lowerbound, upperbound, Inclusive.NONE);
                    case ']':
                        return new Range<>(lowerbound, upperbound, Inclusive.END);
                }
            case '[':
                switch (end) {
                    case ')':
                        return new Range<>(lowerbound, upperbound, Inclusive.START);
                    case ']':
                        return new Range<>(lowerbound, upperbound, Inclusive.BOTH);
                }
            default:
                throw new IllegalArgumentException("Invalid range string");
        }
    }

    /**
     * Constructor is private BY DESIGN.
     * <p>
     * TODO: Change the constructor to meet your requirements.
     */
    private Range() {
    }

    protected Range(Bounder lowerbound, Bounder upperbound, Inclusive inclusive) {
        if(lowerbound.compareTo(upperbound) > 0) {
            throw new IllegalArgumentException("Lowerbound cannot be greater than upperbound");
        }
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
        this.inclusive = inclusive;
    }

    /**
     * Creates a new <b>closed</b> {@code Range} that includes both bounds.
     */
    public static <T extends Comparable<T>> Range<T> of(T lowerbound, T upperbound) {
        return new Range<>(new Bounder(lowerbound), new Bounder<>(upperbound), Inclusive.BOTH);
    }

    /**
     * Creates a new <b>open</b> {@code Range} that excludes both bounds
     */
    public static <T extends Comparable<T>> Range<T> open(T lowerbound, T upperbound) {
        return new Range<>(new Bounder(lowerbound), new Bounder(upperbound), Inclusive.NONE);
    }

    /**
     * Creates a new <b>closed</b> {@code Range} that includes both bounds
     */
    public static <T extends Comparable<T>> Range<T> closed(T lowerbound, T upperbound) {
        return of(lowerbound, upperbound);
    }

    /**
     * Creates a new <b>open closed</b> {@code Range} that excludes lowerbound but includes upperbound
     */
    public static <T extends Comparable<T>> Range<T> openClosed(T lowerbound, T upperbound) {
        return new Range<>(new Bounder(lowerbound), new Bounder(upperbound), Inclusive.END);
    }

    /**
     * Creates a new <b>closed open</b> {@code Range} that includes lowerbound but excludes upperbound
     */
    public static <T extends Comparable<T>> Range<T> closedOpen(T lowerbound, T upperbound) {
        return new Range<>(new Bounder(lowerbound), new Bounder(upperbound), Inclusive.START);
    }

    /**
     * Creates a new <b>less than</b> {@code Range} that includes all values less than the given
     */
    public static <T extends Comparable<T>> Range<T> lessThan(T upperbound) {
        return new Range<>(Bounder.negativeInfinity(), new Bounder(upperbound), Inclusive.NONE);
    }

    /**
     * Creates a new <b>greater than</b> {@code Range} that includes all values greater than the given
     */
    public static <T extends Comparable<T>> Range<T> greaterThan(T lowerbound) {
        return new Range<>(new Bounder(lowerbound), Bounder.positiveInfinity(), Inclusive.NONE);
    }

    /**
     * Creates a new <b>at least</b> {@code Range} that includes all values greater than or equal to the given
     */
    public static <T extends Comparable<T>> Range<T> atLeast(T lowerbound) {
        return new Range<>(new Bounder(lowerbound), Bounder.positiveInfinity(), Inclusive.START);
    }

    /**
     * Creates a new <b>at most</b> {@code Range} that includes all values less than or equal to the given
     */
    public static <T extends Comparable<T>> Range<T> atMost(T upperbound) {
        return new Range<>(Bounder.negativeInfinity(), new Bounder(upperbound), Inclusive.END);
    }


    /**
     * Creates a new <b>at least</b> {@code Range} that includes all values greater than or equal to the given
     */
    public static <T extends Comparable<T>> Range<T> all() {
        return new Range<>(Bounder.negativeInfinity(), Bounder.positiveInfinity(), Inclusive.BOTH);
    }

    /**
     * Returns {@code true} on if the given {@code value} is contained in this
     * {@code Range}.
     */
    public boolean contains(T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        Bounder<T> valueBounder = new Bounder<>(value);
        switch (inclusive) {
            case NONE:
                return (isBigger(valueBounder, lowerbound) && isSmaller(valueBounder, upperbound));
            case BOTH:
                return (!isBigger(lowerbound, valueBounder) && !isBigger(valueBounder, upperbound));
            case END:
                return (isBigger(valueBounder, lowerbound) && !isBigger(valueBounder, upperbound));
            case START:
            default:
                return (!isBigger(lowerbound, valueBounder) && isBigger(upperbound, valueBounder));
        }
    }

    /**
     * Returns the {@code lowerbound} of this {@code Range}.
     */
    public T lowerbound() {
        return lowerbound.getValue();
    }

    /**
     * Returns the {@code upperbound} of this {@code Range}.
     */
    public T upperbound() {
        return upperbound.getValue();
    }

}
