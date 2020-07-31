package org.free;

import java.util.stream.IntStream;

import mtree.DistanceFunction;

/* Conventional implementation of edit distance allowing insertions,
   substitutions, deletions using dynamic programming*/

public class LevenshteinDistance implements DistanceFunction<String> {

    @Override
    public double calculate(String A, String B) {
        int[][] memoizationMatrix = new int[A.length() + 1][B.length() + 1];

        IntStream.rangeClosed(0, A.length()).forEach(index -> {
            memoizationMatrix[index][0] = index;
        });

        IntStream.rangeClosed(1, B.length()).forEach(index -> {
            memoizationMatrix[0][index] = index;
        });

        IntStream.range(0, A.length()).forEach(outer -> {
            IntStream.range(0, B.length()).forEach(inner -> {
                int cost = 0;
                if (A.charAt(outer) != B.charAt(inner)) {
                    cost = 1;
                }

                memoizationMatrix[outer + 1][inner + 1] =
                    Math.min(memoizationMatrix[outer][inner + 1] + 1,
                        Math.min(memoizationMatrix[outer+ 1][inner] + 1,
                                 memoizationMatrix[outer][inner] + cost));
            });
        });

        return memoizationMatrix[A.length()][B.length()];
    }
}
