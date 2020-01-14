package utils;
import java.text.DecimalFormat;

public class EvaluationPrinter {
	public static final String pad = String.format("%1$-29s", " ");
	public static final String HEADER = pad + "|"
			+ centerPad("Precison") + "|" + centerPad("Recall") + "|"
			+ centerPad("FMesure") + "|\n";
	private String table = "";

	public EvaluationPrinter() {
		table = HEADER;
		addLine("-", new String[]{"-", "-", "-"}, "+", '-');
	}

	public void addLine(String c, double[] values) {
		String[] vs = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			vs[i] = new DecimalFormat("##.##").format(values[i]);
		}
		addLine(c, vs, "|", ' ');
	}
	
	public void addLine(String c, String[] values, String sep, char pad) {
		table += sep + String.format("%1$-28s", c).replaceAll(" ", pad+"");
		for (String v : values) {
			table += sep + centerPad(v, 10, pad);
		}
		table += sep + "\n";
	}
	
	public void print(){
		System.out.print(table);
		System.out.println(centerPad("-", 62, '-'));
	}

	public static String centerPad(String string) {
		return centerPad(string, 10, ' ');
	}

	public static String centerPad(String string, int length, char pad) {
		StringBuilder sb = new StringBuilder(length);
		sb.setLength((length - string.length()) / 2);
		sb.append(string);
		sb.setLength(length);
		return sb.toString().replace('\0', pad);
	}
}
