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
     * Match the supplied actual cause.
     *
     * @param actualCause
     *          The actual cause to match.
     * @return
     *          {@code true} if {@code actualCause} is {@code null}.
     *
     * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    protected boolean matchesSafely(final Throwable actualCause) {
        return actualCause == null;
    }

    /**
     * Describe the mismatch.
     *
     * @param actualCause
     *          The actual {@code Throwable} that failed to match.
     * @param mismatchDescription
     *          The description to complete.
     */
    @Override
    public void describeMismatchSafely(final Throwable actualCause, final Description mismatchDescription) {

            mismatchDescription.appendText("The cause should be null, but was a ")
                               .appendValue(actualCause)
                               .appendText("\".");
    }
}
