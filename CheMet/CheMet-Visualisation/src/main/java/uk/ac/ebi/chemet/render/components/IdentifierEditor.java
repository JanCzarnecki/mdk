/**
 * CrossreferenceEditor.java
 *
 * 2012.02.03
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
package uk.ac.ebi.chemet.render.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;


/**
 *
 *          CrossreferenceEditor 2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class IdentifierEditor extends JComponent {

    private static final Logger LOGGER = Logger.getLogger(IdentifierEditor.class);

    private JComboBox type;

    private JTextField field;

    private String DEFAULT_TEXT = "enter identifier";

    private static final IdentifierFactory ID_FACTORY = IdentifierFactory.getInstance();

    private Map<String, Byte> idMap = new HashMap<String, Byte>();

    private List<String> idNames;

    private SuggestType suggestion = new SuggestType();

    private Identifier identifier;

    public IdentifierEditor(){
        this(new ArrayList<Class<? extends Identifier>>());
    }

    public IdentifierEditor(Collection<Class<? extends  Identifier>> identifiers) {

        setLayout(new FormLayout("pref, 4dlu, pref", "p"));

        for (Identifier factoryID : ID_FACTORY.getSupportedIdentifiers()) {

            if(identifiers.isEmpty()){
                idMap.put(factoryID.getShortDescription(), factoryID.getIndex());
            }

            ACCEPT:
            for(Class<? extends Identifier> accepted : identifiers){
                if(accepted.isInstance(factoryID)){
                    idMap.put(factoryID.getShortDescription(), factoryID.getIndex());
                    break ACCEPT;
                }
            }

        }
        
        idNames = new ArrayList<String>(idMap.keySet());;

        Collections.sort(idNames);

        field = FieldFactory.newField(14);
        type = ComboBoxFactory.newComboBox(idNames);

        type.setPreferredSize(new Dimension(150, 27));

        type.setBackground(ThemeManager.getInstance().getTheme().getDialogBackground());

        CellConstraints cc = new CellConstraints();

        add(type, cc.xy(1, 1));
        add(field, cc.xy(3, 1));

        field.setText(DEFAULT_TEXT);

        // remove prompt when user clicks on the textfield
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(DEFAULT_TEXT)) {
                    field.setText("");
                }
            }
        });

        turnOnSuggestion();

    }


    /**
     * Turns on identifier suggestion. On every update of the field
     * the combobox model will now change to reflect the content
     * of the field
     */
    public final void turnOnSuggestion() {
        field.getDocument().removeDocumentListener(suggestion);
        field.getDocument().addDocumentListener(suggestion);
    }


    /**
     * Turns off identifier suggestion
     */
    public final void turnOffSuggestion() {
        field.getDocument().removeDocumentListener(suggestion);
    }


    /**
     * Whether the text field has been filled in
     * @return 
     */
    public final boolean isFilled() {
        return !field.getText().isEmpty()
               && !field.getText().trim().equals(DEFAULT_TEXT);
    }


    /**
     * Access an instance of the edited identifier
     * @return 
     */
    public Identifier getIdentifier() {

        String accession = field.getText().trim();
        Byte index = idMap.get((String) type.getSelectedItem());

        // if the idenfier is the same just return the current instance
        if (identifier != null
            && identifier.getAccession().equals(accession)
            && index == identifier.getIndex()) {
            return identifier;
        }

        identifier = ID_FACTORY.ofIndex(index);

        identifier.setAccession(accession);

        return identifier;

    }


    /**
     * Set the identifier to display as editable
     * @param identifier 
     */
    public void setIdentifier(Identifier identifier) {

        this.identifier = identifier;

        field.setText(identifier.getAccession());
        type.setSelectedItem(identifier.getShortDescription());

    }


    /**
     * Private member the does the suggestions.
     */
    private class SuggestType implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            suggest();
        }


        public void insertUpdate(DocumentEvent e) {
            suggest();
        }


        public void removeUpdate(DocumentEvent e) {
            suggest();
        }


        public void suggest() {
            String accession = field.getText().trim();
            DefaultComboBoxModel model = (DefaultComboBoxModel) type.getModel();
            model.removeAllElements();
            for (Identifier id : ID_FACTORY.getMatchingIdentifiers(accession)) {
                model.addElement(id.getShortDescription());
            }
            if (model.getSize() == 0) {
                for (String id : idNames) {
                    model.addElement(id);
                }
            }
            type.repaint();
        }
    }
}
