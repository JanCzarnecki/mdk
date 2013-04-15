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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.prototype.hash;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.util.ConnectionMatrixFactory;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author johnmay
 */
public class ConnectionMatrixFactoryTest {

    public ConnectionMatrixFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void testGetBondElectronMatrix() {
        ConnectionMatrixFactory factory = ConnectionMatrixFactory.getInstance();

        IAtomContainer formaldehyde = TestMoleculeFactory.formaldehyde();

        byte[][] actual = factory.getBondElectronMatrix(formaldehyde);

        System.out.printf("%3s", "-");
        for (int i = 0; i < actual.length; i++) {
            System.out.printf(" %3s ", formaldehyde.getAtom(i).getSymbol());
        }
        System.out.println("");




        for (int i = 0; i < actual.length; i++) {
            System.out.printf("%3s", formaldehyde.getAtom(i).getSymbol());
            for (int j = 0; j < actual[i].length; j++) {
                System.out.printf(" %3s ", actual[i][j]);
            }
            System.out.println("");
        }

        byte[][] expected = new byte[][]{
            {0, 1, 2, 1},
            {1, 0, 0, 0},
            {2, 0, 4, 0},
            {1, 0, 0, 0},};

        assertArrayEquals(expected, actual);
    }
}
