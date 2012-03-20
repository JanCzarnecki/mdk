/*
 * Copyright (c) 2012. EMBL-EBI
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

package uk.ac.ebi.chemet.io.identifier;

import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * IdentifierReader - 12.03.2012 <br/>
 * <p/>
 * The IdentifierReader interface describes an io marshal
 * class that can read a single identifier type from a stream.
 * There should be a different reader for each identifier type, however
 * in most case the only part that need writing is the accession. Therefore
 * there may be a basic identifier writer that will roll-back if no other readers
 * are available
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IdentifierReader<I extends Identifier> {

    /**
     * Read the identifier data
     *
     * @return instance of the new identifier
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    public I readIdentifier() throws IOException, ClassNotFoundException;

}
