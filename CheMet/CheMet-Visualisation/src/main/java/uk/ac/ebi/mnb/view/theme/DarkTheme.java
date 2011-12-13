/**
 * DefaultTheme.java
 *
 * 2011.09.30
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
package uk.ac.ebi.mnb.view.theme;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Font;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Theme;
import uk.ac.ebi.chemet.render.ViewUtilities;

/**
 *          DefaultTheme – 2011.09.30 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class DarkTheme implements Theme {

    private static final Logger LOGGER = Logger.getLogger(DarkTheme.class);
    private Color bg = new Color(64, 64, 64);

    public Color getWarningForeground() {
        return Color.YELLOW;
    }

    public Color getForeground() {
        return Color.WHITE;
    }

    public Color getBackground() {
        return bg;
    }

    public Color getAltForeground() {
        return Color.DARK_GRAY;
    }

    public Color getDialogBackground() {
        return ViewUtilities.BACKGROUND;
    }

    public Font getBodyFont() {
        return ViewUtilities.DEFAULT_BODY_FONT;
    }

    public Font getHeaderFont() {
        return ViewUtilities.DEFAULT_HEADER_FONT;
    }

    public float getDialogOpacity() {
        return 0.95f;
    }

    public Font getLinkFont() {
        return ViewUtilities.DEFAULT_LINK_FONT;
    }

    public Color getEmphasisedForeground() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Icon getPlusIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Icon getMinusIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
    
    
}
