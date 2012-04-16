/**
 * DefaultEntityFactory.java
 *
 * 2012.02.02
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
package uk.ac.ebi.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.entities.reaction.AbstractReaction;
import uk.ac.ebi.chemet.entities.reaction.participant.BasicParticipant;
import uk.ac.ebi.chemet.entities.reaction.participant.ParticipantImplementation;
import uk.ac.ebi.core.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.MetabolicParticipant;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.reaction.CompartmentalisedParticipant;
import uk.ac.ebi.interfaces.reaction.Participant;


/**
 * DefaultEntityFactory 2012.02.02
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Provides centralised entity creation.
 * @version $Rev$ : Last Changed $Date$
 */
public class DefaultEntityFactory
        implements EntityFactory {

    private static final Logger LOGGER = Logger.getLogger(DefaultEntityFactory.class);

    private Map<Class, Entity> entites = new HashMap<Class, Entity>();

    private final Map<Class, Class> ENTITY_INTERFACE_MAP = new HashMap<Class, Class>(20);


    private DefaultEntityFactory() {

        for (Entity entity : Arrays.asList(new MetaboliteImplementation(),
                                           new MetabolicReactionImplementation(),
                                           new ProteinProductImplementation(),
                                           new RibosomalRNAImplementation(),
                                           new TransferRNAImplementation(),
                                           new GeneImplementation(),
                                           new ChromosomeImplementation(),
                                           new GenomeImplementation(),
                                           new MultimerImplementation(),
                                           new IdentifierReactionImplementation(),
                                           new BasicParticipant(),
                                           new ParticipantImplementation(),
                                           new MetabolicParticipantImplementation(),
                                           new AbstractReaction(),
                                           new Reconstruction())) {

            entites.put(getEntityClass(entity.getClass()), entity);

            // remember to check the newInstance method of the entity if something is not working!

        }

    }


    public static EntityFactory getInstance() {
        return DefaultEntityFactoryHolder.INSTANCE;
    }


    /**
     * @inheritDoc
     */
    public <E extends Entity> E newInstance(Class<E> c) {
        return (E) entites.get(c).newInstance();
    }

    public <E extends Entity> E ofClass(Class<E> c) {
        return (E) entites.get(c).newInstance();
    }

    /**
     * @inheritDoc
     */
    public <E extends Entity> E newInstance(Class<E> c,
                                            Identifier identifier,
                                            String name,
                                            String abbr) {

        E entity = (E) entites.get(c).newInstance();

        entity.setIdentifier(identifier);
        entity.setName(name);
        entity.setAbbreviation(abbr);

        return entity;

    }

    /**
     * @inheritDoc
     */
    public <E extends Entity> E ofClass(Class<E> c,
                                        Identifier identifier,
                                        String name,
                                        String abbr) {

        E entity = (E) entites.get(c).newInstance();

        entity.setIdentifier(identifier);
        entity.setName(name);
        entity.setAbbreviation(abbr);

        return entity;

    }


    /**
     * @inheritDoc
     */
    public final Class<? extends Entity> getEntityClass(Class<? extends Entity> c) {

        if (c.isInterface()
                && Entity.class.isAssignableFrom(c)) {
            return c;
        }

        if (ENTITY_INTERFACE_MAP.containsKey(c)) {
            return ENTITY_INTERFACE_MAP.get(c);
        }

        for (Class i : c.getInterfaces()) {
            if (Entity.class.isAssignableFrom(i)) {
                ENTITY_INTERFACE_MAP.put(c, i);
                return i;
            }
        }

        LOGGER.warn("No direct interface found for " + c);

        return null;

    }


    /**
     * @inheritDoc
     */
    public Class<? extends Entity> getRootClass(Class<? extends Entity> c) {

        while (getEntityClass(getSuperClass(c)) != AnnotatedEntity.class) {
            c = getSuperClass(c);
        }

        return getEntityClass(c);
    }


    private Class<? extends Entity> getSuperClass(Class<? extends Entity> c) {
        if (c.isInterface()) {
            return (Class<? extends Entity>) c.getInterfaces()[0]; // can only have one
        }
        return (Class<? extends Entity>) c.getSuperclass();
    }


    public Class<? extends Entity> getClass(String className) {

        for (Class<? extends Entity> entityClass : entites.keySet()) {
            if (entityClass.getName().equals(className)) {
                return entityClass;
            }
        }

        return null;
    }


    private static class DefaultEntityFactoryHolder {

        private static final DefaultEntityFactory INSTANCE = new DefaultEntityFactory();
    }
}