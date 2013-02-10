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

    public void addDocument (SequenceAlignmentDocument sa) {
        List<SequenceDocument> seqDocs;
        String[]               strains;
        String[]               seqs;

        seqDocs = sa.getSequences();
        strains = new String[seqDocs.size()];
        seqs    = new String[seqDocs.size()];

        for(int i = 0; i < seqDocs.size(); i++) {
            strains[i] = seqDocs.get(i).getName();
            seqs[i] = seqDocs.get(i).getSequenceString();
        }

        addColumn(
                sa.getName(),
                strains,
                SeqAnalysis.PartitionSequences(seqs));
    }

    /*
     * Add allele partition data to the table.
     */
    private void addColumn (String gene, String[] strains, int[] groups) {
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

    private int compareRows(List<Integer> r1, List<Integer> r2) {
        int result = 0;
        for(int i = 0; i < r1.size(); i++) {
            if(r1.get(i) != r2.get(i))
                result++;
        }

        return result;
    }

    /*
     * Partition the rows by assigning each a group number.
     * For example, the data set
     *   1, 1, 1
     *   1, 2, 1
     *   1, 2, 1
     *   1, 2, 2
     * yields the following column
     *   1
     *   2
     *   2
     *   3
     */
    private int[] createRowGroups() {
        int counter;
        int[] groups = new int[strains.size()];

        for(int i = 0; i < groups.length; i++)
            groups[i] = 0;

        counter = 1;
        for(int i = 0; i < groups.length; i++) {
            if(groups[i] != 0) continue;
            groups[i] = counter;
            counter++;

            for(int j = i + 1; j < groups.length; j++) {
                if(groups[j] != 0) continue;

                if(compareRows(data.get(i), data.get(j)) == 0)
                    groups[j] = groups[i];
            }
        }

        return groups;
    }

    /*
     * Produces a table of results with gene names in the top row,
     * strain names in the left column, and data filling in the
     * middle.
     */
    public String[][] RenderGeneVStrainTable() {
        String[][] result;
        List<Integer> row;
        int[] rowGroupColumn;

        /*
         * Need room for the data, the top row of gene names, the
         * left column of strain names, and the final column of row
         * groupings.
         */
        result = new String[genes.size() + 2][strains.size() + 1];

        result[0][0] = "";
        for(int i = 0; i < genes.size(); i++) {
            result[i + 1][0] = genes.get(i);
            result[0][i + 1] = strains.get(i);
        }

        for(int j = 0; j < genes.size(); j++) {
            row = data.get(j);

            for(int i = 0; i < strains.size(); i++) {
                result[i + 1][j + 1] = row.get(i).toString();
            }
        }

        rowGroupColumn = createRowGroups();

        result[genes.size() + 1][0] = "Combined Type";
        for(int j = 0; j < strains.size(); j++) {
            result[genes.size() + 1][j + 1] = Integer.toString(rowGroupColumn[j]);
        }

        return result;
    }

    public String[][] RenderStrainVStrainTable() {
        String[][] result;
        List<Integer> row;

        /*
         * Need room for the data, the top row of gene names, the
         * left column of strain names.
         */
        result = new String[genes.size() + 1][strains.size() + 1];

        result[0][0] = "";
        for(int i = 0; i < genes.size(); i++) {
            result[i + 1][0] = genes.get(i);
            result[0][i + 1] = strains.get(i);
        }

        for(int j = 0; j < genes.size(); j++) {
            row = data.get(j);

            for(int i = 0; i < strains.size(); i++) {
                result[i + 1][j + 1] = Integer.toString(compareRows(row, data.get(i)));
            }
        }

        return result;
    }
}
