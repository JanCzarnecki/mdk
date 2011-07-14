/**
 * DrawAlignmentsMain.java
 *
 * 2011.07.14
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
package uk.ac.ebi.execs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.visualisation.AlignmentRendererFactory;
import uk.ac.ebi.chemet.visualisation.BasicAlignmentColor;
import uk.ac.ebi.chemet.visualisation.LocalAlignmentRenderer;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.LocalAlignment;
import uk.ac.ebi.metabolomes.execs.CommandLineMain;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;

/**
 * @name    DrawAlignmentsMain
 * @date    2011.07.14
 * @date    $LastChangedDate$ (this version)
 * @version $Revision$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class DrawAlignmentsMain
        extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger( DrawAlignmentsMain.class );

    public static void main( String[] args ) {
        new DrawAlignmentsMain( args ).process();
    }

    public DrawAlignmentsMain( String[] args ) {
        super( args );
    }

    @Override
    public void setupOptions() {
        add( new Option( "x" , "xml" , true , "BLAST input file (produced by BLASTP -m 7 options)" ) );
    }

    @Override
    public void process() {

        File xmlFile = getCmd().hasOption( "x" ) ? new File( getCmd().getOptionValue( "x" ) ) : null;

        if ( xmlFile == null ) {

            LOGGER.error( "No XML file provided" );
            printHelp();
        }

        BlastXML blastIO = new BlastXML( xmlFile );
        GeneProductCollection productCollection =
                              blastIO.loadProteinHomologyObservations( new GeneProductCollection() ,
                                                                       new JobParameters() );

        // build the renderer, could put in the factory
        LocalAlignmentRenderer renderer =
                               new LocalAlignmentRenderer( new Rectangle() ,
                                                           new BasicAlignmentColor( Color.RED ,
                                                                                    Color.lightGray ) ,
                                                           0 );



        for ( GeneProteinProduct geneProteinProduct : productCollection.getProteinProducts() ) {

            BufferedImage img = new BufferedImage( 800 , 8 * geneProteinProduct.getObservations().getBlastHits().size() ,
                                                   BufferedImage.TYPE_4BYTE_ABGR );
            Graphics2D g2 = ( Graphics2D ) img.getGraphics();
            Rectangle outerBounds = new Rectangle( 800 , 8 );
            Rectangle innerBounds = new Rectangle( 2 , 2 , 796 , 4 );

            for ( LocalAlignment hit : geneProteinProduct.getObservations().getBlastHits() ) {
                renderer.render( hit , g2 , outerBounds , innerBounds );
                outerBounds.y += outerBounds.height;
                innerBounds.y += outerBounds.height;
            }

            g2.dispose();
            try {
                ImageIO.write( img , "png" , new File( "/Users/johnmay/Desktop/test.png" ) );
                return;
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }


            return; // ony do one
        }



    }
}
