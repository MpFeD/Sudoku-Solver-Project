package sudoku.generator;

import java.util.*;
import java.io.*;

import com.google.ortools.samples.Sudoku;

import code.SimplifiedSudoku;

public class SudokuGen {

	public ArrayList<int[][]> ListSudoku;
	static String fileName;

	public SudokuGen() {
		ListSudoku = new ArrayList<int[][]>();
		fileName = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	/** Enregistre nb sudokus dans le dossier Sudokus du workspace */
	public void enregistrerPlusieursSudoku(int nb, boolean unicite) {
		int tmp[][] = null;
		int i = 0;
		Writer w = null;
		String nom = fileName;
		nom = nom.substring(0, nom.length() - 4);
		nom += "/Sudokus/" + "Sudoku_" + nb + "_" + Constants.SIZE + "_" + Constants.MAX_DISPLAYED_NUMBERS;
		try {
			w = new FileWriter(nom, false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (i = 0; i < nb; i++) {
			try {
				tmp = createGrid(unicite);
			} catch (Exception e) {
				i--;
				continue;
			}
			for (int j = 0; j < Constants.SIZE; j++)
				for (int k = 0; k < Constants.SIZE; k++)
					try {
						w.write(("" + tmp[j][k]));
						w.write(",");
					} catch (IOException e) {
						e.printStackTrace();
					}

			System.out.println("Generation : " + i);
		}
		try {
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lis nb sudokus dans le dossier Sudokus du workspace Les sudokus lus sont
	 * enregistrer dans un arraylist
	 */
	public void lirePlusieursSudoku(int nb) {
		Reader r = null;
		String stmp = "";
		String nom = fileName;
		nom = nom.substring(0, nom.length() - 4);
		nom += "/Sudokus/" + "Sudoku_" + nb + "_" + Constants.SIZE + "_" + Constants.MAX_DISPLAYED_NUMBERS;

		try {
			r = new FileReader(nom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < nb; i++) {
			int[][] tmp = new int[Constants.SIZE][Constants.SIZE];
			for (int j = 0; j < Constants.SIZE; j++)
				for (int k = 0; k < Constants.SIZE; k++)
					try {
						stmp = "";
						while (true) {
							char c = (char) (r.read());
							if (c == ',')
								break;
							stmp += c;
						}
						tmp[j][k] = Integer.parseInt(stmp);
					} catch (IOException e) {
						e.printStackTrace();
					}
			ListSudoku.add(tmp);
		}
	}

	/**
	 * Enregistre un sudoku complet de base utile pour la generation des autres
	 * sudokus Cette methode devient très longue pour une taille supérieure à
	 * 4
	 */
	public void enregistrerDejaFait() {
		SimplifiedSudoku s = new SimplifiedSudoku();
		s.initFait();
		s.initParcoursSolverNaifIt();
		s.solverNaifIt();
		Writer w = null;

		String nom = fileName;
		nom = nom.substring(0, nom.length() - 4);
		nom += "/Sudokus/" + "SudokuComplet_" + Constants.SIZE;
		try {
			w = new FileWriter(nom, false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int j = 0; j < Constants.SIZE; j++)
			for (int k = 0; k < Constants.SIZE; k++)
				try {
					w.write(("" + s.model[j][k]));
					w.write(",");
				} catch (IOException e) {
					e.printStackTrace();
				}

		try {
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Seed :\n" + s);
	}

	/** Creer un sudoku aleatoire pour cela on lit un sudoku complet puis on
	 * procede a des permutations de lignes et colonnes
	 */

	public static int[][] createGrid(boolean Unicite) throws Exception {
		int[][] grid2 = new int[Constants.SIZE][Constants.SIZE];

		int i = 0;
		int j = 0;
		int count = 0;
		int rand1;
		int rand2;
		int rand3;
		int rand4;
		int tmp = 0;
		int tmptab1[][] = new int[Constants.SIZE][Constants.SIZE];
		int tmptab2[][] = new int[Constants.SIZE][Constants.SIZE];
		
		SimplifiedSudoku s = new SimplifiedSudoku();

		Reader r = null;
		String stmp = "";
		String nom = fileName;
		nom = nom.substring(0, nom.length() - 4);
		nom += "/Sudokus/" + "SudokuComplet_" + Constants.SIZE;

		try {
			r = new FileReader(nom);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (j = 0; j < Constants.SIZE; j++)
			for (int k = 0; k < Constants.SIZE; k++)
				try {
					stmp = "";
					while (true) {
						char c = (char) (r.read());
						if (c == ',')
							break;
						stmp += c;
					}
					grid2[j][k] = Integer.parseInt(stmp);
				} catch (IOException e) {
					e.printStackTrace();
				}

		for (j = 0; j < Math.pow(Constants.SIZE, 6); j++) {
			rand1 = (int) (Math.random() * Constants.SIZE);
			rand3 = (int) (Math.random() * Constants.SIZE);
			if (rand1 % Constants.SUBGRID_SIZE == Constants.SUBGRID_SIZE - 1) {
				rand2 = rand1 - 1;
			} else
				rand2 = rand1 + 1;
			if (rand3 % Constants.SUBGRID_SIZE == Constants.SUBGRID_SIZE - 1) {
				rand4 = rand3 - 1;
			} else
				rand4 = rand3 + 1;

			permutterLigne(rand1, rand2, grid2);
			permutterColonne(rand3, rand4, grid2);
		}
		

		count = 0;
		int p = 0;
		while (true) {
			if (count == (Constants.SIZE * Constants.SIZE) - Constants.MAX_DISPLAYED_NUMBERS) {
				for (int k = 0; k < Constants.SIZE; k++)
					for (int m = 0; m < Constants.SIZE; m++) {
						System.out.print(grid2[k][m]);
					}
					

				System.out.println();

				for (int k = 0; k < Constants.SIZE; k++)
					for (int m = 0; m < Constants.SIZE; m++) {
						System.out.print(tmptab1[k][m]);
					}
				
				System.out.println();
				
				for (int k = 0; k < Constants.SIZE; k++)
					for (int m = 0; m < Constants.SIZE; m++) {
						System.out.print(tmptab2[k][m]);
					}

				System.out.println();
				for (int k = 0; k < Constants.SIZE; k++)
					for (int m = 0; m < Constants.SIZE; m++) {
						System.out.print(s.model[k][m]);
					}
				System.out.println();

				break;
			}
			
			p++;
			if (p % 250 == 0) {
//				for (int k = 0; k < Constants.SIZE; k++)
//					for (int m = 0; m < Constants.SIZE; m++) {
//						System.out.print(grid2[k][m]);
//					}
				System.out.println(p);
				return createGrid(Unicite);
			}

			i = (int) (Math.random() * Constants.SIZE);
			j = (int) (Math.random() * Constants.SIZE);

			if (grid2[i][j] != 0) {
				tmp = grid2[i][j];
				grid2[i][j] = 0;
				if (Unicite == false) {
					count++;
				} else {
					
					
					Sudoku su = new Sudoku();
					
					
					
					su.copytab(grid2);
					
					su.solve();
					
					
//					for (int k = 0; k < Constants.SIZE; k++)
//						System.arraycopy(grid2[k], 0, s.model[k], 0, Constants.SIZE);
//					
//					
//					s.initFait();
//					s.initPoids();
//					
//					
//					s.initParcoursSolverNaifIt();
//
//					s.solverNaifIt();
//						
//
//					for (int k = 0; k < Constants.SIZE; k++)
//						System.arraycopy(s.model[k], 0, tmptab1[k], 0, Constants.SIZE);
//
//					for (int k = 0; k < Constants.SIZE; k++)
//						System.arraycopy(grid2[k], 0, s.model[k], 0, Constants.SIZE);
//
//					s.initParcoursSolverAdaptatifIt();
//
//					s.solverAdaptatifIt();
//					
//					for (int k = 0; k < Constants.SIZE; k++)
//						System.arraycopy(s.model[k], 0, tmptab2[k], 0, Constants.SIZE);
//					
//					for (int k = 0; k < Constants.SIZE; k++)
//						System.arraycopy(grid2[k], 0, s.model[k], 0, Constants.SIZE);
//					
//					s.TrouverNext(0, 0);
//					s.solverDynamique();
					
					
					boolean controle = true;
//					for (int k = 0; k < Constants.SIZE; k++)
//						for (int l = 0; l < Constants.SIZE; l++)
//							if ( tmptab1[k][l] != s.model[k][l]  || s.model[k][l] != tmptab2[k][l]) {
//								controle = false;
//								break;
//							}
					
					
					if (su.cpt!=1)
						controle = false;

					if (controle == true)
						count++;
					else
						grid2[i][j] = tmp;
				}
			
			}
		}
		return grid2;

	}

	/** permute la ligne row1 avec la ligne row2 dans le tableau tab */

	public static void permutterLigne(int row1, int row2, int tab[][]) {
		int tmp;
		for (int i = 0; i < Constants.SIZE; i++) {
			tmp = tab[row1][i];
			tab[row1][i] = tab[row2][i];
			tab[row2][i] = tmp;
		}
	}

	/** permute la colonne col1 avec la colonne row2 dans le tableau tab */

	public static void permutterColonne(int col1, int col2, int tab[][]) {
		int tmp;
		for (int i = 0; i < Constants.SIZE; i++) {
			tmp = tab[i][col1];
			tab[i][col1] = tab[i][col2];
			tab[i][col2] = tmp;
		}
	}
}
