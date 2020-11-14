package org.qpro;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class CheckerTest {

    @Test
    public void test_it() {
        Human me = new Human();
        me.birthday = "2011-12-03T10:15:30";
        me.trace = new ArrayList<>(2);
        Footprint f = new Footprint();
        f.time = 10;
        f.nut = "NOP";
        me.trace.add(f);
        f = new Footprint();
        f.time = 1;
        me.trace.add(f);

        long beginning = System.nanoTime();
        Map<String, String> vios = new MyChecker().check(me);
        System.out.println(System.nanoTime() - beginning);
        for (Map.Entry<String, String> e: vios.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        assertEquals("must not be blank", vios.get("_.name"));
        assertEquals("must >= 10.0", vios.get("_.trace.1.time"));
        assertEquals("must be one of [WAL,PEA]", vios.get("_.trace.0.nut"));
        assertEquals("must be a date according to ISO-8601 instant",
                vios.get("_.birthday"));
    }

    private class MyChecker extends Checker {
        private MyChecker() {
            addRestriction("_", Human.class, NOT_NULL);
            addRestriction("_.name", String.class, NOT_BLANK);
            addRestriction("_.birthday", String.class, IS_DATE);
            addRestriction("_.trace.*.time", Integer.class, MIN(10));
            addRestriction("_.trace.*.nut", String.class, ONE_OF(NUT.values()));
        }
    }

    private static class Human {
        public String name;
        public String birthday;
        public List<Footprint> trace;
        public String getName() { return name; }
        public String getBirthday() { return birthday; }
        public List<Footprint> getTrace() { return trace; }
    }

    private static class Footprint {
        public Integer time;
        public String nut;
        public Integer getTime() { return time; }
        public String getNut() { return nut; }
    }

    private static enum NUT {
        PEA,WAL;
    }
}

