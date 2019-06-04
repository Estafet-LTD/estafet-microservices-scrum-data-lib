package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher for exception messages.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public abstract class MessageMatcher extends TypeSafeMatcher<String> {
    /**
     * The {@link BaseMatcher} to match messages.
     */
    protected final BaseMatcher<String> matcher;

    /**
     * The expected message to match the actual message against.
     */
    protected final String expectedMessage;

    /**
     * Constructor.
     *
     * @param theMatcher
     *      The {@link BaseMatcher} to use to match the actual message against the expected message.
     * @param theExpectedMessage
     *      The expected message to match the actual message against.
     */
    protected MessageMatcher(final BaseMatcher<String> theMatcher, final String theExpectedMessage) {
        super();
        matcher = theMatcher;
        expectedMessage = theExpectedMessage;
    }

    /**
     * Match the message.
     *
     * @param actualMessage
     *          The actual message.
     * @return
     *          {@code true} if the actual message matches the expected  message.
     */
    @Override
    public boolean matchesSafely(final String actualMessage) {
        return matcher.matches(actualMessage);
    }
}
