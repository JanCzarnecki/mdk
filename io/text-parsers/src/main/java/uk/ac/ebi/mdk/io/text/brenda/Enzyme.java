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

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Enzyme implements Serializable{
	
	private String uniProtAcc;
	private String uniProtID;
        private String pdbID;
	private List<String> names;
	private List<String> ecNumbers;

        private void init() {
            this.names = new ArrayList<String>();
            this.ecNumbers = new ArrayList<String>();
        }

        public Enzyme() {
            this.init();
        }
	
	public void setUniProtAcc(String acc) {
		this.uniProtAcc = acc;
	}
	
	public void setUniProtID(String id) {
		this.uniProtID = id;
	}

        public String getUniProtAcc() {
            return uniProtAcc;
        }

        public String getUniProtID() {
            return uniProtID;
        }

        public void setPDBID(String pdbId) {
            this.pdbID = pdbId;
        }

        public String getPDBID() {
            return this.pdbID;
        }
	
	public void addName(String name) {
		this.names.add(name);
	}

        public String[] getNames() {
            return names.toArray(new String[1]);
        }

	public void addEcNumber(String ecNum) {
		this.ecNumbers.add(ecNum);
	}
	public void removeName(String name) {
		this.names.remove(name);
	}
	public void removeEcNumber(String ecNum) {
		this.names.remove(ecNum);
	}

        @Override
	public boolean equals(Object n) {
                if(!(n instanceof Enzyme))
                    return false;
		if(this.uniProtAcc != null && ((Enzyme) n).uniProtAcc != null && this.uniProtAcc.equals(((Enzyme) n).uniProtAcc))
			return true;
		if(this.uniProtID != null && ((Enzyme) n).uniProtID != null && this.uniProtID.equals(((Enzyme) n).uniProtID))
			return true;
		return false;
		
	}

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + (this.uniProtAcc != null ? this.uniProtAcc.hashCode() : 0);
            hash = 37 * hash + (this.uniProtID != null ? this.uniProtID.hashCode() : 0);
            return hash;
        }
        public boolean shareECNumber(Enzyme n) {
            for(String ec : this.ecNumbers)
                if(n.hasECnumber(ec))
                    return true;
            return false;
        }
        public boolean hasECnumber(String ec) {
            return this.ecNumbers.contains(ec);
        }

	

}
