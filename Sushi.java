import java.beans.Visibility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AccessException;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

class Sushi {

	static int n, m, x;
	static int[] prices;
	static int[][] grades;
	static int[] allgrade;

	Sushi(){}


	static int task1() {
		//in vectorul allgrade calculez suma notelor pentru fiecare platou
		allgrade = new int [m];
		for (int i = 0; i < m; i++) {
			allgrade[i] = 0;
			for (int j = 0; j < n; j++) {
				allgrade[i] += grades[j][i];
			}
		}
		//am folosit algoritumul Knapsack din laborator
		int W = n * x;
		int[][]dp = new int[m + 1][W + 1];
		for (int cap = 0 ;cap <= W; cap++) {
			dp[0][cap] = 0;
		}
		for (int i = 1; i <= m; i++) {
			for (int cap = 0 ; cap <= W; cap++) {
				dp[i][cap] = dp[i - 1][cap];
				if (cap - prices[i - 1] >= 0) {
					int sol_aux = dp[i - 1][cap - prices[i - 1]] + allgrade[i - 1];
					dp[i][cap] = Math.max(dp[i][cap],sol_aux);
				}
			}
		}
		return dp[m][W];
	}
	static int task2() {
		//in vectorul allgrade calculez suma notelor pentru fiecare platou
		allgrade = new int [m];
		for (int i = 0; i < m; i++) {
			allgrade[i] = 0;
			for (int j = 0; j < n; j++) {
				allgrade[i] += grades[j][i];
			}
		}
		//fiind posibil sa alegem 2 platouri de acelasi tip, 
		//dublez numarul de platouri,acelasi platou aparand de 2 ori
		int [] prices2 = new int[2 * m];
		int [] allgrade2 = new int[2 * m];
		for (int i = 0; i < m; i++) {
			prices2[2 * i] = prices[i];
			prices2[2 * i + 1] = prices[i];
			allgrade2[2 * i] = allgrade[i];
			allgrade2[2 * i + 1] = allgrade[i];	
		}
		int W = n * x;
		int[][]dp = new int[2 * m + 1][W + 1];
		//am folosit algoritumul Knapsack din laborator
		for (int cap = 0 ;cap <= W; cap++) {
			dp[0][cap] = 0;
		}
		for (int i = 1; i <= 2 * m; i++) {
			for (int cap = 0 ; cap <= W; cap++) {
				dp[i][cap] = dp[i - 1][cap];
				if (cap - prices2[i - 1] >= 0) {
					int sol_aux = dp[i - 1][cap - prices2[i - 1]] + allgrade2[i - 1];
					if (dp[i][cap] < sol_aux) {
						dp[i][cap] = sol_aux;
					}
				}
			}
		}	
		return dp[2 * m][W];
	}

	static int task3() {
		//in vectorul allgrade calculez suma notelor pentru fiecare platou
		allgrade = new int [m];
		for (int i = 0; i < m; i++) {
			allgrade[i] = 0;
			for (int j = 0; j < n; j++) {
				allgrade[i] += grades[j][i];
			}
		}
		//fiind posibil sa alegem 2 platouri de acelasi tip,
		// dublez numarul de platouri,acelasi platou aparand de 2 ori
		int [] prices2 = new int[2 * m];
		int [] allgrade2 = new int[2 * m];
		for (int i = 0; i < m; i++) {
			prices2[2 * i] = prices[i];
			prices2[2 * i + 1] = prices[i];
			allgrade2[2 * i] = allgrade[i];
			allgrade2[2 * i + 1] = allgrade[i];
		}
		int W = n * x;
		int[][][]dp = new int[2 * m + 1][W + 1][n + 1];
		//am folosit algoritumul Knapsack din laborator
		//am adaugat inca o dimeniune,reprezentand numarul maxim  de platouri admis
		for (int cap = 0 ;cap <= W; cap++) {
			for (int k = 0; k <= n; k++) {
				dp[0][cap][k] = 0;
			}
		}
		for (int i = 1; i <= 2 * m; i++) {
			for (int cap = 0; cap <= W; cap++) {
				for (int k = 1 ;k <= n; k++) {
					dp[i][cap][k] = dp[i - 1][cap][k];
					if (cap - prices2[i - 1] >= 0) {
						int sol_aux = dp[i - 1][cap - prices2[i - 1]][k - 1] + allgrade2[i - 1];
						if (dp[i][cap][k] < sol_aux) {
							dp[i][cap][k] = sol_aux;
						}
					}
				}
			}
		}		
		return dp[2 * m][W][n];
	}


	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("sushi.in"));

			final int task = sc.nextInt(); // task number

			n = sc.nextInt(); // number of friends
			m = sc.nextInt(); // number of sushi types
			x = sc.nextInt(); // how much each of you is willing to spend

			prices = new int[m]; // prices of each sushi type
			grades = new int[n][m]; // the grades you and your friends gave to each sushi type

			// price of each sushi
			for (int i = 0; i < m; ++i) {
				prices[i] = sc.nextInt();
			}

			// each friends rankings of sushi types
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < m; ++j) {
					grades[i][j] = sc.nextInt();
				}
			}

			int ans;
			switch (task) {
				case 1:
					ans = Sushi.task1();
					break;
				case 2:
					ans = Sushi.task2();
					break;
				case 3:
					ans = Sushi.task3();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("sushi.out");
				fw.write(Integer.toString(ans) + '\n');
				fw.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
