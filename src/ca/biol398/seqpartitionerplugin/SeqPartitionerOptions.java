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

import javax.swing.JFileChooser;

import jebl.util.ProgressListener;

import com.biomatters.geneious.publicapi.plugin.*;

public class SeqPartitionerOptions extends Options {
    private FileSelectionOption output_dir;
    private StringOption base_name;
    private StringOption regexp_match;
    private StringOption regexp_replace;

    public String getOutputDir() {
        return output_dir.getValue();
    }
    public String getBaseName() {
        return base_name.getValue();
    }
    public String getRegexpMatch() {
        return regexp_match.getValue();
    }
    public String getRegexpReplacement() {
        return regexp_replace.getValue();
    }

    public SeqPartitionerOptions() {
        output_dir = this.addFileSelectionOption("output_dir", "CSV output directory", "", new String[] {"csv"}, "Browse");
        output_dir.setSelectionType(JFileChooser.DIRECTORIES_ONLY);

        base_name = this.addStringOption("base_name", "CSV base file name", "partition");

        this.addLabel("Allele name extraction from sequence names");
        regexp_match   = this.addStringOption("regexp_match",   "Java regular expression to match",    "(.*?)_.*");
        regexp_replace = this.addStringOption("regexp_replace", "Java regular expression replacement", "$1");
        this.addHelpButton("Help",
                "The regular expression replacement is used to correlate genes between strains " +
                "by extracting the gene name from the full sequence name.  For example, " + 
                "matching \"(.*?)_.*\" with replacement \"$1\" will transform \"GENE_STRAIN\" " +
                "to \"GENE\".\n\n" +
                "For help with Java regular expressions, " +
                "see http://docs.oracle.com/javase/tutorial/essential/regex/intro.html " +
                "and http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html.");
    }
}
