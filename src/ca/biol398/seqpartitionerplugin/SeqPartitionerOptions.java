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
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.virion.jam.util.SimpleListener;

import com.biomatters.geneious.publicapi.plugin.*;

public class SeqPartitionerOptions extends Options {
    private FileSelectionOption output_dir;
    private StringOption base_name;
    private StringOption regexp_match;
    private StringOption regexp_replace;
    private Option<String, ? extends JComponent> example_regex_output;
    private String example_name;

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

    public SeqPartitionerOptions(String example_data_object_name) {
        example_name = example_data_object_name;

        String default_output_dir = System.getProperty("user.home") +
                File.separator + "Desktop" + File.separator + "csv";

        output_dir = this.addFileSelectionOption("output_dir", "CSV output directory",
                default_output_dir, new String[] {}, "Browse");
        output_dir.setSelectionType(JFileChooser.DIRECTORIES_ONLY);

        base_name = this.addStringOption("base_name", "CSV base file name", "partition");

        SimpleListener changeListener = new SimpleListener() {
            @Override
            public void objectChanged() {
                updateRegexExample();
            }
        };

        this.addLabel("Allele name extraction from sequence names");
        regexp_match = this.addStringOption("regexp_match",
                "Java regular expression to match", "(.*?)_.*");
        regexp_match.addChangeListener(changeListener);
        regexp_replace = this.addStringOption("regexp_replace",
                 "Java regular expression replacement", "$1");
        regexp_replace.addChangeListener(changeListener);

        this.addHelpButton("Help",
                "The regular expression replacement is used to group gene sequences by strain " +
                "by extracting the strain name from the full sequence name.  For example, " +
                "matching (.*?)_.* with replacement $1 will transform STRAIN_GENE to STRAIN.\n\n" +
                "For help with Java regular expressions, see" +
                "http://docs.oracle.com/javase/tutorial/essential/regex/intro.html " +
                "and http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html.");

        this.addLabel("Example result of applying the regular expression:");
        this.addLabel("    " + example_name);
        this.addLabel("transforms to");
        this.example_regex_output = this.addLabel("");
        updateRegexExample();
    }

    void updateRegexExample()
    {
        try {
            example_regex_output.setValue("    " +
                    Pattern.compile(regexp_match.getValue())
                    .matcher(example_name)
                    .replaceAll(regexp_replace.getValue()));
        }
        catch (Exception e) {
            example_regex_output.setValue("    *Error in regular expression*");
        }
    }

    @Override
    public boolean areValuesGoodEnoughToContinue() {
        try {
            Pattern.compile(regexp_match.getValue())
            .matcher("")
            .replaceAll(regexp_replace.getValue());
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
