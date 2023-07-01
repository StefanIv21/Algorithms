import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Supercomputer {
	static class Task {
		public static final String INPUT_FILE = "supercomputer.in";
		public static final String OUTPUT_FILE = "supercomputer.out";
		// n = numar de noduri, m = numar de muchii/arce
		int n, m;
		//stochez tipul nodului (1 sau 2)
		int[] cost;
		// adj[node] = lista de adiacenta a nodului node
		// exemplu: daca adj[node] = {..., neigh, ...} => exista arcul (node, neigh)
		HashSet<Integer> []adj;
		// in_degree[node] = gradul intern al nodului node
		//in_degree2[node] - copie
		int[] in_degree;
		int[] in_degree2;
		//retin numarul de contexte 
		int context = 0;
		int context2 = 0;
		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new FileReader(INPUT_FILE));
				n = scanner.nextInt();
				m = scanner.nextInt();
				cost = new int[n + 1];
				adj = new HashSet[n + 1];

				for (int node = 1; node <= n; node++) {
					adj[node] = new HashSet<>();
					cost[node] = scanner.nextInt();
				}

				in_degree = new int[n + 1];
				in_degree2 = new int[n + 1];

				for (int i = 1, x, y; i <= m; i++) {
					// arc(x,y)
					x = scanner.nextInt();
					y = scanner.nextInt();
					adj[x].add(y);
					++in_degree[y];
					++in_degree2[y];
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private void writeOutput(int write) {
			try {
				//scriu valoarea in fisier
				BufferedWriter in = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				in.write(String.valueOf(write));
				in.newLine();
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private int getResult() {
			//Declaram doua cozi ce vor contine nodurile care au cost 1, respectiv 2
			Queue<Integer> q1 = new LinkedList<>();
			Queue<Integer> q2 = new LinkedList<>();
			//copie la cozile de mai sus
			Queue<Integer> q3 = new LinkedList<>();
			Queue<Integer> q4 = new LinkedList<>();
			// Initial adaugam in coada toate nodurile cu grad intern 0 (fara dependente)
			//in functie de task ul fiecarui nod
			for (int node = 1; node <= n; node++) {
				if (in_degree[node] == 0) {
					if (cost[node] == 1) {
						q1.add(node);
						q3.add(node);
					} else {
						q2.add(node);
						q4.add(node);
					}
				}
			}
			//rulam de 2 ori algoritmul,o data cand incep cu nodurile care au task 1
			//a doua oara cand incep cu nodurile care au task 2
			solve_bfs(q1, q2, in_degree);
			context2 = context - 1;
			context = 0;
			solve_bfs(q4, q3, in_degree2);
			//verificam care a avut cel mai mic context-switch
			if (context - 1 < context2) {
				return context - 1;
			}
			return context2;
		}

		private void solve_bfs(Queue<Integer> q, Queue<Integer> q2, int[] in_degree) {
			//cat timp mai sunt noduri in cozi
			while (true) {
				//prelucrez pana cand coada devine goala
				if (!q.isEmpty()) {
					context++;
					while (!q.isEmpty()) {
						//Scot primul nod din coada si scad in_degree ul pentru fiecare nod vecin
						int node = q.poll();
						for (Integer neigh: adj[node]) {	
							--in_degree[neigh];
							//daca gasim un nod care nu mai are dependente
							//il adaug intr o coada,in functie de task pe care il are
							if (in_degree[neigh] == 0) {
								if (cost[neigh] == cost[node]) {
									q.add(neigh);
								} else {
									q2.add(neigh);
								}
							}
						}
					}
				}
				//prelucrez pana cand coada devine goala
				if (!q2.isEmpty()) {
					context++;
					while (!q2.isEmpty()) {
						//Scot primul nod din coada si scad in_degree ul pentru fiecare nod vecin
						int node = q2.poll();
						for (Integer neigh: adj[node]) {		
							--in_degree[neigh];
							if (in_degree[neigh] == 0) {
								//daca gasim un nod care nu mai are dependente
								//il adaug intr o coada,in functie de task pe care il are
								if (cost[neigh] == cost[node]) {
									q2.add(neigh);
								} else {
									q.add(neigh);
								}
							}
						}
					}
				}
				if (q.isEmpty()) {
					break;
				}
			}
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