import java.io.InputStream;
import java.io.IOException;

public class FastScanner {
    private final InputStream in = System.in;
    private final byte[] buffer = new byte[1 << 16];
    private int ptr = 0;
    private int len = 0;

    private int read() throws IOException {
        if (ptr >= len) {
            len = in.read(buffer);
            ptr = 0;
            if (len <= 0)
                return -1;
        }
        return buffer[ptr++];
    }

    int nextInt() throws IOException {
        int c;
        do {
            c = read();
        } while (c <= ' ' && c != -1);

        if (c == -1)
            return Integer.MIN_VALUE;

        int sign = 1;
        if (c == '-') {
            sign = -1;
            c = read();
        }

        int value = 0;
        while (c > ' ') {
            value = value * 10 + (c - '0');
            c = read();
        }
        return value * sign;
    }
}
