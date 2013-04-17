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

package uk.ac.ebi.mdk.ui.edit.reaction;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DialogCompartmentResolver implements CompartmentResolver {

    private static final Logger LOGGER = Logger.getLogger(DialogCompartmentResolver.class);

    private CompartmentResolver resolver;
    private Window              window;
    private boolean             okayClicked;

    public DialogCompartmentResolver(CompartmentResolver parent,
                                     Window window) {
        this.resolver = parent;
        this.window = window;
    }

    @Override
    public Compartment getCompartment(String compartment) {
        return resolver.isAmbiguous(compartment) ? showDialog(compartment) : resolver.getCompartment(compartment);
    }

    public Compartment showDialog(String compartment) {

        final JDialog dialog = new JDialog(window, Dialog.ModalityType.APPLICATION_MODAL);


        List<Compartment> compartmentList = new ArrayList<Compartment>(getCompartments());
        Collections.sort(compartmentList, new Comparator<Compartment>() {
            @Override
            public int compare(Compartment o1, Compartment o2) {
                return o1.getAbbreviation().compareTo(o2.getAbbreviation());
            }
        });
        JComboBox comboBox = ComboBoxFactory.newComboBox(compartmentList);

        final Box box = Box.createHorizontalBox();
        final JLabel label = LabelFactory.newLabel("");
        final JLabel term  = LabelFactory.newFormLabel("");
        box.add(label);
        box.add(Box.createHorizontalGlue());
        box.add(term);
        comboBox.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Compartment compartment = (Compartment) value;

                label.setText(compartment.getAbbreviation() + ": " + compartment.getDescription());
                label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                label.setForeground(list.getForeground());

                String accession = compartment.getIdentifier().getAccession();
                term.setText(accession.isEmpty() ? "" : "[" + accession + "]");

                return box;
            }

        });
        dialog.setUndecorated(true);

        CellConstraints cc = new CellConstraints();
        JPanel panel = PanelFactory.create();
        panel.setLayout(new FormLayout("p, 4dlu, p, 4dlu, p",
                                       "p, 4dlu, p"));
        panel.setBorder(Borders.DLU4_BORDER);
        panel.add(LabelFactory.newLabel("Please select the correct compartment for the given notation"),
                                        cc.xyw(1, 1, 5));
        panel.add(LabelFactory.newFormLabel("<html>" + compartment + " <i>is equivalent to</i></html>"),
                  cc.xy(1, 3));
        panel.add(comboBox,
                  cc.xy(3, 3));
        okayClicked = false;
        panel.add(ButtonFactory.newButton(new AbstractAction("Okay") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                okayClicked = true;
            }
        }),
                  cc.xy(5, 3));
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);

        return okayClicked ? (Compartment) comboBox.getSelectedItem() : null;

    }

    /**
     * Delegates to the parent resolver
     *
     * @param compartment
     *
     * @return
     */
    @Override
    public List<Compartment> getCompartments(String compartment) {
        return resolver.getCompartments(compartment);
    }

    @Override
    public boolean isAmbiguous(String compartment) {
        return resolver.isAmbiguous(compartment);
    }

    @Override
    public Collection<Compartment> getCompartments() {
        return new HashSet<Compartment>(resolver.getCompartments());
    }
}
