
/**
 * AnnotationFactory.java
 *
 * 2011.09.12
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
package uk.ac.ebi.annotation.util;

import java.io.IOException;
import java.io.ObjectInput;
import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;
import java.util.Arrays;
import uk.ac.ebi.interfaces.Annotation;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AuthorAnnotation;
import uk.ac.ebi.annotation.Subsystem;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.Classification;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.annotation.chemical.ChemicalStructure;
import uk.ac.ebi.annotation.chemical.MolecularFormula;


/**
 *          AnnotationFactory – 2011.09.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationFactory {

    private static final Logger LOGGER = Logger.getLogger(AnnotationFactory.class);
    private static final ChemicalStructure CHEMICAL_STRUCTURE = new ChemicalStructure();
    private static final MolecularFormula FORMULA = new MolecularFormula();
    private static final AuthorAnnotation AUTHOR_ANNOTATION = new AuthorAnnotation();
    private static final CrossReference CROSS_REFERENCE = new CrossReference();
    private static final Classification CLASSIFICATION = new Classification();
    private static final EnzymeClassification ENZYME_CLASSIFICATION = new EnzymeClassification();
    private static final ChEBICrossReference CHEBI_CROSS_REFERENCE = new ChEBICrossReference();
    // reflective map
    private static Constructor[] constructors = new Constructor[Byte.MAX_VALUE];
    private static Annotation[] instances = new Annotation[Byte.MAX_VALUE];


    public static AnnotationFactory getInstance() {
        return AnnotationFactoryHolder.INSTANCE;
    }


    private static class AnnotationFactoryHolder {

        private static AnnotationFactory INSTANCE = new AnnotationFactory();
    }


    private AnnotationFactory() {
        try {
            for( Annotation annotation : Arrays.asList(CHEMICAL_STRUCTURE,
                                                       FORMULA,
                                                       AUTHOR_ANNOTATION,
                                                       CROSS_REFERENCE,
                                                       CLASSIFICATION,
                                                       ENZYME_CLASSIFICATION, CHEBI_CROSS_REFERENCE,
                                                       new Subsystem()) ) {

                constructors[annotation.getIndex()] = annotation.getClass().getConstructor();
                instances[annotation.getIndex()] = annotation;

            }
        } catch( Exception e ) {
            LOGGER.error("Could not store annotation constructor in map");
        }
    }


    /**
     *
     * Same is {@see ofIndex(Byte)} but uses if-else cascade to instantiate
     *
     * @param index
     * @return
     * @deprecated use {@see ofIndex(Byte)}
     */
    @Deprecated
    public Annotation ofIndexCascade(int index) {


        if( index == CHEMICAL_STRUCTURE.getIndex() ) {
            return new ChemicalStructure();
        } else if( index == ENZYME_CLASSIFICATION.getIndex() ) {
            return new EnzymeClassification();
        } else if( index == FORMULA.getIndex() ) {
            return new MolecularFormula();
        } else if( index == CROSS_REFERENCE.getIndex() ) {
            return new CrossReference();
        } else if( index == AUTHOR_ANNOTATION.getIndex() ) {
            return new AuthorAnnotation();
        } else if( index == CLASSIFICATION.getIndex() ) {
            return new Classification();
        } else if( index == CHEBI_CROSS_REFERENCE.getIndex() ) {
            return new Classification();
        }

        // todo throw error
        return null;

    }


    /**
     *
     * Same is {@see ofIndex(Byte)} but uses reflection to instantiate
     *
     * @param index
     * @return
     * @deprecated
     *
     */
    @Deprecated
    public Annotation ofIndexReflection(int index) {


        try {
            return (Annotation) constructors[index].newInstance();

//                throw new InvalidParameterException("Class of index " + index +
//                                                    " has not been implemented in factory");
//            }

        } catch( Exception ex ) {
            throw new InvalidParameterException("Unable to construct: " + ex.getMessage());
        }

    }


    /**
     *
     * Construct an empty annotation of the given class type. Note there is an overhead off using
     * this method over {@ofIndex(Byte)} as the Byte index is first looked up in the
     * AnnotationLoader. The average speed reduction is 1800 % slower (note this is still only about
     * 1/3 second for 100 000 objects).
     *
     * @param type
     * @return
     */
    public Annotation ofClass(Class type) {
        return ofIndex(AnnotationLoader.getInstance().getIndex(type));
    }


    /**
     *
     * Construct an empty annotation given it's index. It the index returns a null pointer then an
     * InvalidParameterException is thrown informing of the problematic index. The index is given
     * in the uk.ac.ebi.annotation/AnnotationDescription.properties file which in turn is loaded by
     * {@see AnnotationLoader}.
     *
     * @param index
     * @return
     *
     */
    public Annotation ofIndex(int index) {

        Annotation annotation = instances[index];

        if( annotation != null ) {
            return annotation.getInstance();
        }

        throw new InvalidParameterException("Unable to get instance of annotation with index: " +
                                            index);
    }


    public Annotation readExternal(Byte index, ObjectInput in) throws IOException,
                                                                      ClassNotFoundException {
        Annotation ann = ofIndex(index);
        ann.readExternal(in);
        return ann;
    }


    public static void main(String[] args) {

        AnnotationFactory.getInstance();

        long reflectionAvg = 0;
        long instanceAvg = 0;
        long cascadeAvg = 0;

        for( Annotation ann : instances ) {

            if( ann != null ) {

                long cStart = System.currentTimeMillis();
                for( int i = 0 ; i < 1000000 ; i++ ) {
                    Annotation annotation = AnnotationFactory.getInstance().ofIndexCascade(ann.
                      getIndex());
                }
                long cEnd = System.currentTimeMillis();
                System.out.println("time using cascade: " + (cEnd - cStart) + " (ms)");
                cascadeAvg += (cEnd - cStart);
                cascadeAvg /= 2;


                long rStart = System.currentTimeMillis();
                for( int i = 0 ; i < 1000000 ; i++ ) {
                    Annotation annotation = AnnotationFactory.getInstance().ofIndex(ann.getIndex());
                }
                long rEnd = System.currentTimeMillis();
                System.out.println("time using reflections: " + (rEnd - rStart) + " (ms)");

                reflectionAvg += (rEnd - rStart);
                reflectionAvg /= 2;


                long iStart = System.currentTimeMillis();
                for( int i = 0 ; i < 1000000 ; i++ ) {
                    Annotation annotation = AnnotationFactory.getInstance().ofIndex(ann.getIndex());
                }
                long iEnd = System.currentTimeMillis();
                System.out.println("time using instances: " + (iEnd - iStart) + " (ms)");

                instanceAvg += (iEnd - iStart);
                instanceAvg /= 2;

            }

        }

        System.out.println("    cascade mean: " + cascadeAvg + " (ms)");
        System.out.println("reflections mean: " + reflectionAvg + " (ms)");
        System.out.println("  instances mean: " + instanceAvg + " (ms)");

    }


}

