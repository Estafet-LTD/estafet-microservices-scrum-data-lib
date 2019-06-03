/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

/**
 * Message Matcher class factory.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 * @See CauseMessageMatchType
 */
public final class MessageMatchers {

    /**
     * Cannot instantiate.
     *
     */
    private MessageMatchers() {
        super();
    }

    /**
     * Create an appropriate message matcher.
     *
     * @param causeMessageMatchType
     *          The message match type.
     * @param expectedCauseMessage
     *          The expected cause message.
     * @return
     *          a {@link MessageMatcher}.
     */
    public static MessageMatcher matcher(final CauseMessageMatchType causeMessageMatchType,
                                         final String expectedCauseMessage) {

        switch (causeMessageMatchType) {

        case CAUSE_MESSAGE_IS_NULL:
            // Drop through is intentional;

        case CAUSE_MESSAGE_IS_SAME:
            return MessageMatchers.messageIsSame(expectedCauseMessage);

        case CAUSE_MESSAGE_CONTAINS:
            return MessageMatchers.messageContains(expectedCauseMessage);

        case CAUSE_MESSAGE_STARTS_WITH:
            return MessageMatchers.messageStartsWith(expectedCauseMessage);

        case CAUSE_MESSAGE_ENDS_WITH:
            return MessageMatchers.messageStartsWith(expectedCauseMessage);

        case IGNORE_CAUSE_MESSAGE:
            // Should never be called with this message match type.
        default:
            throw new RuntimeException("Unknown message match type");
        }
    }

    /**
     * Create a matcher to verify that the actual message is the same as the expected message.
     *
     * @param expectedCauseMessage
     *          The expected cause message.
     * @return
     *          The matcher.
     */
    public static final MessageMatcher messageIsSame(final String expectedCauseMessage) {
        return new MessageIsSameMatcher(expectedCauseMessage);
    }

    /**
     * Create a matcher to verify that the actual message contains the expected message.
     *
     * @param expectedCauseMessage
     *          The expected cause message.
     * @return
     *          The matcher.
     */
    public static final MessageMatcher messageContains(final String expectedCauseMessage) {
        return new MessageContainsMatcher(expectedCauseMessage);
    }

    /**
     * Create a matcher to verify that the actual message starts with the expected message.
     *
     * @param expectedCauseMessage
     *          The expected cause message.
     * @return
     *          The matcher.
     */
    public static final MessageMatcher messageStartsWith(final String expectedCauseMessage) {
        return new MessageStartsWithMatcher(expectedCauseMessage);
    }

    /**
     * Create a matcher to verify that the actual message ends with the expected message.
     *
     * @param expectedCauseMessage
     *          The expected cause message.
     * @return
     *          The matcher.
     */
    public static final MessageMatcher messageEndsWith(final String expectedCauseMessage) {
        return new MessageEndsWithMatcher(expectedCauseMessage);
    }


}
