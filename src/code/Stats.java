package code;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.google.ortools.samples.Sudoku;

import sudoku.generator.Constants;
import sudoku.generator.SudokuGen;

public class Stats {

	int NbTest;
	int NbCourbe;

	ArrayList<Long> tempsTotal;
	ArrayList<Long> tempsMax;
	ArrayList<Long> tempsMin;
	ArrayList<Long> mediane;
	ArrayList<Long> moyenne;
	ArrayList<Long> ecartType;
	ArrayList<Long> variance;
	ArrayList<Long> Quartile1;
	ArrayList<Long> Quartile2;
	ArrayList<Long> Quartile3;

	public Stats(int NbTest, int NbCourbe) {
		this.NbTest = NbTest;
		this.NbCourbe = NbCourbe;

		tempsTotal = new ArrayList<Long>();
		tempsMax = new ArrayList<Long>();
		tempsMin = new ArrayList<Long>();
		mediane = new ArrayList<Long>();
		moyenne = new ArrayList<Long>();
		ecartType = new ArrayList<Long>();
		variance = new ArrayList<Long>();
		Quartile1 = new ArrayList<Long>();
		Quartile2 = new ArrayList<Long>();
		Quartile3 = new ArrayList<Long>();

	}

	public void init() {
		for (int i = 0; i < NbCourbe; i++) {
			tempsTotal.add((long) 0);
			tempsMax.add((long) 0);
			tempsMin.add(Long.MAX_VALUE);
			mediane.add((long) 0);
			ecartType.add((long) 0);
			variance.add((long) 0);
			moyenne.add((long) 0);
			Quartile1.add((long) 0);
			Quartile2.add((long) 0);
			Quartile3.add((long) 0);
		}
	}

	/**
	 * Cette méthode va proceder a l'execution des solver sur le nombre de
	 * sudoku designe par NbTest puis a l'etablissement de statistiques
	 */
	public void Generation() {
		 Sudoku ortoolsSolver = new Sudoku();

		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();
		ArrayList<Integer> Z = new ArrayList<Integer>();
		ArrayList<Integer> T = new ArrayList<Integer>();
		SudokuGen sg = new SudokuGen();
		sg.lirePlusieursSudoku(NbTest);

		final int[][] data = new int[NbCourbe][NbTest];
		long debut;
		long tmp;
		int numeroCourbe;
		SimplifiedSudoku s = new SimplifiedSudoku();
		for (int i = 0; i < NbTest; i++) {
			System.out.println(i);
			numeroCourbe = 0;
			X.add((int) (i));

			for (int j = 0; j < Constants.SIZE; j++)
				System.arraycopy(sg.ListSudoku.get(i)[j], 0, s.model[j], 0, Constants.SIZE);

			s.initFait();
			s.initPoids();
			s.TrouverNext(0, 0);
			System.out.println("Solve 1 debut" + s);

			debut = System.nanoTime();
			s.solverNaifIt();
			tmp = System.nanoTime() - debut;
			System.out.println("Solve 1 fin" + s);
			Y.add((int) tmp);

			tempsTotal.set(numeroCourbe, tmp + tempsTotal.get(numeroCourbe));

			if (tmp > tempsMax.get(numeroCourbe))
				tempsMax.set(numeroCourbe, tmp);

			if (tmp < tempsMin.get(numeroCourbe))
				tempsMin.set(numeroCourbe, tmp);

			numeroCourbe++;

			for (int j = 0; j < Constants.SIZE; j++)
				System.arraycopy(sg.ListSudoku.get(i)[j], 0, s.model[j], 0, Constants.SIZE);

			s.initParcoursSolverAdaptatifIt();
			System.out.println("Solve 2 debut" + s);
			
			ortoolsSolver.copytab(sg.ListSudoku.get(i));
			debut = System.nanoTime();
			 
			
			s.solverAdaptatifIt();
//			 ortoolsSolver.solve();

			tmp = System.nanoTime() - debut;
			System.out.println("Solve 2 fin" + s);
			Z.add((int) tmp);

			tempsTotal.set(numeroCourbe, tmp + tempsTotal.get(numeroCourbe));

			if (tmp > tempsMax.get(numeroCourbe))
				tempsMax.set(numeroCourbe, tmp);

			if (tmp < tempsMin.get(numeroCourbe))
				tempsMin.set(numeroCourbe, tmp);

			if (NbCourbe > 2) {

				numeroCourbe++;
				for (int j = 0; j < Constants.SIZE; j++)
					System.arraycopy(sg.ListSudoku.get(i)[j], 0, s.model[j], 0, Constants.SIZE);

				System.out.println("Solve 3 debut" + s);
				debut = System.nanoTime();
				s.solverDynamique();
				tmp = System.nanoTime() - debut;
				System.out.println("Solve 3 fin" + s);
				T.add((int) tmp);
				tempsTotal.set(numeroCourbe, tmp + tempsTotal.get(numeroCourbe));

				if (tmp > tempsMax.get(numeroCourbe))
					tempsMax.set(numeroCourbe, tmp);

				if (tmp < tempsMin.get(numeroCourbe))
					tempsMin.set(numeroCourbe, tmp);
			}

		}
		for (int i = 0; i < NbCourbe; i++) {
			moyenne.set(i, tempsTotal.get(i) / NbTest);
		}

		for (int i = 0; i < X.size(); i++) {
			variance.set(0, (long) Math.pow(Y.get(i) - moyenne.get(0), 2));
			variance.set(1, (long) Math.pow(Z.get(i) - moyenne.get(1), 2));
			if (NbCourbe > 2)
				variance.set(2, (long) Math.pow(T.get(i) - moyenne.get(2), 2));
		}

		for (int i = 0; i < NbCourbe; i++) {
			ecartType.set(i, (long) Math.sqrt(variance.get(i)));
		}

		int tmvx1[] = new int[NbTest];
		int tmvx2[] = new int[NbTest];
		int tmvx3[] = new int[NbTest];
		for (int i = 0; i < X.size(); i++) {
			tmvx1[i] = Y.get(i);
			tmvx2[i] = Z.get(i);
			if (NbCourbe > 2)
				tmvx3[i] = T.get(i);
		}

		Arrays.sort(tmvx1);
		Arrays.sort(tmvx2);
		Arrays.sort(tmvx3);

		mediane.set(0, (long) tmvx1[NbTest / 2]);
		mediane.set(1, (long) tmvx2[NbTest / 2]);
		if (NbCourbe > 2)
			mediane.set(2, (long) tmvx3[NbTest / 2]);

		for (int i = 0; i < X.size(); i++) {
			data[0][i] = (int) (Math.log(tmvx1[i]) / Math.log(2));
			data[1][i] = (int) (Math.log(tmvx2[i]) / Math.log(2));
			if (NbCourbe > 2)
				data[2][i] = (int) (Math.log(tmvx3[i]) / Math.log(2));
		}

		/*
		 * Quartile1Algo1 = tmvx1[(tmvx1.length / 4)]; Quartile2Algo1 =
		 * tmvx1[(tmvx1.length / 2)]; Quartile3Algo1 = tmvx1[(tmvx1.length -
		 * tmvx1.length / 4)];
		 * 
		 * Quartile1Algo2 = tmvx2[(tmvx2.length / 4)]; Quartile2Algo2 =
		 * tmvx2[(tmvx2.length / 2)]; Quartile3Algo2 = tmvx2[(tmvx2.length -
		 * tmvx2.length / 4)];
		 * 
		 * Quartile1Algo3 = tmvx3[(tmvx3.length / 4)]; Quartile2Algo3 =
		 * tmvx3[(tmvx3.length / 2)]; Quartile3Algo3 = tmvx3[(tmvx3.length -
		 * tmvx3.length / 4)];
		 */

		System.out.println(" *** Resolutions de " + NbTest + " Sudokus");
		System.out.println("Taille : " + Constants.SIZE);
		System.out.println();

		for (int i = 0; i < NbCourbe; i++) {
			System.out.println("Temps total algo " + (i + 1) + " : " + tempsTotal.get(i) / 1000 + " microsecondes");
			System.out.println("Temps moyen algo " + (i + 1) + " : " + moyenne.get(i) / 1000 + " microsecondes");
			System.out.println("Temps max algo " + (i + 1) + " : " + tempsMax.get(i) / 1000 + " microsecondes");
			System.out.println("Temps min algo " + (i + 1) + " : " + tempsMin.get(i) / 1000 + " microsecondes");
			System.out.println("Temps variance algo " + (i + 1) + " : " + variance.get(i) / 1000 + " microsecondes");
			System.out.println("Temps ecart type algo " + (i + 1) + " : " + ecartType.get(i) / 1000 + " microsecondes");
			System.out.println("Mediane algo " + (i + 1) + " : " + mediane.get(i) / 1000 + " microsecondes");

			System.out.println("******************************");
			System.out.println();

		}

//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				createAndShowGui(data);
//			}
//		});
	}

	public static void createAndShowGui(int[][] data) {
		GraphPanel2 mainPanel = new GraphPanel2(data);
		mainPanel.setPreferredSize(new Dimension(800, 600));
		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void statsEnfonctionDeN() {

	}

}
