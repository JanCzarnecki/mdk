/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import java.util.List;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.interfaces.identifiers.Identifier;
//import uk.ac.ebi.metabolomes.util.ExternalReference;

/**
 *
 * @author pmoreno
 */
public interface ICrossReferenceProvider {
    //public List<ExternalReference> getCrossReferences(String query);
    public List<CrossReference> getCrossReferences(Identifier query);
}