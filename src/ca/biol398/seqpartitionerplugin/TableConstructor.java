/*
 * Copyright 2013 Jed Barlow <jbarlow@lavabit.com>
 *
 * This file is part of SeqPartitioner.
 *
 * SeqPartitioner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SeqPartitioner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SeqPartitioner.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ca.biol398.seqpartitionerplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;

public class TableConstructor {
    private List<String> genes;
    private List<String> strains;
    private List<List<Integer>> data;

    private static int absentDataIndicator = -1;

    private int expectedNumGenes;

    public TableConstructor(int expectedNumGenes, int expectedNumStrains) {
        genes = new ArrayList<String>(expectedNumGenes);
        strains = new ArrayList<String>(expectedNumStrains);
        data = new ArrayList<List<Integer>>(expectedNumStrains);

        this.expectedNumGenes = expectedNumGenes;
    }

    public void AddDocument (SequenceAlignmentDocument sa) {
        List<SequenceDocument> seqDocs;
        String[]               strains;
        String[]               seqs;
        int[]                  groups;

        seqDocs = sa.getSequences();
        strains = new String[seqDocs.size()];
        seqs    = new String[seqDocs.size()];
        groups  = new    int[seqDocs.size()];

        for(int i = 0; i < seqDocs.size(); i++) {
            strains[i] = seqDocs.get(i).getName();
            seqs[i] = seqDocs.get(i).getSequenceString();
        }

        AddColumn(
                sa.getName(),
                strains,
                SeqAnalysis.PartitionSequences(seqs));
    }

    /*
     * Add allele partition data to the table.
     */
    private void AddColumn (String gene, String[] strains, int[] groups) {
        List<Integer> row;

        addBlankColumn(gene);

        for(int i = 0; i < strains.length; i++) {
            row = getRow(strains[i]);
            row.set(row.size()-1, groups[i]);
        }
    }

    /*
     * Create a new column in the table under the given heading, and
     * populate the entries with absentDataIndicator.
     */
    private void addBlankColumn(String gene) {
        /*
         * TODO: detect error of duplicate column title (gene)
         */

        genes.add(gene);
        for(int i = 0; i < strains.size(); i++) {
            data.get(i).add(absentDataIndicator);
        }
    }

    /*
     * Get the list of integers associated with a strain name.
     * Create a new such row if none exists for the strain.
     */
    private List<Integer> getRow(String strain) {
        List<Integer> row;

        for(int i = 0; i < strains.size(); i++) {
            if(strains.get(i) == strain)
                return data.get(i);
        }

        /* No row exists for the strain, create a new row. */

        row = new ArrayList<Integer>(
                Math.max(expectedNumGenes, genes.size()));

        for(int i = 0; i < row.size(); i++) {
            row.set(i, absentDataIndicator);
        }

        return row;
    }

    private boolean CompareRows(List<Integer> r1, List<Integer> r2) {
        return r1 == r2;
    }
    public String[][] RenderTable() {
        /* Method stub */
        return null;
    }
}
