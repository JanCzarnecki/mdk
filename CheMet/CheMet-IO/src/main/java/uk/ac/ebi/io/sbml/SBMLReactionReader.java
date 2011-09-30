
/**
 * SBMLReactionReader.java
 *
 * 2011.08.15
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
package uk.ac.ebi.io.sbml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import uk.ac.ebi.chemet.entities.reaction.AtomContainerReaction;
import uk.ac.ebi.chemet.entities.reaction.filter.AbstractParticipantFilter;
import uk.ac.ebi.chemet.entities.reaction.filter.AcceptAllFilter;
import uk.ac.ebi.chemet.entities.reaction.participant.AtomContainerParticipant;
import uk.ac.ebi.chemet.entities.reaction.participant.GenericParticipant;
import uk.ac.ebi.chemet.exceptions.AbsentAnnotationException;
import uk.ac.ebi.chemet.exceptions.UnknownCompartmentException;
import uk.ac.ebi.chemet.ws.CachedChemicalWS;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.resource.MIRIAMLoader;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.core.Compartment;
import uk.ac.ebi.metabolomes.util.CDKUtils;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.KeggCompoundWebServiceConnection;


/**
 * SBMLReactionReader – 2011.08.15
 *
 * Loads the reactions from an SBML document in to various types of reactions
 *
 * <pre>
 * InputStream sbmlStream                = getClass().getResourceAsStream( "streptomyces-coelicolor-6.2005.xml" );
 * SBMLReactionReader loader                 = SBMLReactionReader.getInstance();
 * List<AtomContainerReaction> reactions = loader.getReactions( sbmlStream );
 *
 * for ( AtomContainerReaction r : reactions ) {
 *     System.out.println( r );
 * }
 * </pre>
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
public class SBMLReactionReader {

    private static final Logger LOGGER = Logger.getLogger( SBMLReactionReader.class );
    private static final SBMLReader reader = new SBMLReader();
    // web service clients
    private final CachedChemicalWS chebiWS = new CachedChemicalWS( new ChEBIWebServiceConnection() );
    private final CachedChemicalWS keggWS = new CachedChemicalWS( new KeggCompoundWebServiceConnection() );
    // sbml storage
    private final SBMLDocument document;
    private final Model model;
    // iterator location and max
    private Integer reactionIndex = -1;
    private final Integer reactionCount;
    private AbstractParticipantFilter filter;


    /**
     * Construct an SBML reaction reader using an input stream. This constructor
     * uses an empty {@see AcceptAllFilter} filter on reaction participants
     *
     * @param stream
     * @throws XMLStreamException
     */
    public SBMLReactionReader( InputStream stream ) throws XMLStreamException {
        this( reader.readSBMLFromStream( stream ) );
    }


    /**
     * Construct an SBML reaction reader using an input stream using a specified participant
     * filter
     * @param stream
     * @param filter
     * @throws XMLStreamException
     */
    public SBMLReactionReader( InputStream stream , AbstractParticipantFilter filter ) throws
      XMLStreamException {
        this( reader.readSBMLFromStream( stream ) , filter );
    }


    /**
     *
     * This constructor uses an empty {@see AcceptAllFilter} (i.e. accept all participants)
     *
     * @param document
     */
    public SBMLReactionReader( SBMLDocument document ) {
        // default filter is an empty instantiation of BasicFilter (accepts all)
        this( document , new AcceptAllFilter() );
    }


    public SBMLReactionReader( SBMLDocument document , AbstractParticipantFilter filter ) {
        this.document = document;
        this.model = document.getModel();
        this.reactionCount = model.getNumReactions();
        this.filter = filter;
    }


    /**
     *
     * Loads the specified Reactions as an AtomContainer reaction (Note: RDF annotations) for
     * ChEBI (KEGG also in future) is required.
     *
     * @param Model SBML Model
     * @return A collection of fully structured reactions
     *
     */
    public List<AtomContainerReaction> getReactions() {

        List<AtomContainerReaction> loadedReactions = new ArrayList<AtomContainerReaction>(
          reactionCount );

        while ( hasNext() ) {
            try {
                AtomContainerReaction reaction = next();
                loadedReactions.add( reaction );
            } catch ( UnknownCompartmentException ex ) {
                LOGGER.warn( ex.getMessage() );
            } catch ( AbsentAnnotationException ex ) {
                LOGGER.warn( ex.getMessage() );
            } catch ( MissingStructureException ex ) {
                LOGGER.warn( ex.getMessage() );
            }
        }

        return loadedReactions;

    }


    /**
     *
     * Loads the SBML {@see Reaction} object as a custom {@see AtomContainerReaction} instance.
     *
     *
     * @param sbmlReaction Instance of a loaded SBML reaction
     * @return Instance of {@see AtomContainerReaction}
     *
     * @throws UnknownCompartmentException Thrown if a compartment name cannot be matched
     * @throws AbsentAnnotationException  Thrown if a reaction is missing RDF CV terms
     * @throws MissingStructureException  Thrown if a structure for the molecule could not be loaded
     *
     */
    public AtomContainerReaction getReaction( Reaction sbmlReaction ) throws
      UnknownCompartmentException ,
      AbsentAnnotationException ,
      MissingStructureException {
        AtomContainerReaction reaction = new AtomContainerReaction( filter );

        for ( int i = 0 ; i < sbmlReaction.getNumReactants() ; i++ ) {

            reaction.addReactant( getParticipant( sbmlReaction.getReactant( i ) ) );
        }

        for ( int i = 0 ; i < sbmlReaction.getNumProducts() ; i++ ) {

            reaction.addProduct( getParticipant( sbmlReaction.getProduct( i ) ) );
        }

        reaction.setIdentifier( new GenericIdentifier( sbmlReaction.getId() ) );

        // TODO(johnmay): Add Enzyme annotations and modifiers

        return reaction;
    }


    /**
     *
     * Constructs a reaction participant (i.e. AtomContainerParticipant or GenericParticipant if the molecule contains
     * and R/Alkyl group)
     *
     * @param   speciesReference An instance of SBML {@see SpeciesReference}
     * @return                   An {@see AtomContainerParticipant} or {@see GenericParticipant}.
     *                           The class {@see GenericParticipant} extends {@see AtomContainerParticipant} and so can
     *                           be handled identically.
     *
     * @throws UnknownCompartmentException Thrown if compartment identifier is not recognised (valid names are found
     *                                     in the {@see Compartment} class)
     * @throws MissingAnnotationException   Thrown
     * @throws MissingStructureException
     *
     */
    public AtomContainerParticipant getParticipant( SpeciesReference speciesReference )
      throws
      UnknownCompartmentException ,
      AbsentAnnotationException ,
      MissingStructureException {

        Species species = speciesReference.getSpeciesInstance();
        Compartment compartment = Compartment.getCompartment(
          species.getCompartmentInstance().getId() );

        if ( compartment == Compartment.UNKNOWN ) {
            throw new UnknownCompartmentException( "Compartment " + species.getCompartmentInstance().
              getId() +
                                                   " was not identifiable" );
        }

        if ( species.getNumCVTerms() == 0 ) {
            throw new AbsentAnnotationException(
              "Species " + species.getId() +
              " did not have any associated Controlled Vocabulary terms" );
        }

        IAtomContainer molecule = getAtomContainer( species );
        Double coefficient = speciesReference.getStoichiometry();

        return CDKUtils.isMoleculeGeneric( molecule ) ?
               new GenericParticipant( molecule , coefficient , compartment ) :
               new AtomContainerParticipant( molecule , coefficient , compartment );

    }


    /**
     *
     * Constructs and IAtomContainer from the given SBML chemical {@see Species} using RDF MIRIAM annotations
     *
     * @param  species An instance of an SBML Species using the attached Controlled Vocabulary (CV) terms. The CV
     *         terms must contain a CHEBI identifier (MIRIAM Registry) to allow fetching of a structure using Web Services
     * @return A instance of a CDK {@see IAtomContainer}
     *
     * @throws MissingStructureException Thrown if an {@see IAtomContainer} can not be built
     *
     */
    public IAtomContainer getAtomContainer( Species species ) throws MissingStructureException {

        for ( int i = 0 ; i < species.getNumCVTerms() ; i++ ) {
            for ( String resource : species.getCVTerm( i ).getResources() ) {

                String id = MIRIAMLoader.getIdentifier( resource );
                if ( id.startsWith( "CHEBI" ) ) {

                    try {
                        return chebiWS.getAtomContainer( id );
                    } catch ( UnfetchableEntry ex ) {
                        LOGGER.debug( "There was a problem loading: " + id + " : " + ex.getMessage() );
                    }

                } else if ( id.startsWith( "C" ) ) {

                    try {
                        return keggWS.getAtomContainer( id );
                    } catch ( UnfetchableEntry ex ) {
                        LOGGER.debug( "There was a problem loading: " + id + " : " + ex.getMessage() );
                    }

                }
            }
        }

        throw new MissingStructureException( species.getId() );

    }


    public boolean hasNext() {
        return reactionIndex + 1 < reactionCount;
    }


    public AtomContainerReaction next() throws UnknownCompartmentException ,
                                               AbsentAnnotationException ,
                                               MissingStructureException {

        reactionIndex++;
        Reaction sbmlReaction = model.getReaction( reactionIndex );

        AtomContainerReaction reaction = getReaction( sbmlReaction );

        return reaction;

    }


}

