/*
 * Copyright 2013,2014 E. Jed Barlow <ejbarlow@ualberta.ca>
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jebl.util.ProgressListener;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;
import com.biomatters.geneious.publicapi.plugin.*;

public class SeqPartitioner extends DocumentOperation {
    boolean DEBUG = true;

    @Override
    public GeneiousActionOptions getActionOptions() {
        return new GeneiousActionOptions(
                                         "Partition Allele Multiset...",
                                         "Create a partition table of alleles.")
            .setMainMenuLocation(GeneiousActionOptions.MainMenu.Tools)
            .setInMainToolbar(true);
    }

    @Override
    public String getHelp() {
        return "Select alignment files.";
    }

    @Override
    public DocumentSelectionSignature[] getSelectionSignatures() {
        return new DocumentSelectionSignature[] {
            DocumentSelectionSignature.forNucleotideAlignments(
                                                               1,
                                                               Integer.MAX_VALUE)
        };
    }

    @Override
    public Options getOptions(AnnotatedPluginDocument... documents)
            throws DocumentOperationException {
        String example_name;

        try {
            SequenceAlignmentDocument d = (SequenceAlignmentDocument) documents[0].getDocument();
            example_name = d.getSequences().get(0).getName();
        }
        catch (Exception e) {
            example_name = "sample_sequence_name";
        }
        return new SeqPartitionerOptions(example_name);
    }

    @Override
    public List<AnnotatedPluginDocument> performOperation(
            AnnotatedPluginDocument[] documents,
            ProgressListener progressListener, Options options)
            throws DocumentOperationException {
        SeqPartitionerOptions opts;
        SequenceAlignmentDocument seqal;
        Pattern pattern;

        opts = (SeqPartitionerOptions) options;
        pattern = Pattern.compile(opts.getRegexpMatch());

        TableConstructor tc = new TableConstructor(
                ((SequenceAlignmentDocument)documents[0].getDocument()).getSequences().size(),
                documents.length);

        for (int d = 0; d < documents.length; d++) {
            seqal = (SequenceAlignmentDocument) documents[d].getDocument();

            tc.addDocument(seqal, pattern, opts.getRegexpReplacement());
        }

        try {
            String[][] table;
            FileWriter f = new FileWriter(
                    opts.getOutputDir() + File.separator + opts.getBaseName() + "_gene_vs_strain.csv");

            table = tc.RenderGeneVStrainTable();

            for (int j = 0; j < table[0].length; j++) {
                for (int i = 0; i < table.length; i++) {
                    if (i != 0)
                        f.write(",");
                    if (table[i][j] != null) f.write(table[i][j]);
                }
                f.write("\n");
            }
            f.close();

            f = new FileWriter(
                    opts.getOutputDir() + File.separator + opts.getBaseName() + "_strain_vs_strain_matrix.csv");

            table = tc.RenderStrainVStrainTable();

            for (int j = 0; j < table[0].length; j++) {
                for (int i = 0; i < table.length; i++) {
                    if (i != 0)
                        f.write(",");
                    if (table[i][j] != null) f.write(table[i][j]);
                }
                f.write("\n");
            }

            f.close();

            f = new FileWriter(
                    opts.getOutputDir() + File.separator + opts.getBaseName() + "_strain_vs_strain.csv");

            table = TableConstructor.StrainVStrainToSideBySide(table);

            for (int j = 0; j < table[0].length; j++) {
                for (int i = 0; i < table.length; i++) {
                    if (i != 0)
                        f.write(",");
                    if (table[i][j] != null) f.write(table[i][j]);
                }
                f.write("\n");
            }
            f.close();
        } catch (IOException e) {
            // TODO: Show popup dialog box or something.
            e.printStackTrace();
        }

        return new ArrayList<AnnotatedPluginDocument>();
    }
}
