/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.resource.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.interfaces.Resource;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;

import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * MIRIAMLoader.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class MIRIAMLoader {

    private static final org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(
                    MIRIAMLoader.class);

    private Map<String, MIRIAMEntry> nameEntryMap = new HashMap<String, MIRIAMEntry>(50);

    private Map<String, MIRIAMEntry> urnEntryMap = new HashMap<String, MIRIAMEntry>(50);

    private Map<Resource, Identifier> resources = new HashMap<Resource, Identifier>(50);

    private Map<Integer, MIRIAMEntry> mirMap = new HashMap<Integer, MIRIAMEntry>(500);


    /**
     * Singleton Accessor
     */
    public static MIRIAMLoader getInstance() {
        return MIRIAMResourcesHolder.INSTANCE;
    }


    private static class MIRIAMResourcesHolder {

        private static final MIRIAMLoader INSTANCE = new MIRIAMLoader();
    }


    private MIRIAMLoader() {
        long start = System.currentTimeMillis();
        load();
        long end = System.currentTimeMillis();
        logger.info("Loaded miriam xml in " + (end - start) + " (ms)");
    }


    private void load() {

        InputStream stream = getClass().getResourceAsStream("miriam_resources.xml");

        if (stream == null) {
            logger.info("Unable to get stream for miriam.xml");
        }

        Document xmlDocument = XMLHelper.buildDocument(stream);

        if (xmlDocument == null) {
            return;
        }

        // default entry '0'
        mirMap.put(0, new MIRIAMEntry("MIR:00000000",
                                      ".+",
                                      "N/A",
                                      "None MIRIAM Entry",
                                      "",
                                      "http://www.google.com/search?q=$id",
                                      new ArrayList(), false));

        Node datatypeNode = xmlDocument.getLastChild().getFirstChild();

        while (datatypeNode != null) {
            if (datatypeNode.getNodeName().equals("datatype")) {
                Node datatypeChild = datatypeNode.getFirstChild();

                String name = null,
                        urn = null,
                        definition = null, url = null;

                String id = datatypeNode.getAttributes().getNamedItem("id").getNodeValue();
                int mir = Integer.parseInt(id.substring(4));
                String pattern = datatypeNode.getAttributes().getNamedItem("pattern").getNodeValue();
                List<String> synonyms = new ArrayList();

                while (datatypeChild != null) {
                    if (datatypeChild.getNodeName().equals("name")) {
                        name = datatypeChild.getTextContent();
                    } else if (datatypeChild.getNodeName().equals("definition")) {
                        definition = datatypeChild.getTextContent();
                    } else if (datatypeChild.getNodeName().equals("uris")) {
                        urn = datatypeChild.getChildNodes().item(1).getTextContent();
                    } else if (datatypeChild.getNodeName().equals("resources")) {
                        url = getURL(datatypeChild.getChildNodes().item(1));
                    } else if (datatypeChild.getNodeName().equals("synonyms")) {
                        NodeList synonymNodes = datatypeChild.getChildNodes();
                        for (int i = 0; i < synonymNodes.getLength(); i++) {
                            String synonym = synonymNodes.item(i).getTextContent().trim();
                            if (synonym.isEmpty() == Boolean.FALSE) {
                                synonyms.add(synonym);
                            }
                        }
                    }
                    datatypeChild = datatypeChild.getNextSibling();
                }

                // add to the map
                MIRIAMEntry entry = new MIRIAMEntry(id, pattern, name, definition, urn, url, synonyms, true);
                mirMap.put(mir, entry);
                nameEntryMap.put(name.toLowerCase(),
                                 entry);
                urnEntryMap.put(entry.getBaseURN(), entry);
            }
            datatypeNode = datatypeNode.getNextSibling();
        }


    }


    private String getURL(Node node) {
        if (node.getNodeName().equals("resource") == false) {
            return "";
        }
        return node.getChildNodes().item(5).getTextContent();
    }


    /**
     * Access a MIRIAM resource entry by it's name, such as, 'chebi'.
     *
     * @param name A Lower case name of resource with any space characters included, 'kegg compound
     *
     * @return The MIRIAM entry associated with that name
     */
    public MIRIAMEntry getEntry(String name) {
        if (nameEntryMap.containsKey(name)) {
            return nameEntryMap.get(name);
        }
        logger.error("No MIRIAM entry found for name '" + name + "'");
        return null;
    }


    public MIRIAMEntry getEntry(int mir) {
        if (!mirMap.containsKey(mir)) {
            throw new InvalidParameterException("No MIRIAM entry for mir:" + mir);
        }
        return mirMap.get(mir);
    }


    /**
     * Converts a provided URN into a string identifier
     *
     * @param urn such as urn:miriam:obo.chebi:CHEBI%3A17196
     *
     * @return the identifier i.e. "CHEBI:17196" in the above example
     */
    public static String getAccession(String urn) {
        return urn.substring(urn.lastIndexOf(":") + 1).replace("%3A", ":");
    }


    public Identifier getIdentifier(MIRIAMEntry e) {
        if (resources.containsKey(e)) {
            return resources.get(e).newInstance();
        } else {
            logger.error("No entry found for resource: " + e.getId());
            return null;
        }
    }

    public Identifier getIdentifier(String urn) {

        String prefix = urn.substring(0, urn.lastIndexOf(":"));

        // build the map if it's empty
        if (resources.isEmpty()) {
            for (Identifier id : IdentifierFactory.getInstance().getSupportedIdentifiers()) {
                resources.put(id.getResource(), id);
            }
        }

        if (urnEntryMap.containsKey(prefix)) {
            Identifier id = getIdentifier(urnEntryMap.get(prefix));
            if(id != null){
                id.setAccession(getAccession(urn));
            }
            return id;

        } else {
            logger.error("No entry for " + prefix);
            return null;
        }



    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        MIRIAMLoader.getInstance();
        long end = System.currentTimeMillis();
        System.out.println("Time:" + (end - start) + " (ms)");
    }
}
