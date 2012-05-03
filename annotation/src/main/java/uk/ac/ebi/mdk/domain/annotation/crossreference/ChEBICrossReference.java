/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.mdk.domain.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.resource.DefaultLoader;


/**
 *          ChEBICrossReference – 2011.09.14 <br>
 *          Class metaInfo
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context(Metabolite.class)
@Brief("ChEBI Cross-reference")
@Description("A cross-reference specific to the ChEBI database")
public class ChEBICrossReference<O extends Observation>
        extends CrossReference<ChEBIIdentifier, O> {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReference.class);

    private static uk.ac.ebi.core.MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            ChEBICrossReference.class);


    public ChEBICrossReference() {
    }


    public ChEBICrossReference(ChEBIIdentifier identifier) {
        super(identifier);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }





    /**
     * @inheritDoc
     */
    @Override
    public ChEBICrossReference newInstance() {
        return new ChEBICrossReference();
    }
}
