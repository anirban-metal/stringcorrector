package org.free;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import mtree.*;
import mtree.utils.Utils;

/**
 * Class is not {@link java.io.Serializable}.
 * We are exclusively concerned with syntax of languages, and not on
 * semantics. Thus we don't need any complicated indexing strategy.
 * Fortunately, some implementations of edit distances of strings impose
 * a metric topology on the set of all strings, and a simple and robust
 * implementation using MTree (metric-tree, a spatial data structure similar
 * to kd-trees) suffices for out purposes. We have deterministic behaviour
 * which is a necessity in here. An on disk implementation of
 * MTrees based on libGiST is present, but is primitive and requires some
 * refactoring to compile it for a modern nix system.
 */
public class StringMTree {
    private int tolerance;
    private MTree<String> mTree;
    private static final PromotionFunction<String> nonRandomPromotion =
        (dataSet, distanceFunction) -> Utils.minMax(dataSet);
    private static final LevenshteinDistance LE_DISTANCE = new LevenshteinDistance();
    private HashMap<String, String> hashTable = new HashMap<>();

    /**
     @param(tolerance : Integer) : Don't return Strings greater than tolerance level
    */
    public StringMTree() {
        // Very high value for testing
        this.tolerance = 10;
        mTree = new MTree<>(2, new LevenshteinDistance(),
                new ComposedSplitFunction<>(nonRandomPromotion, new PartitionFunctions.BalancedPartition<String>()));
    }

    public void setTolerance(int tolerance) {
        if (tolerance > 0) {
            this.tolerance = tolerance;
        } else {
            throw new InvalidParameterException("Tolerance has to be at least 1");
        }
    }

    public int getTolerance() {
        return this.tolerance;
    }

    public void add(String str) {
        // Don't add String if already present to prevent undefined behaviour
        if (hashTable.containsKey(str))
            return;
        mTree.add(str);
    }

    // Return the most likely result within tolerance limit
    public Optional<String> query(String str) {
        Optional<String> response = Optional.empty();
        // First look in hash table if we get lucky
        if (hashTable.containsKey(str))
            return Optional.of(str);
        // Otherwise String is malformed
        MTree<String>.Query query = mTree.getNearest(str);
        Iterator<MTree<String>.ResultItem> ri = query.iterator();
        String res = "";
        if (ri.hasNext())
            res = ri.next().data;
        if (LE_DISTANCE.calculate(str, res) <= tolerance && !res.isBlank())
            response = Optional.of(res);
        return response;
    }

    public boolean removeRelaxed(String str) {
        Optional<String> res = query(str);
        if (res.isPresent()) {
            hashTable.remove(res.get());
            // We remove malformed Strings too
            return mTree.remove(res.get());
        }
        return false;
    }

    public boolean removeStrict(String str) {
        if (hashTable.containsKey(str)) {
            hashTable.remove(str);
            return mTree.remove(str);
        }
        return false;
    }

}
