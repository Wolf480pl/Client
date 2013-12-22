/*
 * This file is part of Spout.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spout is licensed under the Spout License Version 1.
 *
 * Spout is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spout is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spoutcraft.client.universe.store.impl;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicShortIntDirectBackingArray extends AtomicShortIntBackingArray {
    private final static int[] NO_PALETTE = new int[0];
    private final AtomicIntegerArray store;
    private final int width;

    public AtomicShortIntDirectBackingArray(int length) {
        this(length, (AtomicShortIntBackingArray) null);
    }

    public AtomicShortIntDirectBackingArray(AtomicShortIntBackingArray previous) {
        this(previous.length(), previous);
    }

    private AtomicShortIntDirectBackingArray(int length, AtomicShortIntBackingArray previous) {
        super(length);
        store = new AtomicIntegerArray(length);
        width = AtomicShortIntPaletteBackingArray.roundUpWidth(length - 1);
        try {
            copyFromPrevious(previous);
        } catch (PaletteFullException pfe) {
            throw new IllegalStateException("Unable to copy old array to new array");
        }
    }

    public AtomicShortIntDirectBackingArray(int length, int[] initial) {
        super(length);
        if (initial.length != length) {
            throw new IllegalArgumentException("The length of the initialization array must match the given length");
        }
        store = new AtomicIntegerArray(initial);
        width = AtomicShortIntPaletteBackingArray.roundUpWidth(length - 1);
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int getPaletteSize() {
        return length();
    }

    @Override
    public int getPaletteUsage() {
        return length();
    }

    @Override
    public int get(int i) {
        return store.get(i);
    }

    @Override
    public int set(int i, int newValue) throws PaletteFullException {
        return store.getAndSet(i, newValue);
    }

    @Override
    public boolean compareAndSet(int i, int expect, int update) throws PaletteFullException {
        return store.compareAndSet(i, expect, update);
    }

    @Override
    public boolean isPaletteMaxSize() {
        return true;
    }

    @Override
    public int[] getPalette() {
        return NO_PALETTE;
    }

    @Override
    public int[] getBackingArray() {
        return toIntArray(store);
    }
}
