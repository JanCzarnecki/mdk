
package uk.ac.ebi.mdk.domain;

/**
 * AbstractDescriptor.java
 *
 * 2011.09.14
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
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.tool.MetaInfoLoader;


/**
 *          AbstractDescriptor – 2011.09.14 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractDescriptor
  implements Descriptor {

    private static final Logger LOGGER = Logger.getLogger(AbstractDescriptor.class);
    // short/long description and index are loaded from a properties file
    private MetaInfoLoader loader;


    public AbstractDescriptor(MetaInfoLoader loader) {
        this.loader = loader;
    }


    /**
     * @inheritDoc
     */
    public String getShortDescription() {
        return loader.getShortDescription(getClass());
    }


    /**
     * @inheritDoc
     */
    public String getLongDescription() {
        return loader.getLongDescription(getClass());
    }


}

