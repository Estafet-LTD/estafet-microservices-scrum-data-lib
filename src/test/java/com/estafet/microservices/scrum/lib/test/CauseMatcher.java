/**
 * Copyright (c) Arqiva Ltd., 2019. All rights reserved.
 */
package com.estafet.microservices.scrum.lib.test;

import org.hamcrest.TypeSafeMatcher;

/**
 * Marker class for cause matchers.
 *
 * <p>Subclasses of {@link CauseMatcher} do not need to use generics.</p>
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public abstract class CauseMatcher extends TypeSafeMatcher<Throwable> {
    // Nothing to do.
}
