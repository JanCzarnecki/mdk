/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

package uk.ac.ebi.mdk.tool.domain;

import com.google.common.base.Predicate;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * Checks whether a MetabolicReaction has generic molecule participants.
 *
 * @author Pablo Moreno
 */
public class GenericStructureParticipant
        implements Predicate<MetabolicReaction> {

    /**
     * Returns true if the given reaction has at least one molecule with generic
     * structure among its participants.
     *
     * @param reaction the reaction to check for generic participants.
     * @return true if at least one participant is generic.
     */
    @Override
    public boolean apply(MetabolicReaction reaction) {
        for (MetabolicParticipant p : reaction.getParticipants()) {
            if (p.getMolecule() != null && p.getMolecule().isGeneric()) {
                return true;
            }
        }
        return false;
    }


}
