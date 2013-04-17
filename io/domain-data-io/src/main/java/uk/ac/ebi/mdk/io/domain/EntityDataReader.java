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

package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class EntityDataReader
        implements EntityReader<Entity> {

    private static final Logger LOGGER = Logger.getLogger(EntityDataReader.class);

    private static final DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();

    private DataInput in;
    private IdentifierInput identifierInput;
    
    private Entity entity;

    public EntityDataReader(DataInput in,
                            IdentifierInput identifierInput) {
        this.in = in;
        this.identifierInput = identifierInput;
    }

    public void setEntity(Entity entity){
        this.entity = entity;
    }
    
    public Entity readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        entity.setName(in.readUTF());
        entity.setAbbreviation(in.readUTF());
        entity.setIdentifier(identifierInput.read());

        return entity;

    }

}
