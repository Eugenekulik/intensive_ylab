package by.eugenekulik.utils;

public class IncrementSequence implements Sequence {

    private Long increment;
    private Long current;

    public IncrementSequence(Long start, Long increment) {
        current = start;
        this.increment = increment;
    }

    @Override
    public Long next() {
        current += increment;
        return current - increment;
    }
}
