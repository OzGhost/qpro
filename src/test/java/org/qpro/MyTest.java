package org.qpro;

import org.junit.Test;
import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class MyTest {

    private static final Random r = new Random();

    public void another() throws Exception {
        String[] ioset = {"i01"};
        String base = "src/test/resources/ioset/i01.";
        String candi = base;
        List<String> lines = Files.readAllLines(new File(candi+"info").toPath());
        String[] tray = null;
        if (lines.size() != 2) {
            System.out.println("[o0] .info was broken :P");
            return;
        }
        tray = lines.get(0).split("\\s");
        int yl = Integer.parseInt(tray[0]);
        int ml = Integer.parseInt(tray[1]);
        String ans = lines.get(1);
        String[] your = null;
        String[] mine = null;
        lines = Files.readAllLines(new File(candi+"your").toPath());
        if (lines.size() != yl) {
            System.out.println("[o0] .your was broken :P");
            return;
        }
        your = lines.toArray(new String[lines.size()]);
        lines = Files.readAllLines(new File(candi+"mine").toPath());
        if (lines.size() != ml) {
            System.out.println("[o0] .mine was broken :p");
            return;
        }
        mine = lines.toArray(new String[lines.size()]);
        System.out.println("[o0] ready!!!");
    }

    @Test
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

        /*
        System.out.println(" " + lo + " " + ro + " " + ol);
        for (int i = 0; i < s-ol; i++) System.out.print(" " + dict[i]);
        System.out.println();
        for (int i = 0; i < ll.length; i++) System.out.print(" " + ll[i]);
        System.out.println();
        for (int i = 0; i < rl.length; i++) System.out.print(" " + rl[i]);
        System.out.println();
        System.out.println("--------------------");
        for (int i = 0; i < lol.length; i++) System.out.print(" " + lol[i]);
        System.out.println();
        for (int i = 0; i < rol.length; i++) System.out.print(" " + rol[i]);
        System.out.println();
        for (int i = 0; i < oll.length; i++) System.out.print(" " + oll[i]);
        System.out.println();
        */
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

