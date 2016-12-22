package sudoku.generator;

public class Constants {

	/* Nombre de cases */
	public static int MAX_DISPLAYED_NUMBERS;

	/* Taille : N² */
	public static int SIZE;

	/* Taille des regions : N */
	public static int SUBGRID_SIZE; // 3

	public Constants(int MAX_DISPLAYED_NUMBERS, int SIZE) {
		this.MAX_DISPLAYED_NUMBERS = MAX_DISPLAYED_NUMBERS;
		this.SIZE = SIZE;
		this.SUBGRID_SIZE = (int) Math.sqrt(SIZE);
	}
}
