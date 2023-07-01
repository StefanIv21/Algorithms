import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Teleportare {
	static class Task {
		public static final String INPUT_FILE = "teleportare.in";
		public static final String OUTPUT_FILE = "teleportare.out";
		private static final Integer INF = Integer.MAX_VALUE;
		// n = numar de noduri, m = numar de muchii
		long n, m;
		long k;
		// nodul sursa
		int source;
		// structura folosita pentru a stoca distanta, cat si vectorul de parinti
		// folosind algoritmul Dijkstra
		public class DijkstraResult {
			List<Integer> d;
			List<Integer> p;
			DijkstraResult(List<Integer> _d, List<Integer> _p) {
				d = _d;
				p = _p;
			}
		};

		public class Pair implements Comparable<Pair> {
			public long destination;
			public long cost;
			Pair(long _destination, long _cost) {
				destination = _destination;
				cost = _cost;
			}

			public int compareTo(Pair rhs) {
				return Long.compare(cost, rhs.cost);
			}
		}

		public class Pair2 implements Comparable<Pair2> {
			public int destination;
			public long cost;
			public int timp;
			Pair2(int _destination, long _cost, int _timp) {
				destination = _destination;
				cost = _cost;
				timp = _timp;
			}

			public int compareTo(Pair2 rhs) {
				return Long.compare(cost, rhs.cost);
			}
		}

		// adj[node] = lista de adiacenta a nodului node
		// perechea (neigh, w) semnifica arc de la node la neigh de cost w
		ArrayList<Pair> []adj;
		//adj2 -stochez muchiile de teleportare
		ArrayList<Pair> []adj2;
		//retin toate perioadele de la muchiile de teleportare
		Queue<Long> q1 = new LinkedList<>();

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
				n = sc.nextLong();
				m = sc.nextLong();
				k = sc.nextLong();
				adj = new ArrayList[(int) n + 1];
				adj2 = new ArrayList[(int) n + 1];
				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
					adj2[i] = new ArrayList<>();
				}

				for (int i = 1; i <= m; i++) {
					long x, y;
					long w;
					x = sc.nextLong();
					y = sc.nextLong();
					w = sc.nextLong();
					adj[(int) x].add(new Pair(y, w));
					adj[(int) y].add(new Pair(x, w));
				}

				for (int i = 1; i <= k; i++) {
					long x, y, w;
					x = sc.nextLong();
					y = sc.nextLong();
					w = sc.nextLong();
					q1.add(w);
					adj2[(int) x].add(new Pair(y, w));
					adj2[(int) y].add(new Pair(x, w));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				BufferedWriter in = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				in .write(String.valueOf(result));
				in .newLine();
				in .close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		//calculam cmmmc pentru toate numerele din coada
		private long cmmmc(Queue<Long> q1) {
			long l1 = q1.poll();
			while (!q1.isEmpty()) {
				l1 = calculatecmmc(l1, q1.poll());
			}
			return l1;
		}

		private long calculatecmmc(long a, long b) {
			long gcd = 1;
			for (int i = 1; i <= a && i <= b; ++i) {
				if (a % i == 0 && b % i == 0) {
					gcd = i;
				}
			}
			return (a * b) / gcd;
		}

		private long getResult() {
			return dijkstra(1, n, adj);
		}

		private long dijkstra(int source, long nodes, ArrayList<Pair> []adj) {
			//calculam cmmmc
			long val_cmmmc = nodes;
			if (!q1.isEmpty()) {
				val_cmmmc = cmmmc(q1);
			}

			//folosim o matrice pentru a retine ,pentru fiecare nod,
			//timpi in care se poate ajunge la nod
			long[][] d = new long[(int) nodes + 1][(int) val_cmmmc + 1];
			// Initializam distantele la infinit
			for (int node = 0; node <= nodes; node++) {
				for (int i = 0; i <= val_cmmmc; i++) {
					d[node][i] = INF;
				}
			}

			// Folosim un priority queue de Pair, desi elementele noastre nu sunt tocmai
			// muchii.
			// Vom folosi field-ul de cost ca si distanta pana la nodul respectiv.
			// Observati ca am modificat clasa Pair ca sa implementeze interfata Comparable.
			PriorityQueue<Pair2> pq = new PriorityQueue<>();
			//Inseram nodul de plecare in pq si ii setam distanta la 0, cu timpul 0.
			d[source][0] = 0;
			pq.add(new Pair2(source, 0, 0));
			// Cat timp inca avem noduri de procesat
			while (!pq.isEmpty()) {
				//Scoatem head-ul cozii
				long cost = pq.peek().cost;
				int timp = pq.peek().timp;
				int node = pq.poll().destination;
				//In cazul in care un nod e introdus in coada cu mai multe distante (pentru ca
				//distanta pana la el se imbunatateste in timp), vrem sa procesam doar
				//versiunea sa cu distanta minima. Astfel, dam discard la intrarile din coada
				//care nu au distanta optima.
				if (cost > d[node][timp]) {
					continue;
				}

				//Ii parcurgem toti vecinii.
				for (var e: adj[node]) {
					int neigh = (int) e.destination;
					long w = e.cost;
					//Se imbunatateste distanta pentru timpul respectiv?
					if (d[node][timp] + w < d[neigh][(int)((d[node][timp] + w) % val_cmmmc)]) {
						//Actualizam distanta.
						d[neigh][(int)((d[node][timp] + w) % val_cmmmc)] = d[node][timp] + w;
						pq.add(new Pair2(neigh, d[neigh][(int)((d[node][timp] + w) % val_cmmmc)],
							((int)((d[node][timp] + w) % val_cmmmc))));
					}
				}

				//parcugem muchiile care sunt de teleportare
				for (var e: adj2[node]) {
					int neigh = (int) e.destination;
					long w = e.cost;
					//Vedem daca timpul in care s a ajuns la nod
					//este divizibil cu perioada muchei
					if (d[node][timp] % w == 0) {
						
						//Se imbunatateste distanta pentru timpul respectiv?	
						if (d[node][timp] + 1 < d[neigh][(int)((d[node][timp] + 1) % val_cmmmc)]) {
							//Actualizam distanta.
							d[neigh][(int)((d[node][timp] + 1) % val_cmmmc)] = d[node][timp] + 1;
							pq.add(new Pair2(neigh, d[neigh]
								[(int)((d[node][timp] + 1) % val_cmmmc)],
									((int)((d[node][timp] + 1) % val_cmmmc))));
						}
					}
				}
			}

			//Extragem cel mai mic timp la care s a ajuns la nod
			long min = Long.MAX_VALUE;
			for (int i = 0; i <= val_cmmmc; i++) {
				
				if (min > d[(int) nodes][i]) {
					min = d[(int) nodes][i];
				}
			}

			return min;
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