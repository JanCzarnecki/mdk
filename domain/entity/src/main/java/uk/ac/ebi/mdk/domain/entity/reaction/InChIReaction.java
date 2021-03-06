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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.entity.reaction.filter.InChIFilter;

import java.util.UUID;

/**
 * @name    InChIReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Implementation of Reaction using InChI to represent the reaction participants
 *
 */
public class InChIReaction
        extends AbstractReaction<InChIParticipant> {

    private static final Logger LOGGER = Logger.getLogger( InChIReaction.class );

    public InChIReaction(UUID uuid) {
        super(uuid);
    }
    public InChIReaction(InChIFilter filter) {
        super(filter);
    }

    public void addReactant( InChI inChI , Double d , Compartment compartment ) {
        super.addReactant( new InChIParticipant( inChI , d , compartment ) );
    }

    public void addProduct( InChI inChI , Double d , Compartment compartment ) {
        super.addProduct( new InChIParticipant( inChI , d , compartment ) );
    }
}
