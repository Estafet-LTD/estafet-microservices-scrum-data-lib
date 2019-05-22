package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Class to match the type of the cause of an exception and the cause's error message.
 *
 * @see org.hamcrest.TypeSafeMatcher
 * @author Steve Brown, Estafet Ltd.
 */
public class CauseMatcher extends TypeSafeMatcher<Throwable> {

    /**
     * The expected message.
     */
    private final String expectedMessage;

    /**
     * How to match the cause message.
     */
    private final CauseMatchType causeMatchType;

    /**
     * The expected cause type.
     */
    private final Class<? extends Throwable> expectedType;

    /**
     * Constructor.
     *
     * @param cause
     *          The expected cause. Can be {@code null}.
     */
    public CauseMatcher(final Throwable cause) {
        this(cause, CauseMatchType.MATCH_CAUSE_MESSAGE);
    }

    /**
     * Constructor.
     *
     * <p>Use this constructor when instances of the cause type are difficult to construct.</p>
     * <p>The matcher will ignore the cause message.</p>
     * @param causeType
     *          The expected cause type. Cannot be {@code null}.
     */
    public CauseMatcher(final Class<? extends Throwable> causeType) {

        expectedType = causeType;
        expectedMessage = null;
        causeMatchType = CauseMatchType.IGNORE_CAUSE_MESSAGE;
    }

    /**
     * Constructor.
     *
     * @param cause
     *          The expected cause. Can be {@code null}.
     * @param theCauseMatchType
     *          How to match the cause message.
     */
    public CauseMatcher(final Throwable cause, final CauseMatchType theCauseMatchType) {
        super();
        expectedType = cause != null ? cause.getClass() : null;
        expectedMessage = cause != null ? cause.getMessage() : null;
        causeMatchType = theCauseMatchType;
    }

    /**
     * Describe this matcher.
     * @param description
     *          The description to append to.
     *
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    @Override
    public void describeTo(final Description description) {
        description.appendText("expects expected type ")
                   .appendValue(expectedType)
                   .appendText(" and a message \"")
                   .appendValue(expectedMessage)
                   .appendText("\".");
    }

    /**
     * Check the cause is the expected exception type.
     *
     * @param actual
     *              The actual cause.
     * @return
     *              {@code true} if {@code actual} is the expected cause type or {@code actual}
     *              and the expected cause type are both {@code null}.
     */
    private boolean isCauseExpectedType(final Throwable actual) {
        final boolean actualNull = actual == null;
        final boolean expectedNull = expectedType == null;

        if (actualNull != expectedNull) {
            return false;
        }

        if (actual == null) {
            return true;
        }

        final Class<? extends Throwable> actualClass = actual.getClass();

        // Determines if the class or interface represented by the expectedType object
        // satisfies one of these conditions:
        //
        //    1. It is the same as the class or interface represented by the actualClass Class
        //       object.
        //    2. It is a superclass of the class or implements the interface represented by the
        //       actualClass Class object.
        //    3. It is a super interface of or implements a super interface of the interface
        //       represented by the actualClass Class object.
        //
        final boolean matches = expectedType.isAssignableFrom(actualClass);

        return matches;
    }

    /**
     * Check the the cause message is the expected message.
     *
     * @param actual
     *              The actual cause.
     * @return
     *              {@code true} if the cause message contains the expected message or the cause
     *              message and the expected message are both {@code null}.
     */
    private boolean isCauseMessageExpectedMessage(final Throwable actual) {
        String message = null;

        if (actual != null) {
            message = actual.getMessage();
        }

        final boolean messageNull = message == null;

        final boolean expectedMessageNull = expectedMessage == null;

        if (messageNull != expectedMessageNull) {
            return false;
        }

        if (message == null) {
            return true;
        }

        final boolean matches = message.startsWith(expectedMessage); // NOSONAR
        return matches;
    }

    /**
     * Check the actual exception matches the expected exception.
     *
     * <p>The cause
     * @param actual
     *          The actual exception to check. Can be {@code null}.
     * @return
     *          {@code true} if {@code item} matches the expected exception. {@code false}
     *          otherwise.
     *
     * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    protected boolean matchesSafely(final Throwable actual) {

        if (causeMatchType == CauseMatchType.IGNORE_CAUSE) {
            return true;
        }

        final boolean matches = isCauseExpectedType(actual);

        if (!matches || causeMatchType == CauseMatchType.IGNORE_CAUSE_MESSAGE) {
            return matches;
        }

        return isCauseMessageExpectedMessage(actual);
    }
}