package code;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

import sudoku.generator.Constants;

public class SimplifiedSudoku {

	public int model[][];
	public int poidsTotal[][];
	public int parcours[][];
	public int parcoursSolveIt[][];
	public int parcoursSolveAleatoire[][];
	public int parcoursSolve3[][];
	public ArrayList<Integer> possibilites;
	public ArrayList<Integer> numpossibilites;
	public ArrayList<int[][]> saveModel;
	public ArrayList<Integer> saveCurseur;
	int etage;
	public boolean fait[][];
	public boolean faitParcours[][];

	public int region[][];

	private int curseur;

	public final static int TAILLE = Constants.SIZE;
	public final static int Tsqr = Constants.SUBGRID_SIZE;

	public ArrayList<Integer> nextX;
	public ArrayList<Integer> nextY;

	public ArrayList<Integer> tabX;
	public ArrayList<Integer> tabY;

	public int[][] resultat;

	public int monindex;

	public SimplifiedSudoku() {
		resultat = new int[TAILLE][TAILLE];
		saveCurseur = new ArrayList<Integer>();
		model = new int[TAILLE][TAILLE];

		poidsTotal = new int[TAILLE][TAILLE];

		fait = new boolean[TAILLE][TAILLE];
		faitParcours = new boolean[TAILLE][TAILLE];
		
		
		region= new int[][]{{1, 1, 1, 2, 3, 3, 3, 3, 3},
				  {1, 1, 1, 2, 2, 2, 3, 3, 3},
				  {1, 4, 4, 4, 4, 2, 2, 2, 3},
				  {1, 1, 4, 5, 5, 5, 5, 2, 2},
				  {4, 4, 4, 4, 5, 6, 6, 6, 6},
				  {7, 7, 5, 5, 5, 5, 6, 9, 9},
				  {8, 7, 7, 7, 6, 6, 6, 6, 9},
				  {8, 8, 8, 7, 7, 7, 9, 9, 9},
				  {8, 8, 8, 8, 8, 7, 9, 9, 9}};

		saveModel = new ArrayList<int[][]>();
		parcours = new int[3][TAILLE * TAILLE];
		parcoursSolveIt = new int[3][TAILLE * TAILLE];
		parcoursSolveAleatoire = new int[3][TAILLE * TAILLE];
		parcoursSolve3 = new int[3][TAILLE * TAILLE];
		possibilites = new ArrayList<Integer>();
		numpossibilites = new ArrayList<Integer>();
		etage = 0;

		nextX = new ArrayList<Integer>();
		nextY = new ArrayList<Integer>();

		tabX = new ArrayList<Integer>();
		tabY = new ArrayList<Integer>();
	}

	/** Affiche le model courant au solveur */
	public String toString() {
		String ret = "*****************";
		for (int row = 0; row < TAILLE; row++)
			for (int col = 0; col < TAILLE; col++) {
				if (col == 0)
					ret += "\n";
				ret += model[row][col] + " ";
			}
		return ret;
	}

	/** Affiche les degres de liberte */
	public String PoidsToStr() {
		String ret = "*****************";
		for (int row = 0; row < TAILLE; row++)
			for (int col = 0; col < TAILLE; col++) {
				if (col == 0)
					ret += "\n";
				ret += poidsTotal[row][col] + " ";
			}
		return ret;
	}

	/** Cherche si num est une valeure acceptable pour la ligne row */
	protected boolean checkRow(int row, int num) {
		for (int col = 0; col < TAILLE; col++)
			if (model[row][col] == num)
				return false;

		return true;
	}

	/** Cherche si num est une valeure acceptable pour colonne col */
	protected boolean checkCol(int col, int num) {
		for (int row = 0; row < TAILLE; row++)
			if (model[row][col] == num)
				return false;

		return true;
	}

	/** Cherche si num est une valeure acceptable pour la région de (row, col) */
	protected boolean checkBox(int row, int col, int num) {
		row = (row / Tsqr) * Tsqr;
		col = (col / Tsqr) * Tsqr;

		for (int r = 0; r < Tsqr; r++)
			for (int c = 0; c < Tsqr; c++) {
				if (model[row + r][col + c] == num)
					return false;
			}

		return true;
	}

	/** checkBox etendu pour le chaoSudoku */
	protected boolean checkBox(int[][] region, int row, int col, int num) {
		int nbReg = region[row][col];
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (region[i][j] == nbReg) {
					if (model[i][j] == num)
						return false;
				}
			}
		}
		nbReg++;

		return true;
	}

	/** initialise un parcours aleatoire */
	public void initParcoursSolveAleatoire() {

		ArrayList<Integer> X = new ArrayList();
		ArrayList<Integer> Y = new ArrayList();

		int k = -1;
		for (int i = 0; i < TAILLE * TAILLE; i++) {
			if ((i % TAILLE) == 0)
				k++;
			X.add(i % TAILLE);
			Y.add(k);
		}

		for (int i = 0; i < TAILLE * TAILLE; i++) {
			int indice = (int) (Math.random() * (X.size()));
			parcoursSolveAleatoire[0][i] = X.get(indice);
			parcoursSolveAleatoire[1][i] = Y.get(indice);
			X.remove(indice);
			Y.remove(indice);
		}

	}

	/** solver aleatoire */
	public void solveAleatoire() {
		int row;
		int col;
		int etage = 0;
		int tmp = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		int profondeur = 0;
		int xx = 1;

		initParcoursSolveAleatoire();

		System.out.println("Debut Solve aleatoire \n" + this);

		while (true) {
			try {
				row = parcoursSolveAleatoire[0][etage];
				col = parcoursSolveAleatoire[1][etage];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Fin Solve aleatoire\n" + this);
				return;
			}

			tmp = 0;
			tmp2 = 0;
			tmp3 = 0;
			profondeur = 0;

			if (model[row][col] != 0) {
				etage++;
				xx = 1;
				continue;
			} else {
				for (int num = xx; num < TAILLE + 1; num++) {
					if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
						tmp++;
						if (tmp > 1) {
							possibilites.add(num);
							numpossibilites.add(etage);
							break;
						}
						if (tmp == 1)
							tmp2 = num;
					}
				}

				if (tmp > 0) {
					model[row][col] = tmp2;
					etage++;
					xx = 1;
					continue;
				} else {
					tmp3 = possibilites.get(possibilites.size() - 1);
					profondeur = etage - (numpossibilites.get(numpossibilites.size() - 1));

					for (int i = 0; i <= profondeur; i++) {
						if (fait[(parcoursSolveAleatoire[0][etage - i])][(parcoursSolveAleatoire[1][etage - i])] == false) {
							model[parcoursSolveAleatoire[0][etage - i]][parcoursSolveAleatoire[1][etage - i]] = 0;
						}
					}

					etage = numpossibilites.get(numpossibilites.size() - 1);
					numpossibilites.remove(numpossibilites.size() - 1);
					possibilites.remove(possibilites.size() - 1);
					xx = tmp3;
					continue;
				}
			}
		}
	}

	/** solver naif recursif */
	public void solveNaif(int row, int col) {
		if (row > TAILLE - 1) {
			return;
		}

		if (model[row][col] != 0)
			next(row, col);
		else {
			for (int num = 1; num < TAILLE + 1; num++) {
				if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
					model[row][col] = num;
					next(row, col);
				}
			}
			model[row][col] = 0;
		}
	}

	/** pour le solver naif recursif, cherche la prochaine case*/
	public void next(int row, int col) {
		if (col < TAILLE - 1)
			solveNaif(row, col + 1);
		else
			solveNaif(row + 1, 0);
	}

	/** solver naif itératif */
	public void solverNaifIt() {
		int row;
		int col;
		int etage = 0;
		int tmp = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		int profondeur = 0;
		int xx = 1;

		initParcoursSolverNaifIt();
		possibilites.clear();
		numpossibilites.clear();

		while (true) {

			try {
				row = parcoursSolveIt[0][etage];
				col = parcoursSolveIt[1][etage];
			} catch (ArrayIndexOutOfBoundsException e) {
				return;
			}
			tmp = 0;
			tmp2 = 0;
			tmp3 = 0;
			profondeur = 0;

			if (model[row][col] != 0) {
				etage++;
				xx = 1;
				continue;
			} else {
				for (int num = xx; num < TAILLE + 1; num++) {
					if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
						tmp++;
						if (tmp > 1) {
							possibilites.add(num);
							numpossibilites.add(etage);
							break;
						}
						if (tmp == 1)
							tmp2 = num;
					}
				}

				if (tmp > 0) {
					model[row][col] = tmp2;
					etage++;
					xx = 1;
					continue;
				} else {
					tmp3 = possibilites.get(possibilites.size() - 1);
					profondeur = etage - (numpossibilites.get(numpossibilites.size() - 1));

					for (int i = 0; i <= profondeur; i++) {
						if (fait[(parcoursSolveIt[0][etage - i])][(parcoursSolveIt[1][etage - i])] == false) {
							model[parcoursSolveIt[0][etage - i]][parcoursSolveIt[1][etage - i]] = 0;
						}
					}

					etage = numpossibilites.get(numpossibilites.size() - 1);
					numpossibilites.remove(numpossibilites.size() - 1);
					possibilites.remove(possibilites.size() - 1);
					xx = tmp3;
					continue;
				}
			}
		}
	}

	/** calcul les poids
	 * renvoi vrai si le sudoku est fini */
	public boolean initPoids() {
		int poids = 0;
		int m = 0;
		boolean fini = true;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (fait[i][j] == true) {
					poidsTotal[i][j] = Constants.SIZE + 1;
				} else {
					for (int k = 1; k < TAILLE + 1; k++) {
						if (checkRow(i, k) && checkCol(j, k) && checkBox(i, j, k)) {
							poids++;
						}
					}
					parcoursSolve3[0][m] = i;
					parcoursSolve3[1][m] = j;
					parcoursSolve3[2][m] = poids;

					if (poids == 0 && model[parcoursSolve3[0][m]][parcoursSolve3[1][m]] == 0) {
						nextX.add(parcoursSolve3[0][m]);
						nextY.add(parcoursSolve3[1][m]);
					}
					if (poids != 0) {
						fini = false;
					}
					m++;
					poidsTotal[i][j] = poids;
					poids = 0;
				}
			}
		}
		return fini;
	}
	
	/** change les poids des cases sur la meme ligne, colonne, région que (row,col) */
	public void changerPoids(int row, int col) {
		int poids;

		/* Changement colonne */
		for (int k = 0; k < TAILLE; k++) {
			poids = 0;
			for (int i = 0; i < TAILLE; i++) {
				if (checkRow(row, i) && checkCol(k, i) && checkBox(row, k, i)) {
					poids++;
				}
			}
			poidsTotal[row][k] = poids;
		}

		/* Changement ligne */
		for (int k = 0; k < TAILLE; k++) {
			poids = 0;
			for (int i = 0; i < TAILLE; i++) {
				if (checkRow(k, i) && checkCol(col, i) && checkBox(k, col, i)) {
					poids++;
				}
			}
			poidsTotal[k][col] = poids;
		}

		/* Changement region */
		int a = (row / Tsqr) * Tsqr;
		int b = (col / Tsqr) * Tsqr;

		for (int r = 0; r < Tsqr; r++) {
			for (int c = 0; c < Tsqr; c++) {
				poids = 0;
				for (int i = 0; i < TAILLE; i++) {
					if (checkRow(a + r, i) && checkCol(b + c, i) && checkBox(a + r, b + c, i)) {
						poids++;
					}
				}
				poidsTotal[a + r][b + c] = poids;
			}
		}
	}

	/** initialise le parcours pour le solver naif iteratif*/
	public void initParcoursSolverNaifIt() {
		int k = -1;
		for (int i = 0; i < TAILLE * TAILLE; i++) {
			if ((i % TAILLE) == 0)
				k++;
			parcoursSolveIt[0][i] = i % TAILLE;
			parcoursSolveIt[1][i] = k;
		}

	}
	/** initialise le parcours pour le solver naif adaptatif*/
	public void initParcoursSolverAdaptatifIt() {

		int min = TAILLE + 1;
		int imin = TAILLE + 1;
		int jmin = TAILLE + 1;

		int k = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				parcours[0][j + i * TAILLE] = i;
				parcours[1][j + i * TAILLE] = j;
				parcours[2][j + i * TAILLE] = poidsTotal[i][j];

			}
		}

		int t1, t2, t3, index = 0;
		for (int i = 0; i < TAILLE * TAILLE; i++) {
			min = TAILLE + 1;
			imin = TAILLE + 1;
			jmin = TAILLE + 1;

			for (int j = i; j < TAILLE * TAILLE; j++) {
				if (parcours[2][j] < min) {
					index = j;
					imin = parcours[0][j];
					jmin = parcours[1][j];
					min = parcours[2][j];

				}
			}
			t1 = parcours[0][i];
			t2 = parcours[1][i];
			t3 = parcours[2][i];

			parcours[0][i] = imin;
			parcours[1][i] = jmin;
			parcours[2][i] = min;

			parcours[0][index] = t1;
			parcours[1][index] = t2;
			parcours[2][index] = t3;
		}

	}
	
	/** solver adaptatif recursif*/
	public void solverAdaptatif(int row, int col, int xx) {

		int tmp = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		int profondeur = 0;

		if (etage > TAILLE * TAILLE - 2) {

			if (model[row][col] == 0) {
				for (int num = xx; num < TAILLE + 1; num++) {
					if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
						tmp++;
						if (tmp > 1) {
							possibilites.add(num);
							numpossibilites.add(etage);
							break;
						}
						if (tmp == 1)
							tmp2 = num;
					}
				}
				if (tmp == 1) {
					model[row][col] = tmp2;
				}
			}
		}

		if (model[row][col] != 0) {
			etage++;
			solverAdaptatif(parcours[0][etage], parcours[1][etage], 1);
		} else {
			for (int num = xx; num < TAILLE + 1; num++) {
				if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
					tmp++;
					if (tmp > 1) {
						possibilites.add(num);
						numpossibilites.add(etage);
						break;
					}
					if (tmp == 1)
						tmp2 = num;
				}
			}

			if (tmp > 0) {
				model[row][col] = tmp2;
				etage++;
				solverAdaptatif(parcours[0][etage], parcours[1][etage], 1);
			} else {
				tmp3 = possibilites.get(possibilites.size() - 1);
				profondeur = etage - (numpossibilites.get(numpossibilites.size() - 1));
				for (int i = 0; i <= profondeur; i++) {
					if (fait[(parcours[0][etage - i])][(parcours[1][etage - i])] == false) {
						model[parcours[0][etage - i]][parcours[1][etage - i]] = 0;
					}
				}

				etage = numpossibilites.get(numpossibilites.size() - 1);
				numpossibilites.remove(numpossibilites.size() - 1);
				possibilites.remove(possibilites.size() - 1);
				solverAdaptatif(parcours[0][etage], parcours[1][etage], tmp3);
			}
		}
	}

	/** solver adaptatif iteratif*/
	public void solverAdaptatifIt() { // resoud (row,col) ï¿½ la possibilite xx

		int row = 0;
		int col = 0;
		int etage = 0;
		int tmp = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		int profondeur = 0;
		int xx = 1;

		possibilites.clear();
		numpossibilites.clear();
		System.out.println(region[0][0]);

		while (true) {
			row = parcours[0][etage];
			col = parcours[1][etage];

			if (row == Constants.SIZE + 1) {
				return;
			}

			tmp = 0;
			tmp2 = 0;
			tmp3 = 0;
			profondeur = 0;

			if (model[row][col] != 0) {
				etage++;
				xx = 1;
				continue;
			} else {
				for (int num = xx; num < TAILLE + 1; num++) {
					if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
						tmp++;
						if (tmp > 1) {
							possibilites.add(num);
							numpossibilites.add(etage);
							break;
						}
						if (tmp == 1)
							tmp2 = num;
					}
				}

				if (tmp > 0) {
					model[row][col] = tmp2;
					etage++;
					xx = 1;
					continue;
				} else {
					tmp3 = possibilites.get(possibilites.size() - 1);
					profondeur = etage - (numpossibilites.get(numpossibilites.size() - 1));

					for (int i = 0; i <= profondeur; i++) {
						if (fait[(parcours[0][etage - i])][(parcours[1][etage - i])] == false) {
							model[parcours[0][etage - i]][parcours[1][etage - i]] = 0;
						}
					}

					etage = numpossibilites.get(numpossibilites.size() - 1);
					numpossibilites.remove(numpossibilites.size() - 1);
					possibilites.remove(possibilites.size() - 1);
					xx = tmp3;
					continue;
				}
			}
		}
	}
	
	/** solver dynamique*/
	public void solverDynamique() {
		int row = 0;
		int col = 0;
		int xx = 1;
		int tmp = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		curseur = 0;
		nextX.clear();
		nextY.clear();
		saveCurseur.clear();
		possibilites.clear();
		numpossibilites.clear();

		while (true) {
			tmp = 0;

			try {
				row = nextX.get(curseur);
				col = nextY.get(curseur);

			} catch (Exception e) {
				if (initPoids() == true) {
					return;
				}
				TrouverNext(row, col);
				continue;
			}

			curseur++;

			if (model[row][col] != 0) {
				xx = 1;
				continue;
			} else {
				for (int num = xx; num < TAILLE + 1; num++) {
					if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num)) {
						tmp++;
						if (tmp > 1) {
							possibilites.add(num);
							int tabb[][] = new int[TAILLE][TAILLE];
							for (int j = 0; j < Constants.SIZE; j++)
								System.arraycopy(model[j], 0, tabb[j], 0, Constants.SIZE);

							saveModel.add(tabb);
							saveCurseur.add(curseur);
							break;
						}
						if (tmp == 1)
							tmp2 = num;
					}
				}

				if (tmp > 0) {
					model[row][col] = tmp2;
					xx = 1;
					continue;
				} else {
					tmp3 = possibilites.get(possibilites.size() - 1);
					try {
						for (int j = 0; j < Constants.SIZE; j++)
							System.arraycopy(saveModel.get(saveModel.size() - 1)[j], 0, model[j], 0, Constants.SIZE);
					} catch (Exception e) {
						System.out.println(this);
					}

					saveModel.remove(saveModel.size() - 1);
					possibilites.remove(possibilites.size() - 1);

					curseur = saveCurseur.get(saveCurseur.size() - 1) - 1;
					saveCurseur.remove(saveCurseur.size() - 1);
					xx = tmp3;
					continue;
				}
			}
		}
	}

	/** pour le solver dynamique, cette méthode est appeler pour ajouter des cases avec de faibles degrés de libertés dans le parcours*/
	public void TrouverNext(int row, int col) {

		int poidmin = TAILLE + 1;
		int i;

		for (i = 0; i < TAILLE * TAILLE - Constants.MAX_DISPLAYED_NUMBERS; i++) {
			if (parcoursSolve3[2][i] < poidmin && parcoursSolve3[2][i] > 0 && parcoursSolve3[0][i] != row && parcoursSolve3[1][i] != col) {
				poidmin = parcoursSolve3[2][i];
			}
		}

		for (i = 0; i < TAILLE * TAILLE - Constants.MAX_DISPLAYED_NUMBERS; i++) {
			if ((parcoursSolve3[2][i] <= poidmin + (Constants.SUBGRID_SIZE) + (Constants.SUBGRID_SIZE / 2)) && (parcoursSolve3[2][i] > 0) && model[parcoursSolve3[0][i]][parcoursSolve3[1][i]] == 0) {
				nextX.add(parcoursSolve3[0][i]);
				nextY.add(parcoursSolve3[1][i]);
			}
		}
	}

	/** initialise le tableau booleen qui contiendra vrai pour chaque case si la case est présente dans le model de base, faux sinon*/
	public void initFait() {
		for (int i = 0; i < TAILLE; i++)
			for (int j = 0; j < TAILLE; j++)
				if (model[i][j] != 0) {
					fait[i][j] = true;
					faitParcours[i][j] = true;
				} else {
					fait[i][j] = false;
					faitParcours[i][j] = false;
				}
	}

}
