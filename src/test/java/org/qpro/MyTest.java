package org.qpro;

import org.junit.Test;
import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class MyTest {

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

    public void firstCase() throws Exception {
        String p = "src/test/resources/lipsum.txt";
        String content = Files.readString(new File(p).toPath());
        content = content.replace('\n', ' ');
        Set<String> uniq = new HashSet<>();
        int c = 0;
        for (String i: content.split("[^a-zA-Z]")) {
            String j = i.trim().toLowerCase();
            if ( ! j.isEmpty()) {
                uniq.add(j);
                c++;
            }
        }
        String[] dict = uniq.toArray(new String[uniq.size()]);
        Random r = new Random();
        int z = 20;
        int hz = z*3/10;
        int n = r.nextInt(z-hz)+hz;
        int m = r.nextInt(z-hz)+hz;
        int l = n+m-r.nextInt(Math.min(n,m));
        int nr = n;
        int mr = m;
        if (l > dict.length) {
            z = Math.min(nr, dict.length);
            hz = z*3/10;
            n = r.nextInt(z-hz)+hz;
            z = Math.min(mr, dict.length);
            hz = z*3/10;
            m = r.nextInt(z-hz)+hz;
            l = Math.min(n+m-r.nextInt(Math.min(n,m)), dict.length);
        }
        String tmp = null;
        int ti = 0;
        for (int i = 0; i < l; i++) {
            ti = r.nextInt(dict.length);
            tmp = dict[i];
            dict[i] = dict[ti];
            dict[ti] = tmp;
        }
        String[] nc = new String[nr];
        String[] mc = new String[mr];
        System.arraycopy(dict, 0, nc, 0, n);
        System.arraycopy(dict, l-m, mc, 0, m);
        int buf = n+m-l;
        String ans = (n-buf) + " " + (m-buf) + " " + buf;
        for (int i = n; i < nr; i++) {
            nc[i] = nc[r.nextInt(n)];
        }
        for (int i = m; i < mr; i++) {
            mc[i] = mc[r.nextInt(m)];
        }
        for (int i = 0; i < nr; i++) {
            ti = r.nextInt(nr);
            tmp = nc[i];
            nc[i] = nc[ti];
            nc[ti] = tmp;
        }
        for (int i = 0; i < mr; i++) {
            ti = r.nextInt(mr);
            tmp = mc[i];
            mc[i] = mc[ti];
            mc[ti] = tmp;
        }
        // under construction
        /*
        System.out.println("[o0] dict: " + l);
        for (int i = 0; i < l; i++) {
            System.out.print(" " + dict[i]);
        }
        System.out.println("\n[o0] nr:" + nr);
        for (int i = 0; i < nr; i++) {
            System.out.print(" " + nc[i]);
        }
        System.out.println("\n[o0] m:" + mr);
        for (int i = 0; i < mr; i++) {
            System.out.print(" " + mc[i]);
        }
        System.out.println();
        */
        System.out.println(nr + " " + mr);
        System.out.println(ans);
        p = "src/test/resources/ioset/";
        String now = ""+new Date().getTime();
        try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(p + now + ".info")
            )
        ) {
            writer.write(nr + " " + mr + "\n");
            writer.write(ans + "\n");
            writer.flush();
        }
        try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(p + now + ".your")
            )
        ) {
            for (String i: nc) {
                writer.write(i + "\n");
            }
            writer.flush();
        }
        try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(p + now + ".mine")
            )
        ) {
            for (String i: mc) {
                writer.write(i + "\n");
            }
            writer.flush();
        }
    }

}

