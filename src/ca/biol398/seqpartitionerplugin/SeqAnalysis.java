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
     * On detection of such a situation, two of the conflicting sequences are
     * marked with -1.  However this marking alone does not completely identify
     * the all the sequences in violation of transitivity.
     *
     * TODO: provide more informative feedback on error.
     */
    public static int[] PartitionSequences(String[] ms) {
        int nextClassId;
        int[] seqToEqClassMap;
        int unmapped;

        unmapped = 0;
        nextClassId = 1;
        seqToEqClassMap = new int[ms.length];

        for (int i = 0; i < ms.length; i++) {
            if (seqToEqClassMap[i] == unmapped) {
                seqToEqClassMap[i] = nextClassId;
                nextClassId++;
            }

            for (int j = 0; j < ms.length; j++) {
                if (i == j) continue;

                boolean eqvClassMatch = (seqToEqClassMap[i] == seqToEqClassMap[j]);
                if (eqvClassMatch)
                	continue;

                boolean seqMatch = CompareSequences(ms[i], ms[j]);

                if (seqMatch != eqvClassMatch) {
                    if (seqToEqClassMap[j] == unmapped)
                        seqToEqClassMap[j] = seqToEqClassMap[i];
                    else {
                        seqToEqClassMap[i] = -1;
                        seqToEqClassMap[j] = -1;
                    }
                }
            }
        }

        return seqToEqClassMap;
    }

    /*
     * Equivalence up to a special character.
     */
    private static boolean CompareSequences(String sa1, String sa2) {
        int min;
        String maxstr;

        min = Math.min(sa1.length(), sa2.length());

        /* Test for match up to a special char. */
        for (int i = 0; i < min; i++) {
            if (sa1.charAt(i) == sa2.charAt(i) ||
                sa1.charAt(i) == EmptyPlaceChar ||
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

            for (int i = min; i < maxstr.length(); i++) {
                if (maxstr.charAt(i) != EmptyPlaceChar)
                    return false;
            }
        }

        return true;
    }
}
