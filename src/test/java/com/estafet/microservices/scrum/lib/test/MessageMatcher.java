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
     * The message to match.
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

    /**
     * <p>The actual message is the same as the expected message.</p>
     * @param expected
     *          The expected message.
     *
     * @return
     *          A {@link TypeSafeMatcher} that matches a message for equality.
     *
     */
    public static MessageIsSameMatcher messageIs(final String expected) {
        return new MessageIsSameMatcher(expected);
    }

    /**
     * <p>The actual message contains the expected message.</p>
     * @param expected
     *          The expected substring.
     *
     * @return
     *          A {@link TypeSafeMatcher} that matches a substring of the actual message.
     *
     */
    public static MessageContainsMatcher messageContains(final String expected) {
        return new MessageContainsMatcher(expected);
    }

    /**
     * <p>The actual message starts with the expected message.</p>
     * @param expected
     *      The expected start of the actual message.
     *
     * @return
     *          A {@link TypeSafeMatcher} that matches the start of the actual message.
     *
     */
    public static MessageStartsWithMatcher messageStartsWith(final String expected) {
        return new MessageStartsWithMatcher(expected);
    }

    /**
     * <p>The actual message ends with the expected message.</p>
     *
     * @return
     *          A {@link TypeSafeMatcher} that matches the end of the actual message.
     *
     */
    public static MessageEndsWithMatcher messageEndsWith(final String expected) {
        return new MessageEndsWithMatcher(expected);
    }

}
