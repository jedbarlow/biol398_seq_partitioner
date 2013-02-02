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
    /* Used for construction of the table. */
    private HashMap<String, HashMap<String, Integer>> htable;

    /* These get rendered to. */
    private List<String> genes;
    private List<String> strains;
    private List<List<Integer>> data;

    public TableConstructor() {
        htable = new HashMap<String, HashMap<String, Integer>>();

        genes = null;
        strains = null;
        data = null;
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
        HashMap<String, Integer> row;

        for(int i = 0; i < data.size() && i < groups.length; i++) {
            row = htable.get(strains[i]);

            if (row == null) {
                row = new HashMap<String, Integer>();
                htable.put(strains[i], row);
            }

            row.put(gene, groups[i]);
        }
    }

    private boolean CompareRows(List<Integer> r1, List<Integer> r2) {
        return r1 == r2;
    }
    public String[][] RenderTable() {
        /* Method stub */
        return null;
    }
}
