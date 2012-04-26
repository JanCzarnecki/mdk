/**
 * SelectionMap.java
 *
 * 2011.10.14
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
package uk.ac.ebi.mdk.domain.entity.collection;

import java.util.Collection;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;

/**
 * EntityCollection - 2011.10.14 <br>
 * 
 * An interface to manage and retrieve selections of different core entities.
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * 
 */
public interface EntityCollection {

    /**
     * Access entire selection
     * @return
     */
    public Collection<AnnotatedEntity> getEntities();

    /**
     * Add a component to the selection
     * @return Whether the selection was changed
     */
    public boolean add(AnnotatedEntity entity);

    /**
     * Add a collection of entities to the selection
     * @return Whether the selection was changed
     */
    public boolean addAll(Collection<? extends AnnotatedEntity> entities);

    /**
     * Remove all selection objects
     * @return The manager returns an instance of it's self allow chaining
     *         (e.g. selection.clear().add())
     */
    public EntityCollection clear();

    /**
     * Allows fetching of specified
     * @param type
     * @return
     */
    public <T> Collection<T> get(Class<T> type);

    /**
     * Allows access to all gene products (Protein/RNA) in one selection
     * @return
     */
    public Collection<GeneProduct> getGeneProducts();

    /**
     * Access whether this selection manager has a selection
     * @return 
     */
    public boolean hasSelection();

    /**
     * Access whether this selection manager has a selection of the specified
     * class type
     * @return
     */
    public boolean hasSelection(Class<?> type);

    /**
     * Determine whether the selection is empty
     * @return
     */
    public boolean isEmpty();

    /**
     * Access the first entity of a selection. If there is more then one class
     * of selection first entity is determined by class with the most entities
     * 
     * @return
     */
    public AnnotatedEntity getFirstEntity();

}
