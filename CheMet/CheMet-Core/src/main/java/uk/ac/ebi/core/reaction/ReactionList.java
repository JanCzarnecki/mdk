/**
 * ReactionSet.java
 *
 * 2011.10.04
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
package uk.ac.ebi.core.reaction;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.Metabolite;

/**
 * @name    ReactionSet - 2011.10.04 <br>
 *           Provides access to a set of reactions and alogrithms that work on them
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public final class ReactionList extends ArrayList<MetabolicReaction> implements Collection<MetabolicReaction> {

    private static final Logger LOGGER = Logger.getLogger(ReactionList.class);
    private Multimap<Metabolite, MetabolicReaction> participantMap = ArrayListMultimap.create();

    public ReactionList() {
    }

    public ReactionList(Collection<MetabolicReaction> reactions) {
        super(reactions);
    }

  
    @Override
    public boolean add(MetabolicReaction rxn) {

        for (Metabolite m : rxn.getAllReactionMolecules()) {
            participantMap.get(m).add(rxn);
        }

        return super.add(rxn);

    }

    @Override
    public boolean addAll(Collection<? extends MetabolicReaction> rxns) {

        boolean changed = false;

        for (MetabolicReaction reaction : rxns) {
            changed = add(reaction) || changed;
        }

        return changed;

    }

    public boolean remove(MetabolicReaction rxn) {
        for (Metabolite m : rxn.getAllReactionMolecules()) {
            participantMap.get(m).remove(rxn);
            if (participantMap.get(m).isEmpty()) {
                participantMap.removeAll(m);
            }
        }
        return super.remove(rxn);
    }

    @Override
    public boolean removeAll(Collection<?> rxns) {

        boolean changed = false;

        for (Object reaction : rxns) {
            changed = remove((MetabolicReaction) reaction) || changed;
        }

        return changed;

    }

    /**
     * Returns a list of reactions that contain Metabolite m
     * If not mapping is found an empty collection is returned
     */
    public Collection<MetabolicReaction> getReactions(Metabolite m) {
        return participantMap.get(m);
    }


}