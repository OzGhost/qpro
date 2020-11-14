package org.qpro;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.*;
import java.lang.reflect.*;
import java.util.regex.*;

public abstract class Checker {

    protected static ValCheck<Object> NOT_NULL = new NullCheck();
    protected static ValCheck<String> NOT_BLANK = new BlankCheck();
    protected static ValCheck<Number> MIN(double min) {
        return new MinCheck(min);
    }
    protected static ValCheck<String> IS_DATE = new DateCheck();
    protected static <K extends Enum> ValCheck<String> ONE_OF(K[] vals) {
        return new EnumCheck(vals);
    }

    private List<CheckSet> cs = new LinkedList<>();
    private Map<String, String> vios = new HashMap<>();
    
    public Map<String, String> check(Object target) {
        for (CheckSet c: cs) {
            doCheckOn(c, target);
        }
        Map<String, String> v = vios;
        vios = new HashMap<>();
        return v;
    }

    private void doCheckOn(CheckSet c, Object target) {
        Map<String, Object> vals = travel(c.ap, c.type, target);
        for (Entry<String, Object> v: vals.entrySet()) {
            if ( ! c.vc.checkVal(v.getValue())) {
                vios.put(v.getKey(), c.vc.getMessage());
            }
        }
    }

    private Map<String, Object> travel(String ap, Class<?> type, Object root) {
        String[] p = ap.split("\\.");
        List<Entry<String, Object>> branches = Collections.emptyList();
        for (String s: p) {
            branches = step(s, branches, root);
        }
        Map<String, Object> out = new HashMap<>();
        for (Entry<String, Object> b: branches) {
            if (b.getValue() == null ||
                    type.isAssignableFrom(b.getValue().getClass())
            ) {
                out.put(b.getKey(), b.getValue());
            } else {
                vios.put(b.getKey(), "incompatible type");
            }
        }
        return out;
    }
    
    private List<Entry<String, Object>> step(
            String step,
            List<Entry<String, Object>> branches,
            Object root
    ) {
        List<Entry<String, Object>> out = new ArrayList<>();
        if (branches.isEmpty()) {
            out.add(new SimpleEntry("_", root));
            return out;
        }
        if ("*".equals(step)) {
            for (Entry<String, Object> b: branches) {
                Object p = b.getValue();
                String cap = b.getKey();
                if (p instanceof List) {
                    List l = (List)p;
                    int i = 0;
                    for (Object li: l) {
                        out.add(new SimpleEntry(cap + "." + i++, li));
                    }
                } else {
                    vios.put(b.getKey(), "must be a list");
                }
            }
            return out;
        }
        for (Entry<String, Object> b: branches) {
            Object p = b.getValue();
            if (p == null) {
                continue;
            }
            String ap = b.getKey() + "." + step;
            try {
                Method m = p.getClass().getMethod(toMethodName(step));
                Object v = m.invoke(p);
                out.add(new SimpleEntry(ap, v));
            } catch(IllegalAccessException | InvocationTargetException e) {
                vios.put(ap, "must be accessable");
            } catch(NoSuchMethodException e) {
                vios.put(ap, "must be exists");
            }
        }
        return out;
    }

    private String toMethodName(String step) {
        return new StringBuilder()
            .append("get")
            .append((char)(step.charAt(0) - 'a' + 'A'))
            .append(step.substring(1))
            .toString();
    }

    protected <X> void addRestriction(
            String ap,
            Class<? extends X> type,
            ValCheck<X> vc
    ) {
        CheckSet c = new CheckSet();
        c.ap = ap;
        c.type = type;
        c.vc = vc;
        cs.add(c);
    }

    private static class CheckSet {
        private String ap;
        private Class type;
        private ValCheck vc;
    }

    public static interface ValCheck<T> {
        String getMessage();
        boolean checkVal(T val);
    }

    private static class NullCheck implements ValCheck<Object> {
        public String getMessage() { return "must not be null"; }
        public boolean checkVal(Object val) { return val != null; }
    }

    private static class BlankCheck implements ValCheck<String> {
        public String getMessage() { return "must not be blank"; }
        public boolean checkVal(String val) {
            return val!=null && !val.trim().isEmpty();
        }
    }

    private static class MinCheck implements ValCheck<Number> {
        private double min;
        MinCheck(double m) { min = m; }
        public String getMessage() { return "must >= "+min; }
        public boolean checkVal(Number val) {
            return  val==null ||
                Double.compare(val.doubleValue(), min) >= 0;
        }
    }

    private static class EnumCheck implements ValCheck<String> {
        private final Set<String> goods = new HashSet<>();
        private final String msg;
        <K extends Enum> EnumCheck(K[] vals) {
            for (K x: vals) {
                goods.add(x.name());
            }
            String names = String.join(",", goods);
            msg = "must be one of [" + names + "]";
        }
        public String getMessage() { return msg; }
        public boolean checkVal(String val) {
            return val==null || goods.contains(val);
        }
    }

    private static class DateCheck implements ValCheck<String> {
        public String getMessage() {
            return "must be a date according to ISO-8601 instant";
        }
        public boolean checkVal(String val) {
            if (val == null) {
                return true;
            }
            return Pattern.matches(
                    "\\d{4}(?:-\\d{2}){2}T\\d{2}(?:\\:\\d{2}){2}(?:\\.\\d+)?Z",
                    val);
        }
    }
}

