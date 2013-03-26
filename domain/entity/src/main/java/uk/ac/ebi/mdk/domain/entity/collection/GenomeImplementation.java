/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.collection;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * GenomeImplementation - 2011.10.18 <br> Implementation of genome interface
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class GenomeImplementation implements Genome {

    private static final Logger LOGGER = Logger.getLogger(GenomeImplementation.class);
    private Map<Integer, Chromosome> chromosomes = new HashMap();

    private final Reconstruction reconstruction;

    public GenomeImplementation(Reconstruction reconstruction) {
        this.reconstruction = reconstruction;
    }

    /**
     * @inheritDoc
     */
    public Collection<Chromosome> getChromosomes() {
        return chromosomes.values();
    }

    /**
     * @inheritDoc
     */
    public Collection<Gene> getGenes() {

        List<Gene> genes = new ArrayList<Gene>();

        for (Chromosome c : getChromosomes()) {
            genes.addAll(c.getGenes());
        }

        return genes;

    }

    /**
     * @inheritDoc
     */
    public Chromosome getChromosome(int number) {

        Chromosome chromosome = chromosomes.get(number);

        if(chromosome == null){
            chromosome = new ChromosomeImplementation(reconstruction, number);
            chromosomes.put(number, chromosome);
        }

        return chromosome;
    }

    /**
     * @inheritDoc
     */
    public boolean add(Chromosome chromosome) {
        return chromosomes.put(chromosome.getChromosomeNumber(), chromosome) != null;
    }

    /**
     * @inheritDoc
     */
    public boolean remove(Chromosome chromosome) {
        return chromosomes.remove(chromosome.getChromosomeNumber()) != null;
    }

    /**
     * @inheritDoc
     */
    public boolean add(int number, Gene gene) {
        return getChromosome(number).add(gene);
    }

    /**
     * @inhetiDoc
     */
    @Override
    public boolean remove(Gene gene) {
        return gene.getChromosome() != null && gene.getChromosome()
                                                   .remove(gene);
    }



    /**
     * Access a gene for the given index on the given chromosome
     */
    public Gene getGene(int number, int index) {
        if (chromosomes.containsKey(number)) {
            return chromosomes.get(number).getGenes().get(index);
        }
        return null;
    }

    public int[] getIndex(Gene gene) {
        for (Chromosome c : getChromosomes()) {
            int index = c.getGenes().indexOf(gene);
            if (index != -1) ;
            return new int[]{c.getChromosomeNumber(), index};
        }
        return new int[]{-1, -1};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenomeImplementation that = (GenomeImplementation) o;

        if (chromosomes != null ? !chromosomes.equals(that.chromosomes) : that.chromosomes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return chromosomes != null ? chromosomes.hashCode() : 0;
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String getAbbreviation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Identifier getIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void setIdentifier(Identifier identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void setAbbreviation(String abbreviation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String getAccession() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String getBaseType() {
        return "Genome";
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
