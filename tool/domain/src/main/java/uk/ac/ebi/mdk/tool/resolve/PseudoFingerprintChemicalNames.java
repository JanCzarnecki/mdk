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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 * Implements string finger print as explained in Google refine,
 * but adapted for chemical entities names: 
 * remove leading and trailing whitespace
 * change all characters to their lowercase representation
 * remove all punctuation and control characters
 * split the string into whitespace-separated tokens
 * sort the tokens and keep duplicates
 * join the tokens back together
 * normalize extended western characters to their ASCII representation (for example "gödel" → "godel")
 * 
 * We should also singularize words.
 * 
 * TODO Move this to be compliant with the FingerprintEncoder class.
 *
 * @author pmoreno
 */
public class PseudoFingerprintChemicalNames implements Comparator<String> {
   
    private static final Pattern alphanum = Pattern.compile("\\p{Punct}|\\p{Cntrl}");
    private static final Pattern plural = Pattern.compile("s$");
    private static final Pattern definedHtmlTags = Pattern.compile("</{0,1}(i|sub|sup)>");
    private static final Pattern digit = Pattern.compile("\\d");
    private static final Pattern a_genericBeginning = Pattern.compile("^an{0,1} ");
    
    
    public String key(String s) {
        s = s.trim(); // first off, remove whitespace around the string
        s = s.toLowerCase(); // then lowercase it
        s = a_genericBeginning.matcher(s).replaceFirst("");
        s = plural.matcher(s).replaceAll("");
        s = definedHtmlTags.matcher(s).replaceAll(""); // replace <i> </i>, <sub> </sub>, <sup> </sup>
        s = alphanum.matcher(s).replaceAll(""); // then remove all punctuation and control chars
        s = s.replaceAll("-", ""); // removes - 
        s = s.replaceAll(" ", ""); // removes whitespace
        //String[] frags = StringUtils.split(s); // split by whitespace
        char[] chars = s.toCharArray();
        List<String> listOfTokens = new ArrayList<String>();
        List<String> numericTokens = new ArrayList<String>();
        /*for (String frag : frags) {
            if(digit.matcher(frag).find())
                numericTokens.add(frag);
            else listOfTokens.add(frag);
        }*/
        for (char letter : chars) {
            String character = Character.toString(letter);
            if(digit.matcher(character).find())
                numericTokens.add(character);
            else listOfTokens.add(character);
        }
        Collections.sort(listOfTokens); 
        /*
         * Sort them. Probably we should have an exception here for numbers,
         * so that they are left in the same order they appeared in the name.
         * Originally the refine implementation (for 'normal words') makes a 
         * unique set of characters and sort them. I thought that for chemical 
         * names it would be useful to keep the number of appearances.
         * 
         */
        StringBuilder b = new StringBuilder();
        for (String number : numericTokens) {
            b.append(number);
            //b.append(' ');
        }
        for (String token : listOfTokens) {
            b.append(token);
            //b.append(' ');
        }
        return asciify(b.toString()); // find ASCII equivalent to characters 
    }
    
    protected String asciify(String s) {
        char[] c = s.toCharArray();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            b.append(translate(c[i]));
        }
        return b.toString();
    }
    
    /**
     * Translate the given unicode char in the closest ASCII representation
     * NOTE: this function deals only with latin-1 supplement and latin-1 extended code charts
     */
    private char translate(char c) {
        switch(c) {
            case '\u00C0':
            case '\u00C1':
            case '\u00C2':
            case '\u00C3':
            case '\u00C4':
            case '\u00C5':
            case '\u00E0':
            case '\u00E1':
            case '\u00E2':
            case '\u00E3':
            case '\u00E4':
            case '\u00E5':
            case '\u0100':
            case '\u0101':
            case '\u0102':
            case '\u0103':
            case '\u0104':
            case '\u0105':
                return 'a';
            case '\u00C7':
            case '\u00E7':
            case '\u0106':
            case '\u0107':
            case '\u0108':
            case '\u0109':
            case '\u010A':
            case '\u010B':
            case '\u010C':
            case '\u010D':
                return 'c';
            case '\u00D0':
            case '\u00F0':
            case '\u010E':
            case '\u010F':
            case '\u0110':
            case '\u0111':
                return 'd';
            case '\u00C8':
            case '\u00C9':
            case '\u00CA':
            case '\u00CB':
            case '\u00E8':
            case '\u00E9':
            case '\u00EA':
            case '\u00EB':
            case '\u0112':
            case '\u0113':
            case '\u0114':
            case '\u0115':
            case '\u0116':
            case '\u0117':
            case '\u0118':
            case '\u0119':
            case '\u011A':
            case '\u011B':
                return 'e';
            case '\u011C':
            case '\u011D':
            case '\u011E':
            case '\u011F':
            case '\u0120':
            case '\u0121':
            case '\u0122':
            case '\u0123':
                return 'g';
            case '\u0124':
            case '\u0125':
            case '\u0126':
            case '\u0127':
                return 'h';
            case '\u00CC':
            case '\u00CD':
            case '\u00CE':
            case '\u00CF':
            case '\u00EC':
            case '\u00ED':
            case '\u00EE':
            case '\u00EF':
            case '\u0128':
            case '\u0129':
            case '\u012A':
            case '\u012B':
            case '\u012C':
            case '\u012D':
            case '\u012E':
            case '\u012F':
            case '\u0130':
            case '\u0131':
                return 'i';
            case '\u0134':
            case '\u0135':
                return 'j';
            case '\u0136':
            case '\u0137':
            case '\u0138':
                return 'k';
            case '\u0139':
            case '\u013A':
            case '\u013B':
            case '\u013C':
            case '\u013D':
            case '\u013E':
            case '\u013F':
            case '\u0140':
            case '\u0141':
            case '\u0142':
                return 'l';
            case '\u00D1':
            case '\u00F1':
            case '\u0143':
            case '\u0144':
            case '\u0145':
            case '\u0146':
            case '\u0147':
            case '\u0148':
            case '\u0149':
            case '\u014A':
            case '\u014B':
                return 'n';
            case '\u00D2':
            case '\u00D3':
            case '\u00D4':
            case '\u00D5':
            case '\u00D6':
            case '\u00D8':
            case '\u00F2':
            case '\u00F3':
            case '\u00F4':
            case '\u00F5':
            case '\u00F6':
            case '\u00F8':
            case '\u014C':
            case '\u014D':
            case '\u014E':
            case '\u014F':
            case '\u0150':
            case '\u0151':
                return 'o';
            case '\u0154':
            case '\u0155':
            case '\u0156':
            case '\u0157':
            case '\u0158':
            case '\u0159':
                return 'r';
            case '\u015A':
            case '\u015B':
            case '\u015C':
            case '\u015D':
            case '\u015E':
            case '\u015F':
            case '\u0160':
            case '\u0161':
            case '\u017F':
                return 's';
            case '\u0162':
            case '\u0163':
            case '\u0164':
            case '\u0165':
            case '\u0166':
            case '\u0167':
                return 't';
            case '\u00D9':
            case '\u00DA':
            case '\u00DB':
            case '\u00DC':
            case '\u00F9':
            case '\u00FA':
            case '\u00FB':
            case '\u00FC':
            case '\u0168':
            case '\u0169':
            case '\u016A':
            case '\u016B':
            case '\u016C':
            case '\u016D':
            case '\u016E':
            case '\u016F':
            case '\u0170':
            case '\u0171':
            case '\u0172':
            case '\u0173':
                return 'u';
            case '\u0174':
            case '\u0175':
                return 'w';
            case '\u00DD':
            case '\u00FD':
            case '\u00FF':
            case '\u0176':
            case '\u0177':
            case '\u0178':
                return 'y';
            case '\u0179':
            case '\u017A':
            case '\u017B':
            case '\u017C':
            case '\u017D':
            case '\u017E':
                return 'z';
        }
        return c;
    }
    
    /**
     * Returns the levenshein distance between the compared keys for these strings.
     * @param a
     * @param b
     * @return 
     */
    public int lenvenshteinComparisonKeyed(String a, String b) {
        String keyA = this.key(a);
        String keyB = this.key(b);
        
        return StringUtils.getLevenshteinDistance(keyA, keyB);
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return 
     */
    public int compare(String a, String b) {
        return this.key(a).compareTo(this.key(b));
    }
    
    /**
     * Given a set of possible strings that could explain the differences between 
     * a and b, gives true if the results of key comparison changes to 0 after
     * replacing all the candidates by nothing.
     */
    public boolean explainsTheDifference(String a, String b, List<String> candidateDifferences) {
        int dist = this.lenvenshteinComparisonKeyed(a, b);
        a = a.toLowerCase(Locale.ENGLISH);
        b = b.toLowerCase(Locale.ENGLISH);
        for (String cand : candidateDifferences) {
            a = a.replaceAll(cand.toLowerCase(Locale.ENGLISH), "");
            b = b.replaceAll(cand.toLowerCase(Locale.ENGLISH), "");
        }
        int dist2 = this.lenvenshteinComparisonKeyed(a, b);
        
        if(dist2 ==  0 && dist > dist2)
            return true;
        return false;
    }
    
    private static PseudoFingerprintChemicalNames instance;
    
    public static PseudoFingerprintChemicalNames getInstance() {
        if(instance==null)
            instance = new PseudoFingerprintChemicalNames();
        return instance;
    }
}
