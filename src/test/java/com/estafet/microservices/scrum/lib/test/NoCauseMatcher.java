/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;

/**
 * Matcher for a {@code null} exception.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class NoCauseMatcher extends CauseMatcher {


    /**
     * Constructor.
     */
    public NoCauseMatcher() {
       super();
    }

    /**
     * Describe this matcher.
     *
     * @param description
     *          The description to complete.
     *
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    @Override
    public void describeTo(final Description description) {
        description.appendText("a Throwable where the cause is not specified (null).");
    }

    /**
     * Match the supplied actual.
     *
     * @param actual
     *          The actual to match.
     * @return
     *          {@code true} if {@code actual} is {@code null}.
     *
     * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    protected boolean matchesSafely(final Throwable actual) {
        return actual.getCause() == null;
    }

    /**
     * Describe the mismatch.
     *
     * @param actual
     *          The actual {@code Throwable} that failed to match.
     * @param mismatchDescription
     *          The description to complete.
     */
    @Override
    public void describeMismatchSafely(final Throwable actual, final Description mismatchDescription) {

        if (actual != null) {
            final Throwable cause = actual.getCause();
            if (cause != null) {
                mismatchDescription.appendText("Cause should be null, but was a ")
                                   .appendText(cause.getClass().getName())
                                   .appendText(" with a message of \"")
                                   .appendValue(cause.getLocalizedMessage())
                                   .appendText("\".");
            }
        }
        else {
            mismatchDescription.appendText("The exception was null.");
        }
    }
}
