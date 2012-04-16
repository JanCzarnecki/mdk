/**
 * AtomContainerParticipant.java
 *
 * 2011.08.12
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
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.interfaces.reaction.Compartment;
import uk.ac.ebi.interfaces.reaction.CompartmentalisedParticipant;
import uk.ac.ebi.interfaces.reaction.Participant;
import uk.ac.ebi.metabolomes.util.CDKUtils;


/**
 * @name    AtomContainerParticipant
 * @date    2011.08.12
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class GenericParticipant extends AtomContainerParticipant {

    private static final Logger LOGGER = Logger.getLogger(GenericParticipant.class);

    private static AtomContainerComparator comparator = new AtomContainerComparator();

    protected IAtomContainer trimmedMolecule;


    public GenericParticipant(IAtomContainer molecule, Double coefficient, Compartment compartment) {
        super(molecule, coefficient, compartment);
        trimmedMolecule = CDKUtils.removePseudoAtoms(molecule);
    }


    public GenericParticipant(IAtomContainer molecule, Double coefficient) {
        super(molecule, coefficient);
        trimmedMolecule = CDKUtils.removePseudoAtoms(molecule);
    }


    public GenericParticipant(IAtomContainer molecule) {
        super(molecule);
        trimmedMolecule = CDKUtils.removePseudoAtoms(molecule);
    }


    public GenericParticipant() {
    }


    @Override
    public void setMolecule(IAtomContainer molecule) {
        super.setMolecule(molecule);
        trimmedMolecule = CDKUtils.removePseudoAtoms(molecule);
    }


    @Override
    public boolean equals(CompartmentalisedParticipant<IAtomContainer, Double, Compartment> other) {

        // other is also of Generic.. so we check raw
        // similarity instead of checking substructure
        if (other instanceof GenericParticipant) {
            return super.equals(other);
        }
        if (this.coefficient != other.getCoefficient()
            && (this.coefficient == null || !this.coefficient.equals(other.getCoefficient()))) {
            return false;
        }

        if (this.compartment != other.getCompartment()
            && (this.compartment == null || !this.compartment.equals(other.getCompartment()))) {
            return false;
        }



        try {
            // the trimmed molecule should be a fragment
            if (this.trimmedMolecule.getAtomCount() >= other.getMolecule().getAtomCount()) {
                return false;
            }

            if (this.trimmedMolecule != other.getMolecule()) {
                Isomorphism comparison = new Isomorphism(Algorithm.DEFAULT, true);
                comparison.init(this.trimmedMolecule, other.getMolecule(), false, true);
                comparison.setChemFilters(false, true, false);
                return comparison.isSubgraph();
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to compare generic reaction participants: " + ex.getMessage());
            return false;
        }
        return true;
    }
}