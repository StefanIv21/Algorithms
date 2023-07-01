import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Ferate {
	static class Task {
		public static final String INPUT_FILE = "ferate.in";
		public static final String OUTPUT_FILE = "ferate.out";
		// n = numar de noduri, m = numar de muchii/arce
		//retin care este gara
		//nr reprezinta numarul de drumuri create
		int n, m;
		int gara;
		int nr = 0;
		// adj[node] = lista de adiacenta a nodului node
		// exemplu: daca adj[node] = {..., neigh, ...} => exista arcul (node, neigh)
		//adj2[nde] =lista de adiacenta a nodului node
		//retin nodurile care au muchie la nodul node (inversul lui adj[node])
		ArrayList<Integer> []adj;
		ArrayList<Integer> []adj2;
		//numarul de noduri care intra in nod
		int[] in_degree;
		//parent[node] = parent of node in the DFS traversal
		int[] parent;
		//found[node] = the timestamp when node was found (when started to visit its subtree)
		//Note: The global timestamp is incremented everytime a node is found.
		int[] found;
		int[] low_link;
		//the minimum accessible timestamp that node can see/access
		//low_link[node] = min { found[x] | x is node OR x in ancestors(node) 
		//OR x in descendants(node) };
		//visiting stack: nodes are pushed into the stack in visiting order
		Stack<Integer> nodes_stack = new Stack<>();
		boolean[] in_stack;
		//in_stack[node] = true, if node is in stack
		//false, otherwise
		int timestamp;
		public void solve() {
			readInput();
			getResult();
		}
		private void readInput() {
			try {
				MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
				n = sc.nextInt();
				m = sc.nextInt();
				gara = sc.nextInt();
				adj = new ArrayList[n + 1];
				adj2 = new ArrayList[n + 1];
				in_degree = new int[n + 1];

				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
					adj2[i] = new ArrayList<>();
				}
				for (int i = 1, x, y; i <= m; i++) {
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
					++in_degree[y];
					adj2[y].add(x);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private void getResult() {
			tarjan_scc();
			try {
				BufferedWriter in = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				in.write(String.valueOf(nr));
				in.newLine();
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private void tarjan_scc() {
			// STEP 1: initialize results
			parent = new int[n + 1];
			found = new int[n + 1];
			low_link = new int[n + 1];
			in_stack = new boolean[n + 1];

			for (int i = 1; i <= n; i++) {
				parent[i] = -1;
				found[i] = -1;
				low_link[i] = -1;
				in_stack[i] = false;
			}
			// STEP 2: visit all nodes
			timestamp = 0;
			for (int node = 1; node <= n; ++node) {
				if (parent[node] == -1) {
					parent[node] = node;
					// STEP 3: start a new DFS traversal this subtree
					dfs(node);
				}
			}
		}
		private void dfs(int node) {
			// STEP 1: a new node is visited - increment the timestamp
			found[node] = ++timestamp;
			low_link[node] = found[node];// node only knows its timestamp
			nodes_stack.push(node); // add node to the visiting stack
			in_stack[node] = true;
			// STEP 2: visit each neighbour
			for (int neigh: adj[node]) {
				// STEP 3: check if neigh is already visited
				if (parent[neigh] != -1) {
					// STEP 3.1: update low_link[node] with information gained through neigh
					// note: neigh is in the same SCC with node only if it's in the visiting stack;
					// otherwise, neigh is from other SCC, so it should be ignored
					if (in_stack[neigh]) {
						low_link[node] = Math.min(low_link[node], found[neigh]);
					}
					continue;
				}
				// STEP 4: save parent
				parent[neigh] = node;
				// STEP 5: recursively visit the child subree
				dfs(neigh);
				// STEP 6: update low_link[node] with information gained through neigh
				low_link[node] = Math.min(low_link[node], low_link[neigh]);
			}
			//STEP 7: node is root in a SCC if low_link[node] == found[node]
			//there is no edge from a descendant to an ancestor)
			if (low_link[node] == found[node]) {
				LinkedList<Integer> scc = new LinkedList<>();

				do {
					Integer x = nodes_stack.peek();
					//daca scc este unu , nu este gara si nu exista muchie catre el,
					//cresc numaurl de drumuri
					if (nodes_stack.size() == 1) {
						if (x != gara && in_degree[x] == 0) {
							nr++;
							nodes_stack.pop();
							in_stack[x] = false;
							return;
						}
					}

					nodes_stack.pop();
					in_stack[x] = false;
					scc.add(x);
				} while (scc.getLast() != node);
				//daca scc ul contine gara , ma intorc
				if (scc.contains(gara)) {
					return;
				}
				//verific daca nodurile din scc au muchie catre alte scc uri
				//daca da ,nu mai este nevoie sa adaug drumuri
				for (Integer nod: scc) {
					for (Integer neigh: adj2[nod]) {
						if (!scc.contains(neigh)) {
							return;
						}
					}
				}
				nr++;
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