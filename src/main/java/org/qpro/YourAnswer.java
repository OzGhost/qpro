package org.qpro;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
        return f3(your, mine);
    }

    public AnswerSet f3(String[] your, String[] mine) throws Exception {
        List<String> yo = new ArrayList<>(your.length);
        List<String> mo = new ArrayList<>(mine.length);
        List<String> bo = new ArrayList<>(Math.min(your.length, mine.length));

        Map<String, Integer> mask = new HashMap<>();
        for (String y: your) {
            mask.put(y, 1);
        }
        for (String m: mine) {
            mask.merge(m, 2, (o, n) -> o |= n);
        }
        for (Map.Entry<String, Integer> e: mask.entrySet()) {
            int v = e.getValue();
            String k = e.getKey();
            if (v == 1) {
                yo.add(k);
            } else if (v == 2) {
                mo.add(k);
            } else if (v == 3) {
                bo.add(k);
            }
        }

        AnswerSet out = new AnswerSet();
        out.onlyYour = yo;
        out.onlyMine = mo;
        out.our = bo;
        return out;
    }

    public AnswerSet f2(String[] your, String[] mine) throws Exception {
        List<String> yo = new ArrayList<>(your.length);
        List<String> mo = new ArrayList<>(mine.length);
        List<String> bo = new ArrayList<>(Math.min(your.length, mine.length));

        for (String y: your) {
            boolean b = false;
            for (String m: mine) {
                if (y.equals(m)) {
                    b = true;
                    break;
                }
            }
            if (b) {
                bo.add(y);
            } else {
                yo.add(y);
            }
        }

        for (String m: mine) {
            boolean b = false;
            for (String y: your) {
                if (m.equals(y)) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                mo.add(m);
            }
        }
        
        AnswerSet out = new AnswerSet();
        out.onlyYour = yo;
        out.onlyMine = mo;
        out.our = bo;
        return out;
    }

    public AnswerSet f1(String[] your, String[] mine) throws Exception {
        AnswerSet out = new AnswerSet();
        Set<String> l = new HashSet<>();
        Set<String> r = new HashSet<>();
        List<String> o = new ArrayList<>(Math.min(your.length, mine.length));
        List<String> ol = new ArrayList<>(your.length);
        List<String> or = new ArrayList<>(mine.length);
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

