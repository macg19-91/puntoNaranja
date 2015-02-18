/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * Autor: Francisco Mahecha
 * Fecha: 19 de Febrero del 2011
 */
public class Hash {

    /*
     * Obtiene el resultado de la funcion de dispersion para
     * el usuario y la clave
     * Autor: Francisco Mahecha
     * Fecha: 19 de Febrero del 2011
     */
    public static String get(String user, String password) {
        try {
            Random rnd = new Random();
            String rndValue = Integer.toString(rnd.nextInt());
            return (get(get(rndValue) + get(user + password)) + get(rndValue)).toUpperCase();
        } catch (Exception ex) {
            return "";
        }
    }

    /*
     * Convierte un arreglo de bytes en String
     * Autor: Francisco Mahecha
     * Fecha: 19 de Febrero del 2011
     */
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString().toUpperCase();
    }

    /*
     * Obtiene el resultado de la fucion de dispersion (SHA-1)
     * Autor: Francisco Mahecha
     * Fecha: 19 de Febrero del 2011
     */
    public static String get(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
