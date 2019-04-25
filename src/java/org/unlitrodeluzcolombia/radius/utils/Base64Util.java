package org.unlitrodeluzcolombia.radius.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 25, 2019
 */
public final class Base64Util {

    /**
     * Convertir archivo a Base64
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String encodeBase64(final File file) throws IOException {
        final byte[] bytes = readFileBytes(file);
        final byte[] encoded = Base64.encodeBase64(bytes);
        final String base64 = new String(encoded);

        return base64;
    }

    /**
     * Convertir Base64 a foto
     *
     * @param outPathname
     * @param encoded
     * @return
     * @throws IOException
     */
    public File decodeBase64(final String outPathname, final String encoded) 
            throws IOException {
        final byte[] decoded = Base64.decodeBase64(encoded);
        final File file = writeFileBytes(outPathname, decoded);

        return file;
    }

    /**
     * De archivo a bytes para base64
     *
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] readFileBytes(final File file) throws IOException {
        final long length = file.length();
        final String name = file.getName();

        if (length > Integer.MAX_VALUE) {
            throw new IOException("Archivo con tamaño mayor al soportado: " + name);
        }

        try (InputStream in = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int read = 0;

            while ((offset < bytes.length) && (read = in.read(bytes, offset, bytes.length - offset)) != -1) {
                offset += read;
            }

            if (offset < bytes.length) {
                throw new IOException("No se pudo completar la lectura del archivo: " + name);
            }

            return bytes;
        }
    }

    /**
     * De bytes a foto.
     *
     * @param outPathname
     * @param bytes
     * @return
     * @throws IOException
     */
    private File writeFileBytes(final String outPathname, final byte[] bytes) throws IOException {
        final File file = new File(outPathname);

        try (OutputStream os = new FileOutputStream(file);
                OutputStream out = new BufferedOutputStream(os)) {
            out.write(bytes);
            out.flush();

            return file;
        }
    }

}
