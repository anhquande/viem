import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.anhquan.viem.core.util.Formatter;


public class TestFormatter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Locale currentLocale = Locale.GERMANY;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		String pattern = "###,###.00";
		DecimalFormat myFormatter = new DecimalFormat(pattern, symbols);
		int value  = 200;
		String output = myFormatter.format(value  );
		System.out.println(output);

	}

}
