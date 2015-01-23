/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.cluster.raft;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import scala.concurrent.duration.FiniteDuration;

public class FollowerLogInformationImpl implements FollowerLogInformation {

    private final String id;

    private final AtomicLong nextIndex;

    private final AtomicLong matchIndex;

    private final Stopwatch stopwatch;

    private final long followerTimeoutMillis;

    public FollowerLogInformationImpl(String id, long nextIndex,
        long matchIndex, FiniteDuration followerTimeoutDuration) {
        this.id = id;
        this.nextIndex = new AtomicLong(nextIndex);
        this.matchIndex = new AtomicLong(matchIndex);
        this.stopwatch = new Stopwatch();
        this.followerTimeoutMillis = followerTimeoutDuration.toMillis();
    }

    @Override
    public long incrNextIndex(){
        return nextIndex.incrementAndGet();
    }

    @Override
    public long decrNextIndex() {
        return nextIndex.decrementAndGet();
    }

    @Override
    public void setNextIndex(long nextIndex) {
        this.nextIndex.set(nextIndex);
    }

    @Override
    public long incrMatchIndex(){
        return matchIndex.incrementAndGet();
    }

    @Override
    public void setMatchIndex(long matchIndex) {
        this.matchIndex.set(matchIndex);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getNextIndex() {
        return nextIndex.get();
    }

    @Override
    public long getMatchIndex() {
        return matchIndex.get();
    }

    @Override
    public boolean isFollowerActive() {
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        return (stopwatch.isRunning()) && (elapsed <= followerTimeoutMillis);
    }

    @Override
    public void markFollowerActive() {
        if (stopwatch.isRunning()) {
            stopwatch.reset();
        }
        stopwatch.start();
    }

    @Override
    public void markFollowerInActive() {
        if (stopwatch.isRunning()) {
            stopwatch.stop();
        }
    }
}
