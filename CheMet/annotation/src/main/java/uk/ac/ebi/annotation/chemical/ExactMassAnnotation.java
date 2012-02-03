/**
 * SMILESAnnotation.java
 *
 * 2012.02.03
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
package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractFloatAnnotation;
import uk.ac.ebi.annotation.AbstractStringAnnotation;
import uk.ac.ebi.interfaces.Annotation;

/**
 * @name    SMILESAnnotation
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ExactMassAnnotation extends AbstractFloatAnnotation {
    
    private static final Logger LOGGER = Logger.getLogger(ExactMassAnnotation.class);
    

    public ExactMassAnnotation() {
    }
    
    public ExactMassAnnotation(Float exactMass) {
        super.setValue(exactMass);
    }
    
    public Annotation getInstance() {
        return new ExactMassAnnotation();
    }

    public Annotation getInstance(Float exactMass) {
        return new ExactMassAnnotation(exactMass);
    }

    @Override
    public String getShortDescription() {
        return "Exact Mass";
    }

    @Override
    public String getLongDescription() {
        return "The exact mass is the sum of the masses of the atoms in a molecule using the most abundant isotope for each element.";
    }
    
    
}
