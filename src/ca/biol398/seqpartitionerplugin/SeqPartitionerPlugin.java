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

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.GeneiousPlugin;

public class SeqPartitionerPlugin extends GeneiousPlugin {
    @Override
    public DocumentOperation[] getDocumentOperations() {
        return new DocumentOperation[] {
                new SeqPartitioner()
        };
    }

    @Override
    public String getName() {
        return "Sequence Partitioner Plugin";
    }

    public String getDescription() {
        return "Creates partition tables of allele sequence multisets";
    }
    public String getHelp() {
        return "See http://www.ualberta.ca/~ejbarlow/biol398/manual.pdf " +
               "for installation and usage instructions.";
    }
    public String getAuthors() {
        return "E. Jed Barlow <ejbarlow@ualberta.ca>";
    }
    public String getEmailAddressForCrashes() {
        return "E. Jed Barlow <ejbarlow@ualberta.ca>";
    }
    public String getVersion() {
        return "0.5";
    }
    public String getMinimumApiVersion() {
        return "4.14";
    }
    public int getMaximumApiVersion() {
        return 4;
    }
}
