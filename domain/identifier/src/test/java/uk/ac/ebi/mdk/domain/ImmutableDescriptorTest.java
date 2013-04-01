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

package uk.ac.ebi.mdk.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author John May
 */
public class ImmutableDescriptorTest {

    @Test(expected = NullPointerException.class) public void testNullName(){
        new ImmutableDescriptor(null, "b");
    }

    @Test(expected = NullPointerException.class) public void testNullDescription(){
        new ImmutableDescriptor("a", null);
    }

    @Test public void testName() throws Exception {
        Assert.assertThat(new ImmutableDescriptor("a", "b").name(), is("a"));
    }

    @Test public void testDescription() throws Exception {
        Assert.assertThat(new ImmutableDescriptor("a", "b").description(), is("b"));
    }
}
