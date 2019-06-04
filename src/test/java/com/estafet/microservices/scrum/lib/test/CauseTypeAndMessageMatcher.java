/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.Description;

/**
 * Matcher for a cause (@link Throwable} with a specific message.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class CauseTypeAndMessageMatcher extends CauseMatcher {


    /**
     * The matcher to match the actual cause type against the expected cause type.
     */
    private final CauseTypeMatcher causeTypeMatcher;

    /**
     * The {@link MessageMatcher} to use to match messages.
     */
    private final MessageMatcher messageMatcher;

    /**
     * Constructor.
     * @param theCauseTypeMatcher
     *          The matcher to match the actual cause against the expected cause.
     * @param theMessageMatcher
     *          The {@link MessageMatcher} to use to match messages.
     */
    public CauseTypeAndMessageMatcher(final CauseTypeMatcher theCauseTypeMatcher,
                                      final MessageMatcher theMessageMatcher) {
       super();
       causeTypeMatcher = theCauseTypeMatcher;
       messageMatcher =  theMessageMatcher;
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
        description.appendDescriptionOf(causeTypeMatcher)
                   .appendText(" and ")
                   .appendDescriptionOf(messageMatcher);
    }

    /**
     * Match the supplied item.
     *
     * @param item
     *          The item to match. Cannot be {@code null}.
     * @return
     *          {@code true} if {@code item} matches the expected Exception type and message contents.
     *
     * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    protected boolean matchesSafely(final Throwable item) {
        final boolean causeTypeMatches = causeTypeMatcher.matchesSafely(item);

        if (!causeTypeMatches) {
            return false;
        }

        final boolean messageMatches = messageMatcher.matchesSafely(item.getLocalizedMessage());

        return messageMatches;
    }

    /**
     * Describe the mismatch.
     *
     * @param actualCause
     *          The cause that failed to match. Will never be {@code null}.
     * @param mismatchDescription
     *          The description to complete.
     */
    @Override
    public void describeMismatchSafely(@SuppressWarnings("unused") final Throwable actualCause,
                                       final Description mismatchDescription) {

         mismatchDescription.appendDescriptionOf(causeTypeMatcher)
                            .appendText(" and ")
                            .appendDescriptionOf(messageMatcher);
    }
}
