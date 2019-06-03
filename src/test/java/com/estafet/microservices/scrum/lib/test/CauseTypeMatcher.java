/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;

/**
 * Matcher for an Exception cause type.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class CauseTypeMatcher extends CauseMatcher {


    /**
     * The expected cause type.
     */
    private final Class<? extends Throwable> expectedCauseType;

    /**
     * Constructor.
     * @param theExpectedCauseType
     *          The expected Exception type to match actual Exceptions against.
     */
    public CauseTypeMatcher(final Class<? extends Throwable> theExpectedCauseType) {
       super();
       expectedCauseType = theExpectedCauseType;
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
        description.appendText("a Throwable that is a ")
                   .appendValue(expectedCauseType)
                   .appendText(" object.");
    }

    /**
     * Match the actual {@link Throwable} type.
     *
     * <p>{@link #expectedCauseType} must satisfy one of these conditions:</p>
     * <ul>
     *      <li>{@code expectedCauseType} and {@code actual.getCause()} are the same type</li>
     *      <li>{@code expectedCauseType} is a superclass of {@code  actual.getCause()}'s class</li>
     * </ul>
     *
     * @param actualCause
     *          The actual cause {@link Throwable} to match. Will never be {@code null}.
     * @return
     *          {@code true} if the class represented by {@link #expectedCauseType} is either the same
     *          as, or is a superclass of, the class represented by {@code actual.getClass()}.
     *
     * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    public boolean matchesSafely(final Throwable actualCause) {

        final Class<? extends Throwable> actualCauseClass = actualCause.getClass();

        // Determines if the class represented by the expectedCauseType object satisfies one of these conditions:
        //
        //    1. It is the same as the class represented by the actualClass Class object.
        //    2. It is a superclass of the class represented by the actualClass Class object.
        //
        final boolean matches = expectedCauseType.isAssignableFrom(actualCauseClass);

        return matches;
    }

    /**
     * Describe the mismatch.
     *
     * @param actualCause
     *          The item that failed to match. Will never be {@code null}.
     * @param mismatchDescription
     *          The description to complete.
     */
    @Override
    public void describeMismatchSafely(final Throwable actualCause, final Description mismatchDescription) {

         mismatchDescription.appendText(" The cause was a ")
                            .appendValue(actualCause.getClass().getName())
                            .appendText(", which is neither a ")
                            .appendValue(expectedCauseType)
                            .appendText(", nor a subclass of ")
                            .appendValue(expectedCauseType)
                            .appendText("\".");
    }
}
