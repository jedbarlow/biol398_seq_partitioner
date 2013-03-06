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

import jebl.util.ProgressListener;

import com.biomatters.geneious.publicapi.plugin.*;

//import java.util.*;

public class SeqPartitionerOptions extends Options {
    private FileSelectionOption output_dir;

    public String getOutputDir() {
        return output_dir.getValue();
    }

    public SeqPartitionerOptions() {
        output_dir = this.addFileSelectionOption("output_dir", "CSV output directory", "");
    }
}