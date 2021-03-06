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

package uk.ac.ebi.mdk.ui.render.alignment;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;

/**
 * BlastScoreColorRange.java
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @date May 5, 2011
 */
public enum BlastScoreColorRange {

    VERY_LOW( 0 , 40 , new Color( Integer.parseInt( "111111" , 16 ) ) ),
    LOW( 40 , 50 , new Color( Integer.parseInt( "3311FF" , 16 ) ) ),
    MEDIUM( 50 , 80 , new Color( Integer.parseInt( "33FF11" , 16 ) ) ),
    HIGH( 80 , 200 , new Color( Integer.parseInt( "FF11FF" , 16 ) ) ),
    VERY_HIGH( 200 , Integer.MAX_VALUE , new Color( Integer.parseInt( "FF1133" , 16 ) ) ),
    UNKNOWN( -1, -1 , Color.WHITE );
    private Integer min;
    private Integer max;
    private Color color;

    private BlastScoreColorRange( Integer min , Integer max , Color color ) {
        this.min = min;
        this.max = max;
        this.color = color;
    }

    /**
     * Returns the required object given a bit score
     * @param bitScore
     * @return
     */
    public static BlastScoreColorRange getColorForScore( int bitScore ) {
        for ( BlastScoreColorRange blastScoreColor : values() ) {
            if ( blastScoreColor.isInRange( bitScore ) ) {
                return blastScoreColor;
            }
        }
        return UNKNOWN;
    }

    /**
     * Set the color of the
     * @param color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the max value
     * @return
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Returns the min value
     * @return
     */
    public Integer getMin() {
        return min;
    }

    /**
     * Tests whether the provided bit score is within range
     * of this objects values
     * @param bitScore
     * @return T/F whether the bitscore is in the range of the object min/max limits
     */
    public boolean isInRange( Integer bitScore ) {
        if ( min <= bitScore && max > bitScore ) {
            return true;
        }
        return false;
    }
}
