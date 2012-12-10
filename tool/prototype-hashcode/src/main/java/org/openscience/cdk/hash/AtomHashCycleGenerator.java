/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openscience.cdk.hash;

import org.openscience.cdk.parity.component.StereoComponent;

import java.util.BitSet;

/**
 * Describes a hash generator which cycles over the connections and includes
 * invariant information of the neighbours
 *
 * @author John May
 */
public interface AtomHashCycleGenerator extends AtomHashGenerator {

    Long[] combined(int[][] connections, Long[] invariants, StereoComponent<Long> stereo, BitSet reducible);

}
