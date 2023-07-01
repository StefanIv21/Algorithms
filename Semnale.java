import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Semnale {

	static int sig_type, x, y;
	static final int  mod = 1000000007;

	Semnale(){}

	static int type1() {
		//matrice in care memorez numarul de semnale posibile pe x+y biti,folosind y biti de 1
		//am folosit principiul de la Knapsack
		int[][]dp = new int[y + 1][x + y + 1];
		//daca numarul de biti de 1 este mai mare decat cel de 0 ma opresc
		if (x < y) {
			return 0;
		}
		for (int i = 1; i <= y; i++) {
			for (int j = 1; j <= x + y; j++) {
				//pentru un singur bit de 1,numarul de posibilitati este j
				//j reprezinta pe cati biti este scris semnalul
				//pentru a putea scrie un semnal cu j biti de 0 si i biti de 1
				//numarul trebuie sa aiba cel putin i + i-1 biti de 0
				//numarul de semnale pentru i biti de 1 si j de 0 am calculat o ,
				//gasind recurenta urmatoare
				if (i == 1) {
					dp[i][j] = j % mod;
				
				} else if (j - i - (i - 1) >= 0) {
					dp[i][j] = (dp[i][j - 1] + dp[i - 1][j - 2]) % mod;	
				}
			}
		}
		return dp[y][x + y];
	}

	static int type2() {
		//matrice in care memorez numarul de semnale posibile pe x+y biti,folosind y biti de 1
		//am folosit principiul de la Knapsack
		int[][]dp = new int[y + 1][x + y + 1];
		for (int i = 1 ;i <= y; i++) {
			for (int j = 1; j <= x + y; j++) {
				//pentru un singur bit de 1,numarul de posibilitati este j
				//j reprezinta pe cati biti este scris semnalul
				//pentru a putea scrie un semnal cu 2 biti de 1
				//numarul trebuie sa aiba cel putin i  biti de 0
				//numarul de semnale pentru 2 biti de 1 si j de 0 am calculat o ,
				//gasind recurenta urmatoare
				if (i == 1) {
					dp[i][j] = j % mod;
				} else if (i == 2) {
					if (j -  i >= 0) {
						dp[i][j] = (dp[i][j - 1] + dp[i - 1][j - 1]) % mod;	
					}
				} else if (j - (j / 3) - i >= 0) {
					//pentru a putea scrie un semnal cu j biti de 0 si i biti de 1
					//numarul trebuie sa aiba cel putin (j/3) + i biti de 0
					//numarul de semnale pentru i biti de 1 si j de 0 am calculat o ,
					//gasind recurenta urmatoare
					dp[i][j] = ((dp[i][j - 1] + dp[i - 1][j - 2]) % mod + dp[i - 2][j - 3]) % mod;
				} 	
			}
		}	
		return dp[y][x + y];
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("semnale.in"));

			sig_type = sc.nextInt();
			x = sc.nextInt();
			y = sc.nextInt();

			int ans;
			switch (sig_type) {
				case 1:
					ans = Semnale.type1();
					break;
				case 2:
					ans = Semnale.type2();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("semnale.out");
				fw.write(Integer.toString(ans));
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
