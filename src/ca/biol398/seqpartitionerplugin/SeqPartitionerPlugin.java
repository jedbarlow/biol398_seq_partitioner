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
        return "";
    }
    public String getAuthors() {
        return "Jed Barlow <jbarlow@lavabit.com>";
    }
    public String getVersion() {
        return "0.1";
    }
    public String getMinimumApiVersion() {
        return "";  // TODO: figure out what to put here
    }
    public int getMaximumApiVersion() {
        return 6;  // TODO: figure out what to put here
    }
}
