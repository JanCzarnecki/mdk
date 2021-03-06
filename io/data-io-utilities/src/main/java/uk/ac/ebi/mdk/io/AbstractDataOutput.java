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

package uk.ac.ebi.mdk.io;


import org.apache.commons.lang.mutable.MutableInt;
import uk.ac.ebi.caf.utility.version.Version;

import java.io.DataOutput;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * AbstractDataOutput - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AbstractDataOutput<M> extends MarshalManager<M> {

    private DataOutput out;
    private Map<Object, Integer> objectIds = new IdentityHashMap<Object, Integer>(5000);
    private MutableInt iterator = new MutableInt(Integer.MIN_VALUE + 10);

    public AbstractDataOutput(DataOutput out, Version v) {
        super(v);
        this.out = out;
    }

    public void writeClass(Class c) throws IOException {
        if (writeObjectId(c)) {
            out.writeUTF(c.getName());
        }
    }

    public DataOutput getDataOutput() {
        return out;
    }


    /**
     * @param obj
     *
     * @return whether a new object id was written
     *
     * @throws IOException
     */
    public boolean writeObjectId(Object obj) throws IOException {

        if (objectIds.containsKey(obj)) {
            out.writeInt(objectIds.get(obj));
            return false;
        }

        out.writeInt(newObjectId(obj));

        return true;

    }

    public Integer newObjectId(Object obj) {

        if (objectIds.containsKey(obj))
            throw new InvalidParameterException("Object already has id!");

        iterator.increment();

        if (iterator.toInteger().equals(Integer.MAX_VALUE)) {
            System.err.println("Fatal error - run out of unique object ids");
            System.exit(0);
        }

        objectIds.put(obj, iterator.toInteger());
        return iterator.intValue();
    }

}
