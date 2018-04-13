package ru.track.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.track.io.vendor.Bootstrapper;
import ru.track.io.vendor.FileEncoder;
import ru.track.io.vendor.ReferenceTaskImplementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public final class TaskImplementation implements FileEncoder {

    /**
     * @param finPath  where to read binary data from
     * @param foutPath where to write encoded data. if null, please create and use temporary file.
     * @return file to read encoded data from
     * @throws IOException is case of input/output errors
     */
    @NotNull
    public File encodeFile(@NotNull String finPath, @Nullable String foutPath) throws IOException {
        FileInputStream stream = new FileInputStream(new File(finPath));
        FileWriter fileWriter;

        if(foutPath == null)
            fileWriter = new FileWriter("/home/ananas/java/1.txt");
        else
            fileWriter = new FileWriter(foutPath);
        byte[] buf = new byte[stream.available()];
        stream.read(buf, 0, stream.available());
        for(int i=0; i<buf.length;i++){
            if((i + 1) % 3 == 0){
                int[] parsed = parse(makeBinaryArray(new byte[]{buf[i-2],buf[i-1],buf[i]}));
                for (int j = 0; j < 4; j++) {
                    fileWriter.write(toBase64[parsed[j]]);
                }
            }
        }
        if(buf.length % 3 == 2){
            int [] parsed = parse(makeBinaryArray(new byte[]{buf[buf.length - 2], buf[buf.length - 1], 0}));
            for (int i = 0; i < 3; i++) {
                fileWriter.write(toBase64[parsed[i]]);
            }
            fileWriter.write("=");
        }

        if(buf.length % 3 == 1){
            int [] parsed = parse(makeBinaryArray(new byte[]{buf[buf.length-1], 0, 0}));
            for (int i = 0; i < 2; i++) {
                fileWriter.write(toBase64[parsed[i]]);
            }
            fileWriter.write("==");
        }

        fileWriter.close();
        stream.close();
        return new File("/home/ananas/java/1.txt");
    }

    public ArrayList<Integer> toBinaryArray(byte b){
        ArrayList<Integer> result = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            if((b & (1 << (7 - i))) == 0)
                result.add(0);
            else
                result.add(1);
        }
        return result;
    }

    private int[] parse(ArrayList<Integer> list){
        int[] res = new int[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                res[i] += list.get(i * 6 + j) << (5 - j);
            }
        }
        return res;
    }

    private ArrayList<Integer> makeBinaryArray(byte[] b){
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(toBinaryArray(b[0]));
        list.addAll(toBinaryArray(b[1]));
        list.addAll(toBinaryArray(b[2]));
        return list;
    }
    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static void main(String[] args) throws Exception {
        final FileEncoder encoder = new ReferenceTaskImplementation();
        // NOTE: open http://localhost:9000/ in your web browser
        (new Bootstrapper(args, encoder))
                .bootstrap("", new InetSocketAddress("127.0.0.1", 9000));
    }

}
