/**
 * BasicGeneIdentifier.java
 *
 * 2011.10.17
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
package uk.ac.ebi.resource.rna;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *          BasicGeneIdentifier - 2011.10.17 <br>
 *          A basic RNA identifier
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BasicRNAIdentifier extends RNAIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicRNAIdentifier.class);
    private static int ticker = 0;

    public BasicRNAIdentifier() {
    }

    public BasicRNAIdentifier(String accession) {
        super(accession);
    }

    /**
     * Convenience method for creating basic identifiers. It will return the
     * next incremented identifier to where it's ticker is at (resets) every
     * jvm instance
     */
    public static Identifier nextIdentifier() {
        return new BasicRNAIdentifier(String.format("rna_%05d", ++ticker));
    }

    public Identifier newInstance() {
        return new BasicRNAIdentifier();
    }
}
