/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

/**
 * NonNullAtomicNumberSeed.java
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
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.BitSet;

/**
 * Uses the bond order sum to provide a seed for the an atom. The bond order sum
 * is calculated using {@see AtomContainerManipulator#getBondOrderSum(IAtomContainer,
 *IAtom)}). The seed is used in the MolecularHashFactory when creating a hash
 * for a molecule.
 *
 * @author johnmay
 * @see AtomContainerManipulator#getBondOrderSum(IAtomContainer, IAtom)
 * @see uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory
 */
public class BondOrderSumSeed implements AtomSeed, MaskedSeed {

    public BondOrderSumSeed() {
    }

    public int seed(IAtomContainer molecule, IAtom atom) {
        return Double.valueOf(AtomContainerManipulator
                                      .getBondOrderSum(molecule, atom))
                     .hashCode();
    }

    @Override
    public int seed(IAtomContainer molecule, IAtom atom, BitSet mask) {
        double sum = 0;
        for (IBond bond : molecule.getConnectedBondsList(atom)) {
            if (mask.get(molecule.getAtomNumber(bond.getConnectedAtom(atom))))
                sum += order(bond.getOrder());
        }
        return ((Double) sum).hashCode();
    }

    // can be replaced with the new Order.numeric() field once it is released
    private static int order(IBond.Order order) {
        switch (order) {
            case SINGLE:
                return 1;
            case DOUBLE:
                return 2;
            case TRIPLE:
                return 3;
            case QUADRUPLE:
                return 4;
            default:
                return 1;
        }
    }

    @Override
    public String toString() {
        return "Bond Order Sum";
    }

}
