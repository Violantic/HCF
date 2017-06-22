package me.borawski.hcf.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * Created by Ethan on 3/8/2017.
 */
public class UUIDUtil {

    private UUIDUtil() {
    }

    public static String strip(UUID uniqueId) {
        return uniqueId.toString().replace("-", "");
    }

    public static UUID cloth(String uniqueId) {
        long mostSigBits = new BigInteger(uniqueId.substring(0, 16), 16).longValue();
        long leastSigBits = new BigInteger(uniqueId.substring(16, 32), 16).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    public static String shorten(UUID uniqueId) {
        String shortened = new String(Base64.getEncoder().encode(toByteArray(uniqueId)));
        return shortened.substring(0, (shortened.length() - 2));
    }

    public static UUID amplify(String shortened) {
        if (shortened.isEmpty()) {
            return null;
        }

        return fromByteArray(Base64.getDecoder().decode(shortened));
    }

    private static byte[] toByteArray(UUID uniqueId) {
        byte[] bytes = new byte[(Long.SIZE / Byte.SIZE) * 2];
        LongBuffer longBuffer = ByteBuffer.wrap(bytes).asLongBuffer();

        longBuffer.put(new long[] { uniqueId.getMostSignificantBits(), uniqueId.getLeastSignificantBits() });
        return bytes;
    }

    private static UUID fromByteArray(byte[] bytes) {
        LongBuffer longBuffer = ByteBuffer.wrap(bytes).asLongBuffer();
        return new UUID(longBuffer.get(0), longBuffer.get(1));
    }

}
