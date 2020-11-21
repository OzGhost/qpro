package org.qpro;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class YourAnswer {

    /*
    public AnswerSet answerTo(String[] your, String[] mine) throws Exception {
        AnswerSet out = new AnswerSet();
        // your code ...
        out.onlyYour = Collections.emptyList(); // < set answer here 
        out.onlyMine = Collections.emptyList(); // < set answer here 
        out.our = Collections.emptyList();      // < set answer here 
        return out;
    }
    */

    public AnswerSet answerTo(String[] your, String[] mine) throws Exception {
        AnswerSet out = new AnswerSet();
        Set<String> l = new HashSet<>();
        Set<String> r = new HashSet<>();
        List<String> o = new ArrayList<>();
        List<String> ol = new ArrayList<>();
        List<String> or = new ArrayList<>();
        for (String x: your) l.add(x);
        for (String x: mine) r.add(x);
        for (String x: l) {
            if (r.contains(x)) {
                o.add(x);
            } else {
                ol.add(x);
            }
        }
        for (String x: r) {
            if (!l.contains(x)) {
                or.add(x);
            }
        }
        out.onlyYour = ol;
        out.onlyMine = or;
        out.our = o;
        return out;
    }

}

