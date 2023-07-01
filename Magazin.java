import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Magazin {
	static class Task {
		public static final String INPUT_FILE = "magazin.in";
		public static final String OUTPUT_FILE = "magazin.out";
		// numarul maxim de noduri
		// n = numar de noduri, m = numar de muchii/arce
		int n, m;
		//timpul de gasire a nodului
		int[] found;
		//timpul de finalizare a parcurgerii  nodului 
		int[] end;
		//inversul lui found(pentru fiecare timp (index) retin nodul)
		int[] found2;
		int timestamp = 0;
		// adj[node] = lista de adiacenta a nodului node
		// exemplu: daca adj[node] = {..., neigh, ...} => exista arcul (node, neigh)
		ArrayList<Integer>[] adj;

		public void solve() {
			readInput();
		}
		private void readInput() {
			try {
				MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
				n = sc.nextInt();
				m = sc.nextInt();
				found2 = new int[n + 1];
				found = new int[n + 1];
				end = new int[n + 1];
				adj = new ArrayList[n + 1];

				for (int node = 1; node <= n; node++) {
					adj[node] = new ArrayList<>();
				}
				for (int i = 2, x; i <= n; i++) {
					x = sc.nextInt();
					adj[x].add(i);
				}
				solveDfs();
				try {
					BufferedWriter in = new BufferedWriter(new FileWriter(OUTPUT_FILE));
					for (int i = 1, x, y; i <= m; i++) {
						x = sc.nextInt();
						y = sc.nextInt();
						int nr = -1;
						//daca timpul de gasire plus perioada adaugata este mai mare decat timpul
						//de gasire a ultimului nod ,atunci afisez -1 
						if (found[x] + y <= n) {
							//daca timpul de finalizare a nodului primit este mai mare decat
							//timpul de finalizare a nodului format din
							//(timpul de gasire a nodului primit si timpul dat ),
							//atunci afisez timpul de gasire a nodului format
							if (end[x] >= end[found2[found[x] + y]]) {
								nr = found2[found[x] + y];
							}
						}
						in.write(String.valueOf(nr));
						in.newLine();
					}
					in.newLine();
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private void solveDfs() {
			boolean[] used = new boolean[n + 1];
			//pentru fiecare nod
			for (int node = 1; node <= n; node++) {
				//daca nodul nu a fost vizitat,pornim o parcurgere dfs
				if (!used[node]) {
					dfs(node, used);
				}
			}
		}
		//porneste o parcurgere dfs
		//retin timpul de gasire si finalizare
		void dfs(int node, boolean[] used) {
			used[node] = true;
			found[node] = ++timestamp;
			found2[timestamp] = node;
			for (Integer neigh: adj[node]) {
				if (!used[neigh]) {
					dfs(neigh, used);
				}
			}
			end[node] = timestamp;
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
class MyScanner {
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