/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.DefaultLoader;

import java.io.IOException;
import java.io.ObjectInput;


/**
 * MolecularFormula – 2011.09.14 <br>
 * Annotation of molecular formula
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
@Context(Metabolite.class)
@Brief("Molecular Formula")
@Description("The chemical formula of a metabolite")
public class MolecularFormula
        extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(MolecularFormula.class);

    private IMolecularFormula formula;

    private String html; // speeds up rendering

    private static MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            MolecularFormula.class);


    /**
     * Default constructor need for externalization
     */
    public MolecularFormula() {
    }


    /**
     * Constructs a formula annotation with a provided {@see MolecularFormula} from CDK library
     *
     * @param formula
     */
    public MolecularFormula(IMolecularFormula formula) {
        this.formula = formula;
        super.setValue(MolecularFormulaManipulator.getString(formula));
        if (formula != null) {
            this.html = MolecularFormulaManipulator.getHTML(formula);
        }
    }


    /**
     * Construct a MolecularFormula annotation from a string
     *
     * @param formula
     */
    public MolecularFormula(String formula) {
        super(formula);
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        this.formula = MolecularFormulaManipulator.getMolecularFormula(formula, builder);
        if (this.formula != null) {
            this.html = MolecularFormulaManipulator.getHTML(this.formula);
        }

    }


    /**
     * Accessor to the underlying formula object
     *
     * @return An instance of IMolecularFormula
     */
    public IMolecularFormula getFormula() {
        return formula;
    }


    public void setFormula(IMolecularFormula formula) {
        this.formula = formula;
        super.setValue(MolecularFormulaManipulator.getString(formula));
    }


    public void setFormula(String formula) {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        super.setValue(formula);
        this.formula = MolecularFormulaManipulator.getMolecularFormula(formula, builder);
        if (this.formula != null) {
            this.html = MolecularFormulaManipulator.getHTML(this.formula);
        }
    }


    @Override
    public void setValue(String value) {
        this.setFormula(value);
    }


    /**
     * Returns HTML formula
     *
     * @return
     */
    public String toHTML() {
        return html;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MolecularFormula newInstance() {
        return new MolecularFormula();
    }


    public MolecularFormula getInstance(String formula) {
        return new MolecularFormula(formula);
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        this.formula = MolecularFormulaManipulator.getMolecularFormula(getValue(), builder);
        this.html = MolecularFormulaManipulator.getHTML(formula);
    }
}
