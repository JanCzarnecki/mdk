/**
 * BasicAnnotationCellRenderer.java
 *
 * 2011.09.29
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
package uk.ac.ebi.mnb.renderers;

import com.google.common.base.Joiner;
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import javax.swing.JTable;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.visualisation.ViewUtils;

/**
 *          AnnotationCellRenderer – 2011.09.29 <br>
 *          A simple annotation cell renderer that joins the toString() values 
 *          of a collection of annotations using a comma and space ', '.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationCellRenderer extends DefaultRenderer {

    private boolean html = false;
    private String token = ", ";

    public AnnotationCellRenderer() {
    }

    public AnnotationCellRenderer(boolean html, String token) {
        this.html = html;
        this.token = token;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        Collection collection = (Collection) value;
        String text = Joiner.on(token).join(collection);

        this.setText(html ? ViewUtils.htmlWrapper(text) : text);

        this.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());


        return this;

    }
}
