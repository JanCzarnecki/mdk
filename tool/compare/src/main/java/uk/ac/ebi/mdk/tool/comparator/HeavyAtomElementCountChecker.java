/*
 * Copyright (c) 2013. Pablo Moreno
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
package uk.ac.ebi.mdk.tool.comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * Checks heavy element (non H) counts between the two molecules are the same.
 * @author  pmoreno
 */
public final class HeavyAtomElementCountChecker {

    private static final HeavyAtomCountComparator countComp = new HeavyAtomCountComparator();

    /**
     * Given two molecules, returns true if each heavy element count is equal.
     * 
     * @param molA
     * @param molB
     * @return true if equal individual heavy element counts. 
     */
    public boolean equals(IAtomContainer molA, IAtomContainer molB) {
        IMolecularFormula formA = MolecularFormulaManipulator.getMolecularFormula(molA);
        IMolecularFormula formB = MolecularFormulaManipulator.getMolecularFormula(molB);
        Map<String,Integer> hsymbolsCountA = getHeavySymbolsCount(formA);
        Map<String,Integer> hsymbolsCountB = getHeavySymbolsCount(formB);
        
        if(!(hsymbolsCountA.keySet().containsAll(hsymbolsCountB.keySet()) && hsymbolsCountB.keySet().containsAll(hsymbolsCountA.keySet())))
            return false;
        
        for (String symbA : hsymbolsCountA.keySet()) {
            Integer countElementA = hsymbolsCountA.get(symbA);
            Integer countElementB = hsymbolsCountB.get(symbA);
            
            if(!countElementA.equals(countElementB))
                return false;
        }
        return true;
    }

    private Map<String,Integer> getHeavySymbolsCount(IMolecularFormula formA) {
        Map<String,Integer> symbolsA = new HashMap<String,Integer>();
        for (IElement iElement : MolecularFormulaManipulator.getHeavyElements(formA)) {
            Integer count = MolecularFormulaManipulator.getElementCount(formA, iElement);
            symbolsA.put(iElement.getSymbol(), count);
        }
        return symbolsA;
    }
}
