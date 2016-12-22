package code;

import sudoku.generator.Constants;
import sudoku.generator.SudokuGen;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		/*
		 * Cette variable permet d'assurer l'unicité des sudokus
		 * générés, il est deconseillé de l'utiliser car elle 
		 * ralentis considérablement la génération de sudokus.
		 */
		boolean unique = false;

		// Taille 2
//		 Constants c = new Constants(6, 4);
//		 Constants c = new Constants(8, 4);
//		 Constants c = new Constants(11, 4);

		// Taille 3
//		Constants c = new Constants(29, 9);
//		 Constants c = new Constants(41, 9);	
//		 Constants c = new Constants(53, 9);
		
		// Taille 4
//		 Constants c = new Constants(90, 16);
		 Constants c = new Constants(128, 16);
		
//		Constants c = new Constants(135, 16);
		
//		Constants c = new Constants(137, 16);
		
//		 Constants c = new Constants(140, 16);
		
//		 Constants c = new Constants(150, 16);
		
//		 Constants c = new Constants(167, 16);
		 
		 /*
		  * Cette fonction doit être éxécuté la premiere fois, 
		  * elle permet de generer la seed
		  */
			
		 
		SudokuGen sg = new SudokuGen();
		sg.enregistrerDejaFait();
		sg.enregistrerPlusieursSudoku(10,unique);


		Stats s = new Stats(10, 3);
		s.init();
		s.Generation();
	}

}
