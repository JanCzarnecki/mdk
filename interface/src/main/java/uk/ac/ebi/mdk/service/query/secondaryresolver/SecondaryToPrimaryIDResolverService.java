/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.service.query.secondaryresolver;

import java.util.Collection;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

/**
 *
 * @author pmoreno
 */
public interface SecondaryToPrimaryIDResolverService<I extends Identifier> extends QueryService<I> {

    /**
     * The method takes an identifier, presumed to be a secondary Identifier of the database, looks for a primary id associated in the
     * index and returns it. If the identifier is a primary identifier, the same identifier is returned.
     * @param secondaryIdent
     * @return primaryIdentifier
     */
    I getPrimaryID(I secondaryIdent);

    Collection<I> getSecondaryIDsForPrimaryID(I primaryIdentifier);
    
}
