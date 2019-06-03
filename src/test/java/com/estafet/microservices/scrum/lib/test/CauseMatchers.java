package com.estafet.microservices.scrum.lib.test;

/**
 * Factory class for matching the cause of an Exception.
 *
 * @see org.hamcrest.TypeSafeMatcher
 * @author Steve Brown, Estafet Ltd.
 */
public final class CauseMatchers {

    /**
     * Constructor.
     */
    private CauseMatchers() {
        super();
    }

    /**
     * @return
     *          A {@link NoCauseMatcher} that matches a {@code null} cause.
     *
     */
    public static NoCauseMatcher noCause() {
        return new NoCauseMatcher();
    }

    /**
     * <p>The cause message is ignored.</p>
     * @param causeType
     *          The expected type of the cause.
     *
     * @return
     *          A {@link CauseTypeMatcher} the matches a a cause type.
     *
     */
    public static CauseTypeMatcher causeType(final Class<? extends Throwable> causeType) {
        return new CauseTypeMatcher(causeType);
    }

    /**
     * Matches the cause type and the cause message.
     *
     * @param causeMatcher
     *          The {@link CauseMatchers} to match the cause Exception type.
     * @param messageMatcher
     *          The {@link CauseMatchers} to match the cause message.
     * @return
     *          The {@link CauseTypeAndMessageMatcher}.
     */
    public static CauseTypeAndMessageMatcher causeTypeAndMesssage(final CauseTypeMatcher causeMatcher,
                                                                  final MessageMatcher messageMatcher) {
        return new CauseTypeAndMessageMatcher(causeMatcher, messageMatcher);
    }

    /**
     * Match a specific cause.
     *
     * @param expectedCause
     *          The expected cause.
     * @param causeMessageMatchType
     *          How to match the cause message.
     * @return
     *          The appropriate {@link CauseMatcher}
     */
    public static CauseMatcher causeMatcher(final Throwable expectedCause,
                                            final CauseMessageMatchType causeMessageMatchType) {
        if (expectedCause == null) {
            return new NoCauseMatcher();
        }

        final CauseTypeMatcher causeTypeMatcher = CauseMatchers.causeType(expectedCause.getClass());

        if (causeMessageMatchType == CauseMessageMatchType.IGNORE_CAUSE_MESSAGE) {
            return causeTypeMatcher;
        }

        final MessageMatcher messageMatcher =
                           MessageMatchers.matcher(causeMessageMatchType, expectedCause.getLocalizedMessage());
        return CauseMatchers.causeTypeAndMesssage(causeTypeMatcher, messageMatcher);
    }
}