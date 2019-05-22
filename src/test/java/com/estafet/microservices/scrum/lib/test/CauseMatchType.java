/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

/**
 * How to match the cause.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public enum CauseMatchType {

    /**
     * Ignore the cause and it's associated message..
     */
    IGNORE_CAUSE,

    /**
     * Match the cause, but ignore the associated message.
     */
    IGNORE_CAUSE_MESSAGE,

    /**
     * Match the cause and the associated message.
     */
    MATCH_CAUSE_MESSAGE;
}
