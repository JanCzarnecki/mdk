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

package uk.ac.ebi.mdk.ui.edit.annotation;

import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.mdk.domain.annotation.Annotation;

import javax.swing.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class BasicFieldEditor<A extends Annotation>
        extends AbstractAnnotationEditor<A> {

    private JTextField field = FieldFactory.newField(20);

    public BasicFieldEditor() {
        field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ThemeManager.getInstance().getTheme().getForeground()));
    }

    public JTextField getField() {
        return field;
    }

    @Override
    public JComponent getComponent() {
        return field;
    }
}
