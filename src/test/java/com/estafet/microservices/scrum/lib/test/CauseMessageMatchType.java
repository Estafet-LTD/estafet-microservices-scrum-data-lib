/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

/**
 * How to match the cause message.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public enum CauseMessageMatchType {

    /**
     * Ignore the cause message.
     */
    IGNORE_CAUSE_MESSAGE,

    /**
     * The cause message must be {@code null}.
     */
    CAUSE_MESSAGE_IS_NULL,

    /**
     * The actual cause message must be the same as the actual cause message.
     */
    CAUSE_MESSAGE_IS_SAME,

    /**
     * The actual cause message must contain the expected cause message.
     */
    CAUSE_MESSAGE_CONTAINS,

    /**
     * The actual cause message must start with the expected cause message.
     */
    CAUSE_MESSAGE_STARTS_WITH,

    /**
     * The actual cause message must end with the expected cause message.
     */
    CAUSE_MESSAGE_ENDS_WITH;
}
