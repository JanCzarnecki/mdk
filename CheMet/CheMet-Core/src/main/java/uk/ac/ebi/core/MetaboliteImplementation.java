/**
 * Metabolite.java
 *
 * 2011.09.05
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
package uk.ac.ebi.core;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.core.metabolite.MetaboliteClassImplementation;
import uk.ac.ebi.interfaces.MetaboliteClass;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;


/**
 *          Metabolite – 2011.09.05 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteImplementation
        extends AbstractAnnotatedEntity
        implements Metabolite {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteImplementation.class);

    private boolean generic = false;

    private Double charge = 0d;

    private MetaboliteClass type = MetaboliteClassImplementation.UNKNOWN;

    public static final String BASE_TYPE = "Metabolite";


    public MetaboliteImplementation() {
    }


    public MetaboliteImplementation(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    /**
     *
     * Convenience constructor wraps accession in a BasicChemicalIdentifier
     *
     * @param accession
     * @param abbreviation
     * @param name
     * 
     */
    public MetaboliteImplementation(String accession, String abbreviation, String name) {
        super(new BasicChemicalIdentifier(accession), abbreviation, name);
    }


    /**
     *
     * Accessor to whether the molecule is generic (contains one or more -R groups)
     *
     * @return
     */
    public boolean isGeneric() {
        return generic;
    }


    /**
     *
     * Sets whether the molecule is generic (has -R group)
     *
     */
    public void setGeneric(boolean generic) {
        this.generic = generic;
    }


    /**
     *
     * Access the charge of this molecule
     *
     * @return
     */
    public Double getCharge() {
        return charge;
    }


    /**
     *
     * Molecule charge mutator
     *
     */
    public void setCharge(Double charge) {
        this.charge = charge;
    }


    /**
     * @inheritDoc
     */
    public Class<? extends Entity> getEntityClass() {
        return Metabolite.class;
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        type = MetaboliteClassImplementation.valueOf(in.readByte());
        generic = in.readBoolean();
        charge = in.readDouble();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeByte(type.getIndex());
        out.writeBoolean(generic);
        out.writeDouble(charge);
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaboliteImplementation other = (MetaboliteImplementation) obj;

        if (super.equals(other) == false) {
            return false;
        }

        if (this.generic != other.generic) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }

        return true;
    }


    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return Objects.hashCode(hash, generic, type);
    }


    public MetaboliteClass getType() {
        return type;
    }


    public void setType(MetaboliteClass type) {
        this.type = type;
    }


    public Entity newInstance() {
        return new MetaboliteImplementation();
    }


    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }


    /**
     * @inheritDoc
     */
    public boolean hasStructure() {
        return super.hasAnnotation(AtomContainerAnnotation.class) || super.hasAnnotation(InChI.class);
    }


    /**
     * @inheritDoc
     */
    public Collection<ChemicalStructure> getStructures() {
        return getAnnotationsExtending(ChemicalStructure.class);
    }
}
