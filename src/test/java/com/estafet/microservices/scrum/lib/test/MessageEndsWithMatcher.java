package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;
import org.hamcrest.core.StringEndsWith;

/**
 * Match if the actual message ends with the expected message.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class MessageEndsWithMatcher extends MessageMatcher {

    /**
     * Constructor.
     *
     * @param theExpectedMessage
     *          The expected message to match actual messages against.
     */
    public MessageEndsWithMatcher(final String theExpectedMessage) {
        super(new StringEndsWith(theExpectedMessage), theExpectedMessage);
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
        description.appendText("a message ending with ")
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
        mismatchDescription.appendText("message should end with ")
                           .appendValue(expectedMessage)
                           .appendText(", but was ")
                           .appendValue(item)
                           .appendText(".");
    }
}
