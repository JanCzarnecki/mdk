/**
 * GeneProduct.java
 *
 * 2011.10.07
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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @name    GeneProduct - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractGeneProduct
        extends AbstractAnnotatedEntity implements GeneProduct {

    private static final Logger LOGGER = Logger.getLogger(AbstractGeneProduct.class);
    private Gene gene;

    public AbstractGeneProduct() {
    }

    public AbstractGeneProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public Gene getGene() {
        return this.gene;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    public void readExternal(ObjectInput in, List<Gene> genes) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        int index = in.read();
        gene = index == -1 ? null : genes.get(index);
    }

    public void writeExternal(ObjectOutput out, List<Gene> genes) throws IOException {
        super.writeExternal(out);
        out.write(gene == null ? -1 : genes.indexOf(gene));
    }
}
