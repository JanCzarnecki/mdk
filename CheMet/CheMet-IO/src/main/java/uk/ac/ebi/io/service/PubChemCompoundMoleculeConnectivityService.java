/**
 * ChEBINameService.java
 *
 * 2011.10.26
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
package uk.ac.ebi.io.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.interfaces.services.ChemicalConnectivityQueryService;
import uk.ac.ebi.io.remote.PubChemCompoundConnectivity;
import uk.ac.ebi.io.remote.PubChemCompoundConnectivity.PChemCompConnectivityLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.chemical.PubChemCompoundIdentifier;

/**
 *          ChEBIMoleculeConnectivityService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemCompoundMoleculeConnectivityService
        extends MoleculeConnectivityQueryService implements ChemicalConnectivityQueryService<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundMoleculeConnectivityService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private PubChemCompoundMoleculeConnectivityService() {
        super(new PubChemCompoundConnectivity(1, 10));
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static PubChemCompoundMoleculeConnectivityService getInstance() {
        return PubChemCompoundMoleculeConnectivityServiceHolder.INSTANCE;
    }

    public Collection<PubChemCompoundIdentifier> getEntriesWithConnectivity(String connectivity) {
        Query queryConnectivity = new TermQuery(new Term(PChemCompConnectivityLuceneFields.InChIConnectivity.toString(), connectivity));
        
        return reverseSearch(queryConnectivity);
    }

    private static class PubChemCompoundMoleculeConnectivityServiceHolder {

        private static final PubChemCompoundMoleculeConnectivityService INSTANCE = new PubChemCompoundMoleculeConnectivityService();
    }

    private Collection<PubChemCompoundIdentifier> reverseSearch(Query query) {
        Collection<PubChemCompoundIdentifier> ids = new HashSet<PubChemCompoundIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                PubChemCompoundIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, PChemCompConnectivityLuceneFields.CID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    @Override
    public PubChemCompoundIdentifier getIdentifier() {
        return new PubChemCompoundIdentifier();
    }

}