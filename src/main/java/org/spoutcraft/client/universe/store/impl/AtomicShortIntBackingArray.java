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

import gnu.trove.set.hash.TIntHashSet;

public abstract class AtomicShortIntBackingArray {
    private final int length;

    /**
     * Creates an AtomicShortIntArray
     *
     * @param length the number of entries
     */
    public AtomicShortIntBackingArray(int length) {
        this.length = length;
    }

    /**
     * Gets the width of the internal array, in bits
     *
     * @return the width
     */
    public abstract int width();

    /**
     * Gets the length of the array
     *
     * @return the length
     */
    public int length() {
        return length;
    }

    /**
     * Gets the size of the internal palette
     *
     * @return the palette size
     */
    public abstract int getPaletteSize();

    /**
     * Gets the number of palette entries in use
     *
     * @return the number of entries
     */
    public abstract int getPaletteUsage();

    /**
     * Gets an element from the array at a given index
     *
     * @param i the index
     * @return the element
     */
    public abstract int get(int i);

    /**
     * Sets an element to the given value
     *
     * @param i the index
     * @param newValue the new value
     * @return the old value
     */
    public abstract int set(int i, int newValue) throws PaletteFullException;

    /**
     * Sets the element at the given index, but only if the previous value was the expected value.
     *
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true on success
     */
    public abstract boolean compareAndSet(int i, int expect, int update) throws PaletteFullException;

    public abstract boolean isPaletteMaxSize();

    /**
     * Gets the number of unique entries in the array
     */
    public int getUnique() {
        return getUnique(new TIntHashSet());
    }

    /**
     * Gets the number of unique entries in the array
     *
     * @param inUseSet set to use to store used ids
     */
    public int getUnique(TIntHashSet inUseSet) {
        inUseSet.clear();
        int unique = 0;
        for (int i = 0; i < length; i++) {
            if (inUseSet.add(get(i))) {
                unique++;
            }
        }
        return unique;
    }

    /**
     * Gets the palette in use by the backing array or an array of zero length if no palette is in use.
     */
    public abstract int[] getPalette();

    /**
     * Gets the packed array used by the backing store.  This is a flat array if there is no palette in use.
     */
    public abstract int[] getBackingArray();

    protected void copyFromPrevious(AtomicShortIntBackingArray previous) throws PaletteFullException {
        if (previous != null) {
            for (int i = 0; i < length; i++) {
                set(i, previous.get(i));
            }
        } else {
            set(0, 0);
        }
    }

    protected static int[] toIntArray(AtomicIntegerArray array, int length) {
        int[] packed = new int[length];
        for (int i = 0; i < length; i++) {
            packed[i] = array.get(i);
        }
        return packed;
    }

    protected static int[] toIntArray(AtomicIntegerArray array) {
        int length = array.length();
        int[] packed = new int[length];
        for (int i = 0; i < length; i++) {
            packed[i] = array.get(i);
        }
        return packed;
    }
}
