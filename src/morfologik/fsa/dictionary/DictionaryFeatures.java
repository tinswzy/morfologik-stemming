package morfologik.fsa.dictionary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

/**
 * Description of attributes, their types and default values.
 * 
 * @see Dictionary
 * 
 * @author Dawid Weiss
 */
public final class DictionaryFeatures {
    /**
     * Attribute name for {@link #separator}.
     * 
     * @see #fromMap(Properties)
     */
    public final static String ATTR_NAME_SEPARATOR = "fsa.dict.separator";

    /**
     * Attribute name for {@link #encoding}.
     * 
     * @see #fromMap(Properties)
     */
    public final static String ATTR_NAME_ENCODING = "fsa.dict.encoding";

    /**
     * Attribute name for {@link #usesPrefixes}.
     * 
     * @see #fromMap(Properties)
     */
    public final static String ATTR_NAME_USES_PREFIXES = "fsa.dict.uses-prefixes";

    /**
     * Attribute name for {@link #usesInfixes}.
     * 
     * @see #fromMap(Properties)
     */
    public final static String ATTR_NAME_USES_INFIXES = "fsa.dict.uses-infixes";

    /** 
     * A separator character between fields (stem, lemma, form). The character
     * must be within byte range (FSA uses bytes internally).
     */
    final byte separator;

    /** 
     * Encoding used for converting bytes to characters and vice versa.
     */
    final String encoding;

    /** 
     * True if the dictionary was compiled with prefix compression. 
     */
    final boolean usesPrefixes;

    /** 
     * True if the dictionary was compiled with infix compression. 
     */
    final boolean usesInfixes;

    /**
     * Creates an immutable instance of {@link DictionaryFeatures}. 
     */
    public DictionaryFeatures(
            char separator, String encoding, boolean usesPrefixes,
            boolean usesInfixes) {
        this.encoding = encoding;
        this.usesPrefixes = usesPrefixes;
        this.usesInfixes = usesInfixes;
        
        try {
            final byte [] separatorBytes = new String(new char [] {separator}).getBytes(encoding);
            if (separatorBytes.length != 1) {
                throw new RuntimeException("Separator character: " + separator
                        + " must be a single byte after transformation with encoding " + encoding);
            }
            this.separator = separatorBytes[0];
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported on this VM: " + encoding);
        }
    }

    /**
     * Converts attributes in a {@link Map} to an instance of {@link Dictionary},
     * validating attribute values. 
     */
    static DictionaryFeatures fromMap(Properties properties)
        throws IOException
    {
        final String separator = properties.getProperty(ATTR_NAME_SEPARATOR);
        if (separator == null || separator.length() != 1) {
            throw new IOException("Attribute " + ATTR_NAME_SEPARATOR + " must be "
                    + "a single character.");
        }

        final String encoding = properties.getProperty(ATTR_NAME_ENCODING);
        if (encoding == null || encoding.length() == 0) {
            throw new IOException("Attribute " + ATTR_NAME_ENCODING + " must be "
                    + "present and non-empty.");
        }

        final boolean usesPrefixes = Boolean.valueOf(
                properties.getProperty(ATTR_NAME_USES_PREFIXES, "false")).booleanValue();

        final boolean usesInfixes = Boolean.valueOf(
                properties.getProperty(ATTR_NAME_USES_INFIXES, "false")).booleanValue();
        
        return new DictionaryFeatures(
                separator.charAt(0), encoding, usesPrefixes, usesInfixes);
    }
}
