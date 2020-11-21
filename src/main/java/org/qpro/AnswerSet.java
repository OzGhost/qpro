package org.qpro;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AnswerSet {
    private static final Random r = new Random();
    public List<String> onlyYour = Collections.emptyList();
    public List<String> onlyMine = Collections.emptyList();
    public List<String> our = Collections.emptyList();
    @Override
    public int hashCode() {
        List<String> x = new ArrayList(
                onlyYour.size() +
                onlyMine.size() +
                our.size()
        );
        x.addAll(onlyYour); x.addAll(onlyMine); x.addAll(our);
        return x.get(r.nextInt(x.size())).hashCode();
    }
    @Override
    public String toString(){
        return "" + onlyYour.size() + " " + onlyMine.size() + " " + our.size();
    }
}

