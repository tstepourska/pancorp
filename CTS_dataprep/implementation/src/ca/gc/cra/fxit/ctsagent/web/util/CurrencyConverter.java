package ca.gc.cra.fxit.ctsagent.web.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.Locale;
import java.text.*;
import java.math.BigDecimal;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.opensymphony.xwork2.ActionContext;

/**
 * Performs locale-specific conversions for BigDecimal, Integer and Long
 * currency fields.  Negative values are permitted. 
 * 
 * Since incoming values can be parsed successfully whether or not 
 * they include thousands separators, fields that use this converter 
 * can also make use of the ActionSupport.getFormatted() method, so that the field
 * value will be redisplayed in a locale-specific way, e.g.
 * 
 *     <s:textfield name="dollarsAndCentsField" 
 *         value="%{getFormatted('format.currency_entry','dollarsAndCentsField')}" />  
 * 
 * where the format.currency_entry resource bundle value is
 * 
 *     format.currency_entry={0,number,#0.00} 
 */
public class CurrencyConverter extends StrutsTypeConverter {

    private Log log = LogFactory.getLog(getClass());
    
    // Allow "," as the thousands separator and "." as the decimal separator 
    static final String ENGLISH_DECIMAL_PATTERN="^[+-]?[0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{2})?$";
    
    // Allow space as the thousands separator and "," as the decimal separator
    static final String FRENCH_DECIMAL_PATTERN="^[+-]?[0-9]{1,3}(?: ?[0-9]{3})*(?:\\,[0-9]{2})?$";
    
    // Allow "," as the thousands separator and "." as the decimal separator 
    static final String ENGLISH_INTEGER_PATTERN="^[+-]?[0-9]{1,3}(?:,?[0-9]{3})*$";
    
    // Allow space as the thousands separator and "," as the decimal separator
    static final String FRENCH_INTEGER_PATTERN="^[+-]?[0-9]{1,3}(?: ?[0-9]{3})*$";
    
    /**
     * Converts a String to a BigDecimal, Integer or Long using a locale-specific 
     * DecimalFormat.
     */
    @SuppressWarnings("rawtypes")
	@Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        
        log.debug("convertFromString()");
        Object result = null;
        
        String englishPattern = null;
        String frenchPattern = null;
        
        if( BigDecimal.class.isAssignableFrom(toClass) ) {
            englishPattern = ENGLISH_DECIMAL_PATTERN;
            frenchPattern = FRENCH_DECIMAL_PATTERN;
        } else {
            if( Integer.class.isAssignableFrom(toClass) || Long.class.isAssignableFrom(toClass) ) {
                englishPattern = ENGLISH_INTEGER_PATTERN;
                frenchPattern = FRENCH_INTEGER_PATTERN;
            } else {
                throw new TypeConversionException(getClass() + " can only convert to BigDecimal, Integer or Long.");
            }
        }
        
        Locale locale = (Locale)context.get(ActionContext.LOCALE);
        log.debug("locale:  " + locale);
        
        if (locale == null) {
            locale = Locale.getDefault();
        }

        if( values != null ) {
            if( values.length != 1 ) {
                log.warn("values.length is " + values.length + ".  Failing.");
                throw new TypeConversionException(getClass() + " doesn't know how to handle multiple values.");
            }

            String s = values[0];

            // don't bother trying to parse the empty string.
            if( s.trim().equals("") ) {
                return null;
            }
            

            DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(locale);
            
            // If the entry field has passed through a format, it may 
            // contain non-breaking spaces instead of normal ones.
            if( locale.getLanguage().equals("fr") ) {
                s = s.replace('\u00a0', ' ');
            }
            
            // DecimalFormat is extremely lenient, so we will verify 
            // that the incoming string matches the appropriate locale-specific
            // regex for a dollars-and-cents value before passing it to
            // the format for parsing.
            //
            // For French users, we allow either the English or the French pattern
            // to be used.  
            if( locale.getLanguage().equals("fr") ) {
                if( s.matches(frenchPattern) ) {
                    // force normal space rather than non-breaking space as the thousands 
                    // separator for parsing. 
                    DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
                    dfs.setGroupingSeparator(' ');
                    df.setDecimalFormatSymbols(dfs);
                } else {
                    if( s.matches(englishPattern) ) {
                        df = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ENGLISH);
                    } else {
                        throw new TypeConversionException(getClass() + ":  [" + s + "]" + " failed regex pattern check");
                    }
                }
            } else {
                if( !s.matches(englishPattern) ) {
                    throw new TypeConversionException(getClass() + ":  [" + s + "]" + " failed regex pattern check");                
                }
            }

            BigDecimal bd = null;
            try {
                df.setParseBigDecimal(true);
                bd = (BigDecimal)df.parse(s);
            } catch( Exception ex ) {
                // this shouldn't happen if the input passed the regex check
                log.warn("DecimalFormat conversion failed", ex);
                throw new TypeConversionException(getClass() + ":  DecimalFormat conversion failed.");
            }

            if( BigDecimal.class.isAssignableFrom(toClass) ) {
                result = bd;
            } else {
                if( Long.class.isAssignableFrom(toClass) ) {
                    if( bd.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0 ) {
                        throw new TypeConversionException(getClass() + ":  [" + s + "]" + " too big for Long");
                    }
                    result = Long.valueOf(bd.longValue());
                } else {
                    if( bd.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0 ) {
                        throw new TypeConversionException(getClass() + ":  [" + s + "]" + " too big for Integer");
                    }
                    result = Integer.valueOf(bd.intValue());
                }
            }
            log.debug("result: " + result + "; " + result.getClass());
        }
        return result;
    }

    
    /**
     * Converts the specified object to a String.
     */
    @SuppressWarnings("rawtypes")
	@Override
    public String convertToString(Map context, Object o) {
        log.debug("convertToString(): " + o );
        if( o == null ) {
            return null;
        } 
        return o.toString();
    } 
}
