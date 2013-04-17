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

package uk.ac.ebi.mdk.service.index.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

import java.io.File;

/**
 * KEGGCompoundDataIndex - 28.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundDataIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundDataIndex.class);

    public KEGGCompoundDataIndex(){
        super("KEGG Compound Data", "data/kegg-compound");
    }

    public KEGGCompoundDataIndex(File f){
        super("KEGG Compound Data", f);
    }


}
