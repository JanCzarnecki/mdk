/**
 * BasicReactionIdentifier.java
 *
 * 2011.09.26
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
package uk.ac.ebi.resource.reaction;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IncrementalPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.resource.ResourcePreferences;

/**
 *          BasicReactionIdentifier – 2011.09.26 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BasicReactionIdentifier extends ReactionIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicReactionIdentifier.class);
    private static long ticker = Preferences.userNodeForPackage(BasicReactionIdentifier.class).getLong("ticker", 0);

    public BasicReactionIdentifier() {
    }

    public BasicReactionIdentifier(String accession) {
        super(accession);
    }

    @Override
    public void setAccession(String accession) {
        super.setAccession(accession);
    }

    public BasicReactionIdentifier newInstance() {
        return new BasicReactionIdentifier();
    }

    public static BasicReactionIdentifier nextIdentifier() {
        
        StringPreference format = ResourcePreferences.getInstance().getPreference("BASIC_RXN_ID_FORMAT");
        IncrementalPreference ticker = ResourcePreferences.getInstance().getPreference("BASIC_RXN_ID_TICK");
        
        return new BasicReactionIdentifier(String.format(format.get(), ticker.get()));
        
    }
}
