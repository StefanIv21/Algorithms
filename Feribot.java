import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.lang.model.util.ElementScanner6;
import javax.swing.text.AbstractDocument.LeafElement;

public class Feribot {
	public static void main(final String[] args) throws IOException {
		var scanner = new MyScanner(new FileReader("feribot.in"));

		var n = scanner.nextInt();
		var ships = scanner.nextInt();

		var a = new long[n];
		for (var i = 0; i < n; i += 1) {
			a[i] = scanner.nextLong();
		}


		try (var printer = new PrintStream("feribot.out")) {
			printer.println(solveTask1(a,ships));
		}
	}
	//cautare binara intre maximul greutatii unei masini si suma lor
	private static long solveTask1(long[] a,int ships) {
		long sum = 0;
		long max_elem = 0;
		long mijloc;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
			if (a[i] > max_elem) {
				max_elem = a[i]; 
			}			
		}
		while (max_elem <= sum) {
			mijloc = (max_elem + sum) / 2 ;
			int nr_ships = verifica(mijloc,a,ships);
			if (nr_ships <= ships) {
				sum = mijloc - 1;
			} else {
				max_elem = mijloc + 1;
			}
		}
		return max_elem;
	}
	//functie ce ma ajuta sa verific daca masinile intra in feriboturi cu greutatea data
	private static int verifica(long greutate,long[] a,int ships) {
		int nr_ships = 1;
		long greutate_ship = 0;
		for (int i = 0; i < a.length; i++) {
			greutate_ship += a[i];
			if (greutate_ship > greutate) {
				nr_ships++;
				if (nr_ships > ships) {
					return nr_ships;
				}
				greutate_ship = a[i];
			}
		}
		return nr_ships;
	}

	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
}
