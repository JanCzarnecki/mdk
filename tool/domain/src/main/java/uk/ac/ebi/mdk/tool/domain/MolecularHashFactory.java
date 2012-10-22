/**
 * MolecularHashFactory.java
 *
 * 2011.11.09
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
package uk.ac.ebi.mdk.tool.domain;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import uk.ac.ebi.mdk.tool.domain.hash.AtomSeed;
import uk.ac.ebi.mdk.tool.domain.hash.AtomicNumberSeed;
import uk.ac.ebi.mdk.tool.domain.hash.ConnectedAtomSeed;
import uk.ac.ebi.mdk.tool.domain.hash.SeedFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.UP;

/**
 * MolecularHashFactory - 2011.11.09 <br>
 * Factory of generating MolecularHash objects. The main method here is
 * {@see getHash(IAtomContainer)} that can be tuned with different
 * {@see AtomSeed}s. The default {@see AtomSeed}s can be altered using
 * {@see addSeedMethod(AtomSeed)} and {@see setSeedMethods(Set)}. Seeds
 * are generated in the {@see SeedFactory}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MolecularHashFactory {

    private static final Logger logger = Logger.getLogger(MolecularHashFactory.class);
    private Collection<AtomSeed> seedMethods = new LinkedHashSet<AtomSeed>();
    private ConnectionMatrixFactory matrixFactory = ConnectionMatrixFactory.getInstance();

    private boolean seedWithMoleculeSize = true;
    private int depth = 1;

    private MolecularHashFactory() {
        seedMethods.add(SeedFactory.getInstance().getSeed(AtomicNumberSeed.class));
        seedMethods.add(SeedFactory.getInstance().getSeed(ConnectedAtomSeed.class));
    }

    public static MolecularHashFactory getInstance() {
        return MolecularHashFactoryHolder.INSTANCE;
    }

    private static class MolecularHashFactoryHolder {

        private static final MolecularHashFactory INSTANCE = new MolecularHashFactory();
    }

    /**
     * Access the list of seed methods. This Set is unmodifiable and new seeds
     * should be added using {@see addSeedMethod(AtomSeed)} or setting the seeds
     * with {@see setSeedMethods(Set)}. <br>
     * <p/>
     * The default seeds are currently {@see AtomicNumberSeed} and
     * {@see ConnectedAtomSeed}
     *
     * @return The set of current seeds
     */
    public Collection<AtomSeed> getSeedMethods() {
        return Collections.unmodifiableCollection(seedMethods);
    }

    /**
     * Allows addition of new seeds to the hash factory. New seeds will alter
     * the specificity of of the hash code to suite certain tasks. An example
     * would be to generate a hash code that incorporates the stereo-chemical
     * properties using the {@see uk.ac.ebi.core.tools.hash.seeds.StereoSeed}.
     * <br>
     * <p/>
     * <b>Example</b>
     * <pre>
     * MolecularHashFactory factory = MolecularHashFactory.getInstance();
     * factory.addSeedMethod(SeedFactory.getInstance().getSeed(StereoSeed.class));
     * </pre>
     *
     * @param seed New AtomSeed to add
     * @return Whether the new seed was added (false if it is already present)
     */
    public boolean addSeedMethod(AtomSeed seed) {
        return this.seedMethods.add(seed);
    }

    /**
     * Sets the seed method set for using the hashing algorithm. This will
     * override all current seeds with the new seed and all subsequent calls
     * to {@see getHash(IAtomContainer)} will be affected.
     *
     * @param seedMethods The new seed methods
     */
    public void setSeedMethods(Collection<AtomSeed> seedMethods) {
        this.seedMethods.clear();
        this.seedMethods.addAll(seedMethods);
    }

    /**
     * Generates a hash code for the molecule with default seeds. The default
     * seeds are currently {@see AtomicNumberSeed} and {@see ConnectedAtomSeed}.
     * These can be modified using the {@see setSeedMethods(Set)} and
     * {@see addSeedMethod(AtomSeed)} methods.
     *
     * @param molecule the molecule to generate the hash for
     * @return The hash for this molecule
     */
    public MolecularHash getHash(IAtomContainer molecule) {
        return getHash(molecule, seedMethods);
    }

    /**
     * Generate a hash code with specified seeds. This method allows overriding
     * of the default seeds for selected entries.
     *
     * @param molecule The molecule to generate the hash for
     * @param methods  The methods that will be used to generate the hash
     * @return The hash for this molecule
     */
    public MolecularHash getHash(IAtomContainer molecule, Collection<AtomSeed> methods) {

        if (molecule.getAtomCount() == 0)
            return new MolecularHash(0, new int[0]);

        int[] precursorSeeds = getAtomSeeds(molecule, methods);
        byte[][] table = matrixFactory.getConnectionMatrix(molecule);
        Set<Integer> centres = getTetrahedralCentres(molecule);

        return getHash(table, precursorSeeds, centres, molecule, true);

    }

    public String toString(int[] seeds) {
        StringBuilder sb = new StringBuilder("[");
        int n = seeds.length - 1;
        for (int i = 0; i <= n; i++) {
            sb.append("0x").append(Integer.toHexString(seeds[i]));
            if (i == n) {
                sb.append("]");
                return sb.toString();
            }
            sb.append(", ");
        }
        throw new IllegalStateException("Unexpected state - could not convert int array");
    }

    public MolecularHash getHash(byte[][] table, int[] precursorSeeds,
                                 Set<Integer> centres, IAtomContainer molecule, boolean shallow) {

       // System.out.println("seeds: " + toString(precursorSeeds));

        int hash = 49157;
        int n = precursorSeeds.length;

        int[] previous = new int[n];
        int[] current = new int[n];

        copy(precursorSeeds, previous);
        copy(precursorSeeds, current);

        Set<Integer> trash = new TreeSet<Integer>();

        // set the parities
        for (Integer centre : centres) {

            Integer storageParity = molecule.getAtom(centre).getStereoParity();
            if (storageParity == 2)
                storageParity = -1;
            int hParity = getParity(table, centre, previous);
            int parity  = storageParity * hParity;
            if (parity != 0) {
                // can't set to previous... as this would alter other centres
                current[centre] *= (parity == -1 ? 1300141 : 1300199);
                trash.add(centre);
            }

        }
        centres.removeAll(trash);
        trash.clear();
        copy(current, previous);  // set the adjusted current values (with parity) to the previous seeds

        HashCounter globalCount = new HashCounter();
        HashCounter localCount = new HashCounter();




        for (int d = 0; d < depth; d++) {

            for (int i = 0; i < previous.length; i++) {

                // local count keeps track of the value of aggregated neighbours
                // therefore we need to clear on each neighbour hash otherwise
                // neighbours which are identical at a given depth would be different
                // due to different occurrence counts
                localCount.clear();

                current[i] = neighbourHash(i, previous[i], 0, table, previous, localCount);

            }

            for (Integer centre : centres) {

                Integer storageParity = molecule.getAtom(centre).getStereoParity();
                if (storageParity == 2)
                    storageParity = -1;
                int hParity = getParity(table, centre, previous);
                int parity  = storageParity * hParity;
                if (parity != 0) {
                    // can't set to previous... as this would alter other centres
                    current[centre] *= (parity == -1 ? 1300141 : 1300199);
                    trash.add(centre);
                }

            }

            centres.removeAll(trash);
            trash.clear();

            for(int i = 0; i < current.length; i++){
                globalCount.register(hash);
                hash ^= rotate(current[i], globalCount.register(current[i]));
            }

            copy(current, previous); // set the current values to the previous and repeat until depth


        }

        // un handled stereo centres need to do ensemble hash -
        if (shallow && !centres.isEmpty()) {

            int[] individual = new int[n];

            hash = 49157;
            for (int i = 0; i < n; i++) {
                //System.out.println("preturbing: " + i);
                int[] preturbed = Arrays.copyOf(precursorSeeds, n);
                preturbed[i] = precursorSeeds[i] * 105341;
                individual[i] = getHash(table, preturbed, new HashSet<Integer>(centres), molecule, false).hash;
                globalCount.register(hash);
                hash ^= rotate(individual[i], globalCount.register(individual[i]));
            }

            return new MolecularHash(hash, individual);
        }


        return new MolecularHash(hash, precursorSeeds);

    }


    /**
     * Returns the hash value xor'd with that of the atom's neighbours. The method
     * is recursive thus the depth indicates the current depth of the method
     * <p/>
     * The max depth is set with the {@see setDepth(int depth)} method.
     *
     * @param index        Atom index to add the neighbour values too
     * @param value        The current value of the above atom
     * @param currentDepth The current depth
     * @return Computed value
     */
    private int neighbourHash(int index, int value, int currentDepth, byte[][] connectionTable, int[] precursorSeeds, HashCounter counter) {

        for (int j = 0; j < connectionTable[index].length; j++) {

            if (connectionTable[index][j] != 0) {
                counter.register(value); // avoid self xor'ing
                int from = rotate(precursorSeeds[j], counter.register(precursorSeeds[j]) - 1);
                int result = value ^ from;
                value = result;

                //  value = currentDepth <= depth
                //          ? neighbourHash(j, value, currentDepth + 1, connectionTable, precursorSeeds, counter) : value;
            }
        }

        return value;

    }


    private static String toString(byte[][] table) {
        StringBuilder sb = new StringBuilder();
        for (byte[] aTable : table)
            sb.append(Arrays.toString(aTable)).append("\n");
        return sb.toString();
    }

    private int getParity(byte[][] table, int i, int[] hashes) {

        byte[] row = table[i];

        int count = 0;
        int n = row.length;

        for (int j = 0; j < n; j++) {

            // if we have a connection
            if (row[j] != 0) {

                int h = hashes[j];

                for (int k = j + 1; k < n; k++) {

                    if (row[k] != 0) {
                        int cmp = hashes[k] - h;

                        // if this value is larger then the last value
                        if (cmp > 0) count++;
                        else if (cmp == 0)
                            return 0;

                    }

                }

            }

        }

        // number of swaps for atom numbers and the hashes
        return (count & 0x1) == 1 ? -1 : +1;

    }

    /**
     * Copies the source array to the destination.
     *
     * @param src  source array
     * @param dest destination array
     */
    private void copy(int[] src, int[] dest) {
        // not using System.arraycopy(src, 0, dest, 0, src.length); due to small overhead
        // note: we do not check they are the same size
        int length = src.length;
        for (int i = 0; i < length; ++i)
            dest[i] = src[i];
    }

    /**
     * Sets the depth to recurse on each atom
     *
     * @param depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * If set to true (default) the base seed will use the molecule size. This
     * will not allow comparison of sub-graph hashes. To allow sub-graph
     * pseudo sub-graph matching {@see MolecularHash#getSimilarity(MolecularHash)}
     * this should be set to false
     *
     * @param useMoleculeSize
     */
    public void setSeedWithMoleculeSize(boolean useMoleculeSize) {
        this.seedWithMoleculeSize = useMoleculeSize;
    }

    /**
     * Generates an array of atomic seed values for each atom in the molecule.
     * These seeds are generated using the provided methods
     *
     * @param molecule the molecule to generate the seeds for
     * @return array of integers representing the seeds for each atom in the
     *         molecule
     */
    public int[] getAtomSeeds(IAtomContainer molecule, Collection<AtomSeed> methods) {

        int[] seeds = new int[molecule.getAtomCount()];
        int seed = seedWithMoleculeSize ? 389 % seeds.length : 389;

        for (int i = 0; i < seeds.length; i++) {

            seeds[i] = seed;

            for (AtomSeed method : methods) {
                seeds[i] = 257 * seeds[i] + method.seed(molecule,
                                                        molecule.getAtom(i));
            }

            // rotate the seed 1-7 times (using mask to get the lower bits)
            seeds[i] = rotate(seeds[i], seeds[i] & 0x7);

        }

        return seeds;

    }

    private Set<Integer> getTetrahedralCentres(IAtomContainer container) {

        Set<Integer> centres = new TreeSet<Integer>();

        for (int i = 0; i < container.getAtomCount(); i++) {
            if (candidateTetrahedralCenter(container, container.getAtom(i))) {
                centres.add(i);
            }
        }

        return centres;

    }

    private boolean candidateTetrahedralCenter(IAtomContainer container, IAtom atom) {


        if ((atom.getStereoParity() != null
                && (atom.getStereoParity() == 1 || atom.getStereoParity() == 2)
                && SP3.equals(atom.getHybridization())
                && atom.getFormalNeighbourCount() > 2)) {
            for (IBond bond : container.getConnectedBondsList(atom)) {

                IBond.Stereo stereo = bond.getStereo();

                if (UP.equals(stereo) || DOWN.equals(stereo)) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Performs pseudo random number generation on the provided seed
     *
     * @param seed
     * @return
     */
    public static int xorShift(int seed) {
        seed ^= seed << 6;
        seed ^= seed >>> 21;
        seed ^= (seed << 7);
        return seed;
    }

    /**
     * Rotates the seed using xor shift (pseudo random number generation) the
     * specified number of times.
     *
     * @param seed     the starting seed
     * @param rotation Number of xor rotations to perform
     * @return The starting seed rotated the specified number of times
     */
    public static int rotate(int seed, int rotation) {
        for (int j = 0; j < rotation; j++) {
            seed = xorShift(seed);
        }
        return seed;
    }

    /**
     * Rotates the seed if the seed has already been seen in the provided
     * occurrences map
     *
     * @param seed
     * @param occurences
     * @return
     */
    public static int rotate(int seed, Map<Integer, MutableInt> occurences) {
        if (occurences.get(seed) == null) {
            occurences.put(seed, new MutableInt());
        } else {
            occurences.get(seed).increment();
        }
        return rotate(seed, occurences.get(seed).intValue() - 1);
    }


    private class HashCounter extends OccurrenceCounter<Integer> {
    }

}
