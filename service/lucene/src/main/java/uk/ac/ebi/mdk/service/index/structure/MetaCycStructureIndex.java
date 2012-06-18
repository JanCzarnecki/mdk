/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.service.index.structure;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

import java.io.File;

/**
 * @author John May
 */
public class MetaCycStructureIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(MetaCycStructureIndex.class);

    public MetaCycStructureIndex() {
        super("MetaCyc Structure", "structure/metacyc");
    }

    public MetaCycStructureIndex(File file) {
        super("MetaCyc Structure", file);
    }
}
