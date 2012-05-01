package uk.ac.ebi.chemet.service.query.name;

import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.service.index.name.KEGGCompoundNameIndex;
import uk.ac.ebi.chemet.service.query.AbstractLuceneService;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;

import java.util.Collection;
import java.util.HashSet;

/**
 * KEGGCompoundNameService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundNameService
        extends AbstractLuceneService<KEGGCompoundIdentifier>
        implements
                   PreferredNameService<KEGGCompoundIdentifier>,
                   SynonymService<KEGGCompoundIdentifier>,
                   NameService<KEGGCompoundIdentifier>{

    public KEGGCompoundNameService() {
        super(new KEGGCompoundNameIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<KEGGCompoundIdentifier> searchName(String name, boolean approximate) {
        // use set as to avoid duplicates
        Collection<KEGGCompoundIdentifier> identifiers = new HashSet<KEGGCompoundIdentifier>();

        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));

        return identifiers;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(KEGGCompoundIdentifier identifier) {
        // use set as to avoid duplicates
        Collection<String> names = new HashSet<String>();

        names.add(getPreferredName(identifier));
        names.addAll(getSynonyms(identifier));

        names.remove(""); // remove empty results


        return names;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<KEGGCompoundIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(KEGGCompoundIdentifier identifier) {
        return firstValue(identifier, PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<KEGGCompoundIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(KEGGCompoundIdentifier identifier) {
        return firstValues(identifier, SYNONYM);
    }

    /**
     * @inheritDoc
     */
    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }
}
