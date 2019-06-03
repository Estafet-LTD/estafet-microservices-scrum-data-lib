package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;
import org.hamcrest.core.StringStartsWith;

/**
 * Match if the actual message starts with the expected message.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class MessageStartsWithMatcher extends MessageMatcher {

    /**
     * Constructor.
     *
     * @param theExpectedMessage
     *          The expected message to match actual messages against.
     */
    public MessageStartsWithMatcher(final String theExpectedMessage) {
        super(new StringStartsWith(theExpectedMessage), theExpectedMessage);
    }

    /**
     * Create a description for this matcher.
     *
     * @param description
     *          The {@link Description} to add to.
     *
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    @Override
    public void describeTo(final Description description) {
        description.appendText("a message starting with ")
                   .appendValue(expectedMessage);

    }

    /**
     * Describe the mismatch.
     *
     * @param item
     *          The actual message.
     * @param mismatchDescription
     *          The description to add to.
     *
     * @see org.hamcrest.TypeSafeMatcher#describeMismatchSafely(java.lang.Object, org.hamcrest.Description)
     */
    @Override
    public void describeMismatchSafely(final String item, final Description mismatchDescription) {
        mismatchDescription.appendText("message should start with ")
                           .appendValue(expectedMessage)
                           .appendText(", but was ")
                           .appendValue(item)
                           .appendText(".");
    }
}
