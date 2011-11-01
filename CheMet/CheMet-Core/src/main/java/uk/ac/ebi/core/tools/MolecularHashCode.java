/**
 * MolecularHashCode.java
 *
 * 2011.10.28
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.tools;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *          MolecularHashCode - 2011.10.28 <br>
 *          Generates a hash code for a molecule
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MolecularHashCode {

    private static final Logger LOGGER = Logger.getLogger(MolecularHashCode.class);
    private static Byte index = 42;

    public static int hashCode(IAtomContainer mol) {
        int hash = 0;

        int[] seeds = getAtomSeeds(mol);
        byte[][] table = getConnectionMatrix(mol);

        Map<Integer, MutableInt> codeCount = new HashMap(seeds.length, 1f);
        Map<Integer, MutableInt> occMap = new HashMap(seeds.length, 1f);

        for (int i = 0; i < seeds.length; i++) {

            occMap.clear();
            occMap.put(seeds[i], new MutableInt());
            occMap.get(seeds[i]).increment();

            int value = seeds[i];
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] != 0) {
                    // System.out.printf("\t%12s  %12s  ", value, seeds[j]);
                    value ^= rotate(seeds[j], occMap);
                    //System.out.printf("  %12s\n", value);
                }
            }

            MutableInt count = codeCount.get(value);
            if (count == null) {
                count = new MutableInt();
                codeCount.put(value, count);
            }
            count.increment();

            hash ^= rotate(value, codeCount.get(value).value);

        }
        return hash;
    }

    private static int rotate(int seed, Map<Integer, MutableInt> occMap) {
        if (occMap.get(seed) == null) {
            occMap.put(seed, new MutableInt());
        } else {
            occMap.get(seed).increment();
        }
        // System.out.printf("%10s", occMap.get(seed).value);
        return rotate(seed, occMap.get(seed).value);
    }

    /**
     * Rotates the seed using xor shift the specified number of times
     * @param seed
     * @param rotation
     * @return
     */
    private static int rotate(int seed, int rotation) {
        for (int j = 0; j < rotation; j++) {
            seed = xorShift(seed);
        }
        return seed;
    }

    /**
     * Gets the bond electron matrix for this molecule
     */
    public static byte[][] getConnectionMatrix(IAtomContainer mol) {
        byte[][] connections = new byte[mol.getAtomCount()][mol.getAtomCount()];

        IAtom[] atoms = AtomContainerManipulator.getAtomArray(mol);
        IBond[] bonds = AtomContainerManipulator.getBondArray(mol);

        for (int i = 0; i < bonds.length; i++) {
            IBond bond = bonds[i];
            for (int j = 0; j < bond.getAtomCount(); j++) {
                int index = getIndex(bond.getAtom(j), atoms);
                for (int k = j + 1; k < bond.getAtomCount(); k++) {
                    int index2 = getIndex(bond.getAtom(k), atoms);
                    connections[index][index2] =
                    connections[index2][index] = 1;
                }
            }
        }

        return connections;
    }

    /**
     * Returns the index of the atom in the array of atoms.
     * The property byte value 42 is set once is is found
     * @param atom
     * @param atoms
     * @return
     */
    public static int getIndex(IAtom atom, IAtom[] atoms) {

        if (atom.getProperties().containsKey(index)) {
            return (Integer) atom.getProperty(index);
        }

        for (int i = 0; i < atoms.length; i++) {
            if (atom == atoms[i]) {
                atom.setProperty(index, i);
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates the seeds for all atoms in the molecule. The seeds are
     * calculated first using the atom count % a prime number (257). The
     * atomic number and number of bonded neighbours is also used. Finally
     * the number is xorshift'd between 1 and 5 times based on the low
     * order bits.
     * @param mol
     * @return
     */
    private static int[] getAtomSeeds(IAtomContainer mol) {

        int[] seeds = new int[mol.getAtomCount()];
        int seed = seeds.length % 257;

        for (int i = 0; i < seeds.length; i++) {

            IAtom atom = mol.getAtom(i);
            
            seeds[i] = 257 * seed + atom.getAtomicNumber();               // system number
            seeds[i] = 257 * seeds[i] + mol.getConnectedAtomsCount(atom); // number of bonded neighbours

            // normalise the bit distribution using low-order bits
            for (int j = 0; j < Math.min(seeds[i] & 0x7, 5); j++) {
                seeds[i] = xorShift(seeds[i]);
            }

        }

        return seeds;
    }

    private static int xorShift(int seed) {
        seed ^= seed << 6;
        seed ^= seed >>> 21;
        seed ^= (seed << 7);
        return seed;
    }

    public static void main(String[] args) {
        IAtomContainer mol = MoleculeFactory.make123Triazole();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            MolecularHashCode.hashCode(mol);
        }
        long end = System.currentTimeMillis();
        System.out.println("time taken: " + (end - start));

    }
}

class CodeComparator implements Comparator<Integer> {

    private final Map<Integer, MutableInt> counts;

    public CodeComparator(Map<Integer, MutableInt> counts) {
        this.counts = counts;
    }

    public int compare(Integer o1, Integer o2) {
        Integer o1Value = counts.get(o1).value;
        Integer o2Value = counts.get(o2).value;
        if (o1Value != o2Value) {
            return o1Value.compareTo(o2Value);
        } else {
            return o1.compareTo(o2);
        }
    }
}

class MutableInt {

    int value = 0;

    public void increment() {
        ++value;
    }

    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}