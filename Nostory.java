/*
 * Acest schelet citește datele de intrare și scrie răspunsul generat de voi,
 * astfel că e suficient să completați cele două metode.
 *
 * Scheletul este doar un punct de plecare, îl puteți modifica oricum doriți.
 *
 * Dacă păstrați scheletul, nu uitați să redenumiți clasa și fișierul.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.lang.model.util.ElementScanner6;
import javax.swing.text.AbstractDocument.LeafElement;

public class Nostory {
	public static void main(final String[] args) throws IOException {
		var scanner = new MyScanner(new FileReader("nostory.in"));

		var task = scanner.nextInt();
		var n = scanner.nextInt();
		var moves = task == 2 ? scanner.nextInt() : 0;

		var a = new int[n];
		for (var i = 0; i < n; i += 1) {
			a[i] = scanner.nextInt();
		}

		var b = new int[n];
		for (var i = 0; i < n; i += 1) {
			b[i] = scanner.nextInt();
		}

		try (var printer = new PrintStream("nostory.out")) {
			if (task == 1) {
				printer.println(solveTask1(a, b));
			} else {
				printer.println(solveTask2(a, b, moves));
			}
		}
	}

	private static long solveTask1(int[] a, int[] b) {
		//sortez crescator ambi vecotri
		Arrays.sort(a);
		Arrays.sort(b);
		long sum = 0;
		//daca exista un numar mai mare in vectorul b decat in a ,fac interschimbare
		//fac suma pentru vectorul a
		for (int i = 0; i < a.length; i++) {
			if (a[i] < b[a.length - 1 - i]) {
				int aux = a[i];
				a[i] = b[a.length - 1 - i];
				b[a.length - 1 - i] = aux;
			}
		}
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return  sum;
	}

	private static long solveTask2(int[] a, int[] b, int moves) {
		//pun cele mai mari numere de pe fiecare pozitie dintre  vecotri in vectorul a
		for (int i = 0; i < a.length; i++) {
			if (a[i] < b[i]) {
				int aux = a[i];
				a[i] = b[i];
				b[i] = aux;
			}
		}
		//sortez ambi vectori
		Arrays.sort(a);
		Arrays.sort(b);
		long sum = 0;
		//cat timp mai am mutari si in vecotorul b se afla un numar mai mare decat in vecotorul a,
		//fac interschimbare
		for (int i = 0 ; i < a.length; i++) {
			if (moves == 0) {
				break;
			}
			if (a[i] < b[a.length - 1 - i]) {
				int aux = a[i];
				a[i] = b[a.length - 1 - i];
				b[a.length - 1 - i] = aux;
				moves--;
			}
		}
		//fac suma vectorului a
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}
	/**
	 * A class for buffering read operations, inspired from here:
	 * https://pastebin.com/XGUjEyMN.
	 */
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
