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

import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;

public class SeqAnalysis {
    private static char EmptyPlaceChar = '-';

    /*
     * Attempt to partition a multiset (array) of strings.  Transitivity of the
     * equivalence relation may fail over some particular sequence sets.  For
     * example, the following will cause an error,
     *   1.  ATC
     *   2.  A-C
     *   3.  AGC
     * since
     *   1 == 2 == 3
     * but
     *   1 != 3
     * due to the relation being string equivalence up to a special character.
     * Such errors are immediately halted on, with two conflicting sequences
     * marked with -1 (note: this may not be the most helpful or informative
     * selection of two sequences in terms of helping to identify the conflict).
     *
     * TODO: provide more informative feedback on error.
     */
    public static int[] PartitionSequences(String[] ms) {
        int curClass;
        int[] ec;

        ec = new int[ms.length];
        for (int i = 0; i < ec.length; i++)
            ec[i] = 0;

        curClass = 1;
        for (int i = 0; i < ms.length; i++) {
            if(ec[i] == 0) {
                ec[i] = curClass;
                curClass++;
            }

            for (int j = 0; j < ms.length; j++) {
                if(ec[i] == ec[j]) continue;
                if(ec[j] == 0)
                    ec[j] = ec[i];
                else
                {
                    ec[j] = -1;
                    ec[j] = -1;
                    return ec;
                }
            }
        }

        return ec;
    }

    /*
     * Equivalence up to a special character.
     */
    public static boolean CompareSequences(String sa1, String sa2) {
        int min;
        String maxstr;

        min = Math.min(sa1.length(), sa2.length());

        /* Test for match up to a special char. */
        for(int i = 0; i < min; i++) {
            if(sa1.charAt(i) == sa2.charAt(i)) continue;
            if(sa1.charAt(i) == EmptyPlaceChar ||
                    sa2.charAt(i) == EmptyPlaceChar) continue;
            return false;
        }

        /*
         * sa1 and sa2 should always be the same length, but if they're not
         * then they can only match if the non-overlapping tail of the longer
         * string consists solely of the special character.
         */
        if (sa1.length() != sa2.length()) {
            if (sa1.length() > sa2.length())
                maxstr = sa1;
            else
                maxstr = sa2;

            for(int i = min; i < maxstr.length(); i++) {
                if (sa1.charAt(i) != EmptyPlaceChar)
                    return false;
            }
        }

        return true;
    }
}
