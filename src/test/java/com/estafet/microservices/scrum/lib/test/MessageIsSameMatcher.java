package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;
import org.hamcrest.core.IsEqual;

/**
 * Match message for equality.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class MessageIsSameMatcher extends MessageMatcher {

    /**
     * Constructor.
     *
     * <p>{@link IsEqual} handles {@code null} values safely.</p>
     *
     * @param theExpectedMessage
     *          The expected message to match actual messages against.
     */
    @SuppressWarnings("unused")
    public MessageIsSameMatcher(final String theExpectedMessage) {
        super(new IsEqual<String>(theExpectedMessage), theExpectedMessage);
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
        description.appendText(" matches messages for equality.");
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
        mismatchDescription.appendText(". Expected ")
                           .appendValue(item)
                           .appendText(", but was ")
                           .appendValue(item)
                           .appendText(".");
    }
}
