import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Badgpt {
	public static void main(final String[] args) throws IOException {
		var scanner = new MyScanner(new FileReader("badgpt.in"));
		var n = scanner.nextLine();
		try (var printer = new PrintStream("badgpt.out")) {
			printer.println(solveTask1(n));
		}
	}

	private static  long solveTask1(String  n) {
		long  result = 1;
		long  M = 1000000007;
		//in variabila numere pun numerele extrase dupa literele n si u
		long [] numere = new long [n.length()];
		int k = 0;
		//parcurg tot sirul dat
		//daca gasesc caracterul u sau n,citesc din sir pana cand dau de o litera,
		//cifrele gasite le concatenez in sirul s
		for (int i = 0;i < n.length(); i++) {
			String s = "";
			char a = n.charAt(i);
			if (Character.compare(a, 'u') == 0  || Character.compare(a, 'n') == 0) {
				int j = 0;
				j = i;
				j++;
				while (j <= n.length()) {
					if (n.charAt(j) == '0' || n.charAt(j) == '1' || n.charAt(j) == '2'
						|| n.charAt(j) == '3' || n.charAt(j) == '4' || n.charAt(j) == '5'
						|| n.charAt(j) == '6' || n.charAt(j) == '7' || n.charAt(j) == '8'
						|| n.charAt(j) == '9') {
						s += n.charAt(j);
						j++;
						if (j == n.length()) {
							break;
						}
					} else {
						break;
					}
				}
				//aduag la numarul gasit un 1, deoarece numarul de posibilitati a sirului
				//in functie de numarul dat este fibonacci de numarul respecitiv +1
				long num = Long.parseLong(s);
				numere[k] = (num + 1);
				k++;
			}
		}
		//daca nu am gasit nici un numar
		if (k == 0) {
			return 1;
		}
		for (int i = 0;i < k; i++) {
			//pentru fiecare numar din vector,calculez fibonacci de numarul respeciv
			//folosind algorithmul cu matrice,pentru timp in (log N)
			//matricea initiala pentru algorithmul lui fibonacci
			long[][] F = new long [2][2];
			F[0][0] = 1;
			F[0][1] = 1;
			F[1][0] = 1;
			F[1][1] = 0;
			//am folosit functiile din laborator pentru a calcula ridicarea unei matrice eficient
			power_matrix(F, numere[i] - 1);
			long fib = (F[0][0] % M) % M;
			result = (result * fib) % M;
		
		}
		return result;
	}

	static void power_matrix(long[][] a,long p) {
		// tmp = I (matricea identitate)
		long[][] tmp = new long [2][2];
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 2; ++j) {
				tmp[i][j] = (i == j) ? 1 : 0;
			}
		}
		while (p != 1) {
			if  (p % 2 == 0) {
				multiply_matrix(a,a);     // a = a*a
				p /= 2;                       // rămâne de calculat A^(p/2)
			} else {
				// reduc la cazul anterior:
				multiply_matrix(tmp,a); // tmp = tmp*a
				--p;                          // rămâne de calculat A^(p-1)
			}
		}
		multiply_matrix(a, tmp);
		// avem o parte din rezultat în a și o parte în tmp
		// a = a * C
	}

	static void multiply_matrix(long[][] a, long[][] b) {
		long  M = 1000000007;
		long[][] c = new long [2][2];
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 2; ++j) {
				for (int k = 0; k < 2; ++k) {
					c[i][j] = (c[i][j] % M  + (a[i][k] * b[k][j]) % M) % M;
				}
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				a[i][j] = c[i][j];
			}
		}
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
