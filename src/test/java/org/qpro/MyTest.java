package org.qpro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;

public class MyTest {

    private static final Random r = new Random();

    @Test
    public void another() throws Exception {
        int[] ioset = new int[33];
        ioset[0] = 1; ioset[1] = 2; ioset[2] = 3;
        for (int i = 3; i < 33; i++) ioset[i] = i+40-3;
        //ioset = new int[]{1,2,3}; // comment out for full mode
        List<Long[]> times = new ArrayList<>(33);
        YourAnswer ya = new YourAnswer();
        int dumper = 2052891929;
        for (int j = 0; j < 10; j++) {
            for (int io: ioset) {
                AnswerSet cp = ya.answerTo(read(io+"i.l"), read(io+"i.r"));
                dumper ^= cp.hashCode();
            }
            dumper = dumper << 8;
        }
        for (int j = 0; j < 10; j++) {
            List<Long> itimes = new ArrayList<>(33);
            for (int io: ioset) {
                System.out.println("__ [o0] round: "+(1+j)+" enter io #" + io);
                String[] onlyYour = read(io+"o.lo");
                String[] onlyMine = read(io+"o.ro");
                String[] our = read(io+"o.ol");
                long beginning = System.nanoTime();
                // Method under test
                AnswerSet cp = ya.answerTo(read(io+"i.l"), read(io+"i.r"));
                long taken = System.nanoTime() - beginning;
                assertEquals(onlyYour.length, cp.onlyYour.size());
                assertEquals(onlyMine.length, cp.onlyMine.size());
                assertEquals(our.length, cp.our.size());

                String[] x1 = onlyYour;
                String[] x2 = onlyMine;
                String[] x3 = our;
                String[] y1 =cp.onlyYour.toArray(new String[cp.onlyYour.size()]);
                String[] y2 =cp.onlyMine.toArray(new String[cp.onlyMine.size()]);
                String[] y3 = cp.our.toArray(new String[cp.our.size()]);
                Arrays.sort(x1); Arrays.sort(x2); Arrays.sort(x3);
                Arrays.sort(y1); Arrays.sort(y2); Arrays.sort(y3);

                assertEquals(String.join("\n", x1), String.join("\n", y1));
                assertEquals(String.join("\n", x2), String.join("\n", y2));
                assertEquals(String.join("\n", x3), String.join("\n", y3));
                itimes.add(taken);
            }
            times.add(itimes.toArray(new Long[itimes.size()]));
        }
        System.out.println("__ [o0] taken(s): " + dumper);
        System.out.println();
        System.out.println();
        timing(times);
        System.out.println();
        System.out.println();
    }

    private String[] read(String name) throws Exception {
        String p = "src/test/resources/ioset/"+name;
        List<String> ll = Files.readAllLines(new File(p).toPath());
        return ll.toArray(new String[ll.size()]);
    }

    private void timing(List<Long[]> times) {
        Long[][] x = new Long[times.size()][];
        for (int i = 0; i < times.size(); i++) {
            x[i] = times.get(i);
        }
        long[] rs = new long[x[0].length];
        for (int j = 0; j < x[0].length; j++) {
            long s = 0;
            for (int i = 0; i < x.length; i++) {
                s += x[i][j];
            }
            rs[j] = s/x.length;
        }
        System.out.print(rs[0]);
        for (int i = 1; i < rs.length; i++) {
            System.out.print(","+rs[i]);
        }
        System.out.println();
    }

    public void firstCase() throws Exception {
        String p = "src/test/resources/lipsum.txt";
        List<String> dl = Files.readAllLines(new File(p).toPath());
        String[] dict = dl.toArray(new String[dl.size()]);
        int l = 100;
        int i = 40;
        while (l <= dict.length) {
            gen(l, dict, i);
            i++;
            l += 100;
        }
    }

    private void gen(int s, String[] dict, int name) throws Exception {
        int ol = r.nextInt(1+s/2);
        int lo = r.nextInt(1 + s - ol*2);
        if (ol == 0 && (lo == 0 || lo == s)) {
            lo = r.nextInt(s-1)+1;
        }
        int ro = s - ol*2 - lo;
        int left = lo + ol;
        int right = ro + ol;
        shuf(dict);
        String[] ll = new String[left];
        String[] rl = new String[right];
        System.arraycopy(dict, 0, ll, 0, left);
        System.arraycopy(dict, lo, rl, 0, right);
        
        String[] lol = new String[lo];
        String[] oll = new String[ol];
        String[] rol = new String[ro];
        System.arraycopy(dict, 0, lol, 0, lo);
        System.arraycopy(dict, lo, oll, 0, ol);
        System.arraycopy(dict, lo+ol, rol, 0, ro);
        
        wrote(lol, name+"o.lo");
        wrote(oll, name+"o.ol");
        wrote(rol, name+"o.ro");

        shuf(ll);
        wrote(ll, name+"i.l");
        shuf(rl);
        wrote(rl, name+"i.r");
    }

    private void shuf(String[] arr) {
        int l = arr.length;
        for (int i = 0; i < l; i++) {
            int ri = r.nextInt(l);
            String t = arr[i];
            arr[i] = arr[ri];
            arr[ri] = t;
        }
    }

    private void wrote(String[] arr, String p) throws Exception {
        File f = new File("src/test/resources/ioset/" + p);
        f.createNewFile();
        if (arr.length == 0) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                bw.newLine();
                bw.write(arr[i]);
            }
            bw.flush();
        }
    }

}

