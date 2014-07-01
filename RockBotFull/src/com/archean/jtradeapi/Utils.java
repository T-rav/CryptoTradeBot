/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jtradeapi;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static class Threads {
        public static abstract class CycledRunnable implements Runnable {
            public static final int STOP_CYCLE = -1;

            abstract protected int cycle() throws Exception;

            protected int onError(Exception e) {
                e.printStackTrace();
                return 0;
            }

            @Override
            public void run() {
                int sleepTime = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (sleepTime == STOP_CYCLE) break;
                        else if (sleepTime != 0) Thread.sleep(sleepTime);
                        sleepTime = cycle();
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        sleepTime = onError(e);
                    }
                }
            }
        }

        public static class UniqueHandlerObserver<T> {
            protected final Map<Integer, T> eventHandlers = new HashMap<>();

            public void addEventHandler(int id, T event) {
                synchronized (eventHandlers) {
                    eventHandlers.put(id, event);
                }
            }

            public void removeEventHandler(int id) {
                synchronized (eventHandlers) {
                    eventHandlers.remove(id);
                }
            }
        }
    }

    public static class Crypto {
        public static class Hashing {
            public static final String MD5 = "MD5";
            public static final String SHA1 = "SHA1";
            public static final String SHA256 = "SHA256";
            public static final String SHA384 = "SHA384";
            public static final String SHA512 = "SHA512";

            public static String bytesToHexString(byte[] bytes) {
                StringBuilder hash = new StringBuilder();
                for (byte b : bytes) {
                    String hex = Integer.toHexString(0xFF & b);
                    if (hex.length() == 1) {
                        hash.append('0');
                    }
                    hash.append(hex);
                }
                return hash.toString();
            }

            public static byte[] hmacByteDigest(byte[] dataBytes, byte[] keyBytes, String algo) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
                algo = "Hmac" + algo;
                SecretKeySpec key = new SecretKeySpec(keyBytes, algo);
                Mac mac = Mac.getInstance(algo);
                mac.init(key);
                return mac.doFinal(dataBytes);
            }

            public static String hmacDigest(String msg, String keyString, String algo) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
                return bytesToHexString(hmacByteDigest(msg.getBytes("ASCII"), keyString.getBytes("UTF-8"), algo));
            }
        }
    }

    public static class Strings {
        public static class DecimalFormatDescription {
            public DecimalFormat toDecimalFormat() {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(decimalSeparator);
                symbols.setGroupingSeparator(groupingSeparator);
                return new DecimalFormat(stringFormat, symbols);
            }

            public char decimalSeparator;
            public char groupingSeparator;
            public String stringFormat;

            public DecimalFormatDescription(String stringFormat, char decimalSeparator, char groupingSeparator) {
                this.decimalSeparator = decimalSeparator;
                this.groupingSeparator = groupingSeparator;
                this.stringFormat = stringFormat;
            }
        }

        // Constants:
        public final static DecimalFormatDescription percentDecimalFormat = new DecimalFormatDescription("######.##", '.', ',');
        public final static DecimalFormatDescription moneyFormat = new DecimalFormatDescription("############.########", '.', ','); // precision = 1 satoshi
        public final static DecimalFormatDescription moneyRepresentFormat = new DecimalFormatDescription("###,###,###,###.########", '.', ','); // with groupings
        public final static DecimalFormatDescription moneyRoughRepresentFormat = new DecimalFormatDescription("###,###,###,###.###", '.', ','); // not precise

        private static final Map<DecimalFormatDescription, DecimalFormat> decimalFormatMap = new HashMap<>(); // cached

        public static <T> String formatNumber(T value, DecimalFormatDescription format) { // custom format
            DecimalFormat df = decimalFormatMap.get(format);
            if (df == null) {
                df = format.toDecimalFormat();
                decimalFormatMap.put(format, df);
            }
            return df.format(value);
        }

        public static <T> String formatNumber(T value) { // json format
            return formatNumber(value, moneyFormat);
        }
    }

    public static class Serialization {
        public static void serializeObject(Object data, OutputStream outputStream) throws IOException {
            ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(outputStream));
            output.writeObject(data);
        }

        public static Object deSerializeObject(InputStream inputStream) throws IOException, ClassNotFoundException {
            ObjectInput input = new ObjectInputStream(new BufferedInputStream(inputStream));
            return input.readObject();
        }

    }
}
