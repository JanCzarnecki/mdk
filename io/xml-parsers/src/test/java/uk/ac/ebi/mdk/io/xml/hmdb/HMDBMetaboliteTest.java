/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.io.xml.hmdb;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * unit tests for container simply ensure that fields are non-null by default
 *
 * @author John May
 */
public class HMDBMetaboliteTest {

    private static final HMDBMetabolite EMPTY = new HMDBMetabolite();

    @Test
    public void testGetAccession() throws Exception {
        assertNotNull(EMPTY.getAccession());
    }

    @Test
    public void testGetSecondaryAccessions() throws Exception {
        assertNotNull(EMPTY.getSecondaryAccessions());
    }


    @Test
    public void testGetCommonName() throws Exception {
        assertNotNull(EMPTY.getCommonName());
    }


    @Test
    public void testGetIUPACName() throws Exception {
        assertNotNull(EMPTY.getIUPACName());
    }


    @Test
    public void testGetSynonyms() throws Exception {
        assertNotNull(EMPTY.getSynonyms());
    }


    @Test
    public void testGetFormalCharge() throws Exception {
        assertNotNull(EMPTY.getFormalCharge());
    }

    @Test
    public void testGetMolecularFormula() throws Exception {
        assertNotNull(EMPTY.getMolecularFormula());
    }


    @Test
    public void testGetCrossReferences() throws Exception {
        assertNotNull(EMPTY.getCrossReferences());
    }

    @Test
    public void testGetInChI() throws Exception {
        assertNotNull(EMPTY.getInChI());
    }


    @Test
    public void testGetSMILES() throws Exception {
        assertNotNull(EMPTY.getSMILES());

    }

}
