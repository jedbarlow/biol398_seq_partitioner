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
package ca.biol398.seqpartitioner;

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature; 
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.Options;

public class SeqPartitioner extends DocumentOperation {
    boolean DEBUG = true;
    
    @Override
    public GeneiousActionOptions getActionOptions() {
        return new GeneiousActionOptions(
                                         "Partition Allele Multiset...",
                                         "Create a partition table of alleles.")
            .setMainMenuLocation(GeneiousActionOptions.MainMenu.Tools);
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
        return new SeqPartitionerOptions();
    }
    
    @Override
    public List<AnnotatedPluginDocument> performOperation(
                                                          AnnotatedPluginDocument[] documents,
                                                          ProgressListener progressListener,
                                                          Options options)
        throws DocumentOperationException {
        return new ArrayList<AnnotatedPluginDocument>();
    }
}
