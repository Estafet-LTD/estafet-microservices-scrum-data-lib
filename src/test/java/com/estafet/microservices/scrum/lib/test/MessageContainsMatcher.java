package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;
import org.hamcrest.core.StringContains;

/**
 * Match if the actual message contains the expected message.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class MessageContainsMatcher extends MessageMatcher {

    /**
     * Constructor.
     *
     * @param theExpectedMessage
     *          The expected message to match actual messages against.
     */
    public MessageContainsMatcher(final String theExpectedMessage) {
        super(new StringContains(theExpectedMessage), theExpectedMessage);
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
        description.appendText("a message containing ")
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
        mismatchDescription.appendText("message should contain ")
                           .appendValue(expectedMessage)
                           .appendText(", but was ")
                           .appendValue(item)
                           .appendText(".");
    }
}
