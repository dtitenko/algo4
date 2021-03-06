import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int[] index = null;

    private static final int CUTOFF = 15;

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return index[i];
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        index = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = i;
        }
        sort(s, 0, s.length() - 1, 0);
    }

    private char charAt(String s, int suffix, int offset) {
        return s.charAt((suffix + offset) % s.length());
    }

    private void sort(String s, int lo, int hi, int offset) {
        if (hi - lo <= CUTOFF) {
            insertion(s, lo, hi, offset);
            return;
        }
        int lt = lo, gt = hi, piv = charAt(s, index[lo], offset), eq = lo + 1;
        while (eq <= gt) {
            int t = charAt(s, index[eq], offset);
            if (t < piv)
                exch(lt++, eq++);
            else if (t > piv)
                exch(eq, gt--);
            else
                eq++;
        }
        sort(s, lo, lt - 1, offset);
        if (piv >= 0)
            sort(s, lt, gt, offset + 1);
        sort(s, gt + 1, hi, offset);
    }

    private void exch(int i, int j) {
        int tmp = index[i];
        index[i] = index[j];
        index[j] = tmp;
    }

    private void insertion(String s, int lo, int hi, int offset) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, j, j - 1, offset); j--)
                exch(j, j - 1);
    }

    // Is suffix i less than suffix j, starting at offset
    private boolean less(String s, int i, int j, int offset) {
        int oi = index[i], oj = index[j];
        for (; offset < index.length; offset++) {
            int ival = charAt(s, oi, offset), jval = charAt(s, oj, offset);
            if (ival < jval)
                return true;
            else if (ival > jval)
                return false;
        }
        return false;
    }

    public static void main(String[] args) {
        int SCREEN_WIDTH = 80;
        String s = BinaryStdIn.readString();
        int n = s.length();
        int digits = (int) Math.log10(n) + 1;
        String fmt = "%" + (digits == 0 ? 1 : digits) + "d ";
        StdOut.printf("String length: %d\n", n);
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            StdOut.printf(fmt, i);
            for (int j = 0; j < (SCREEN_WIDTH - digits - 1) && j < n; j++) {
                char c = s.charAt((j + csa.index(i)) % n);
                if (c == '\n')
                    c = ' ';
                StdOut.print(c);
            }
            StdOut.println();
        }
    }
}