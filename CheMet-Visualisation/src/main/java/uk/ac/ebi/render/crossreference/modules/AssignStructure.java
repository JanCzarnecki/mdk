/**
 * GoogleSearch.java
 *
 * 2012.01.13
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.render.crossreference.modules;

import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.security.InvalidParameterException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jniinchi.INCHI_RET;
import org.apache.log4j.Logger;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV3000Reader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.ui.tool.annotation.CrossreferenceModule;


/**
 *
 * GoogleSearch 2012.01.13
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
public class AssignStructure
        implements CrossreferenceModule {

    private static final Logger LOGGER = Logger.getLogger(AssignStructure.class);

    private final JPanel component;

    private final JComboBox format;

    private final JTextArea area;

    private final JButton browse;

    private Metabolite metabolite;

    private String defaultText = "Paste InCHI, SMILES or Mol file here";


    public AssignStructure() {

        component = PanelFactory.createDialogPanel("p, p:grow, min", "p, 4dlu, p");

        format = new JComboBox(new String[]{"InChI", "Mol (v2000)", "CML", "Mol (v3000)", "SMILES"});
        browse = ButtonFactory.newButton("Browse", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int choice = chooser.showOpenDialog(component);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        FileReader reader = new FileReader(file);
                        char[] chars = new char[(int) file.length()];
                        reader.read(chars);
                        area.setText(new String(chars));
                        reader.close();
                    } catch (Exception ex) {
                        area.setText("Unreadable file");
                    }
                }
            }
        });

        area = new JTextArea(10, 10);
        area.setFont(new Font("Courier New", Font.PLAIN, 11));
        area.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(defaultText)) {
                    area.setText("");
                }
            }
        });
        area.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                String text = area.getText().toLowerCase();
                if (text.contains("inchi")) {
                    format.setSelectedItem("InChI");
                } else if (text.contains("v2000")) {
                    format.setSelectedItem("Mol (v2000)");
                } else if (text.contains("cml")) {
                    format.setSelectedItem("CML");
                } else if (text.contains("v3000")) {
                    format.setSelectedItem("Mol (v3000)");
                } else {
                    format.setSelectedItem("SMILES");
                }
            }


            public void removeUpdate(DocumentEvent e) {
            }


            public void changedUpdate(DocumentEvent e) {
            }
        });

        CellConstraints cc = new CellConstraints();

        component.add(format,
                      cc.xy(1, 1, cc.LEFT, cc.CENTER));
        component.add(browse,
                      cc.xy(3, 1, cc.RIGHT, cc.CENTER));
        component.add(new JScrollPane(area),
                      cc.xyw(1, 3, 3));

    }


    public String getDescription() {
        return "Assign Structure";
    }


    public JComponent getComponent() {
        return component;
    }


    public void setup(Metabolite metabolite) {
        area.setText(defaultText);
        this.metabolite = metabolite;
    }


    public boolean canTransferAnnotations() {
        return true;
    }


    public void transferAnnotations() throws Exception {

        String formatName = (String) this.format.getSelectedItem();

        // parse the structure
        if (formatName.equals("Mol (v2000)")) {
            transferMDLV2000();
        } else if (formatName.equals("InChI")) {
            transferInChi();
        } else if (formatName.equals("SMILES")) {
            // todo
        } else if (formatName.equals("Mol (v3000)")) {
            transferMDLV3000();
        } else if (formatName.equals("CML")) {
            transferCML();
        }
    }


    public void transferCML() throws CDKException, UnsupportedEncodingException, IOException {
        String cmltext = area.getText();
        CMLReader cmlreader = new CMLReader(new ByteArrayInputStream(cmltext.getBytes("UTF-8")));
        IChemFile chemfile = cmlreader.read(DefaultChemObjectBuilder.getInstance().newInstance(ChemFile.class));
        cmlreader.close();
        metabolite.addAnnotation(new AtomContainerAnnotation(ChemFileManipulator.getAllAtomContainers(chemfile).get(0)));
    }


    public void transferMDLV2000() throws IOException, CDKException {
        MDLV2000Reader reader = new MDLV2000Reader(new StringReader(area.getText()));
        IMolecule molecule = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));
        reader.close();
        metabolite.addAnnotation(new AtomContainerAnnotation(molecule));
    }


    public void transferMDLV3000() throws IOException, CDKException {
        MDLV3000Reader reader = new MDLV3000Reader(new StringReader(area.getText()));
        IMolecule molecule = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));
        reader.close();
        metabolite.addAnnotation(new AtomContainerAnnotation(molecule));
    }


    public void transferInChi() throws CDKException {
        String inchi = area.getText().replaceAll("\n", "").trim();
        if (inchi.isEmpty()) {
            return;
        }
        InChIGeneratorFactory inchifactory = InChIGeneratorFactory.getInstance();
        InChIToStructure structureGenerator = inchifactory.getInChIToStructure(inchi, DefaultChemObjectBuilder.getInstance());
        INCHI_RET status = structureGenerator.getReturnStatus();
        if (status != INCHI_RET.OKAY) {
            throw new InvalidParameterException("Unable to parse InCHI for " + metabolite.getName() + ": " + structureGenerator.getMessage());
        }
        metabolite.addAnnotation(new AtomContainerAnnotation(structureGenerator.getAtomContainer()));
    }
}
