package service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

@Service
public class ArrService {

    private Map<String, Object> results;

    public Optional<Map<String, Object>> arrManipulationResults() throws IOException
            , ClassNotFoundException {
        String resultsDelimiter = "-------------------------------------------------------------------------------------------------------------------------------------------------";
        results = new LinkedHashMap<>();
        int[] randomIntArr = getRandomArrInt();
        results.put("-", "array of 200 natural numbers, ranging from 0 to 1000");
        results.put("random array", Arrays.toString(randomIntArr));
        results.put(resultsDelimiter+"-", resultsDelimiter);
        byte[] bytes = arrSerialization(randomIntArr);
        results.put("serialized array", bytes);
        results.put("line length", bytes.length);
        results.put(resultsDelimiter+"--", resultsDelimiter);
        byte[] compress = compress(bytes);
        results.put("compress line", compress);
        results.put("compress line length", compress.length);
        results.put("compression ratio ", (1.0f * bytes.length/compress.length));
        results.put("string compression mechanism ", "Deflater.BEST_COMPRESSION");
        results.put(resultsDelimiter+"---", resultsDelimiter);
        byte[] uncompress = uncompress(compress);
        results.put("uncompress line", uncompress);
        results.put("uncompress line length", uncompress.length);
        results.put(resultsDelimiter+"----", resultsDelimiter);
        int[] ints = arrDeserialization(uncompress);
        results.put("recovered array", Arrays.toString(ints));
        return Optional.of(results);
    }

    private int[] getRandomArrInt() {
        int[] arr = new int[200];
        for(int i = 0; i < arr.length; i++){
            arr[i] = ThreadLocalRandom.current().nextInt( 1000);
        }
        return arr;
    }

    private byte[] arrSerialization(int[] arrInt) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(arrInt);
        oos.close();
        return byteArrayOutputStream.toByteArray();
    }

    private int[] arrDeserialization(byte[] data) throws IOException,
            ClassNotFoundException,
            ClassCastException {
        ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayOutputStream);
        int[] result = (int[])objectInputStream.readObject();
        objectInputStream.close();
        return result;
    }

    private byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream
                , deflater)){
            deflaterOutputStream.write(data);
        }
        finally {
            deflater.finish();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] uncompress (byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InflaterOutputStream inflaterOutputStream = new InflaterOutputStream(byteArrayOutputStream)){
            inflaterOutputStream.write(data);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
