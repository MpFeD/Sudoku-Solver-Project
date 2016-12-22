package code;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphPanel2 extends JPanel {

    private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(0, 0, 255, 180); // bleu 
    private Color lineColor2 = new Color(255, 0, 0, 180); //Rouge
    private Color lineColor3 = new Color(0, 255,0, 180); //vert
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private ArrayList<Integer> scores1;
    private ArrayList<Integer> scores2;
    
    int[][] data;

    public GraphPanel2(ArrayList<Integer> scores1, ArrayList<Integer> scores2) {
        this.scores1 = scores1;
        this.scores1 = scores2;
    }
    
    public GraphPanel2(int [][] data) {
		this.data = data;
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (data[0].length - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints1 = new ArrayList<>();
        for (int i = 0; i < data[0].length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - data[0][i]) * yScale + padding);
            graphPoints1.add(new Point(x1, y1));
        }
        
        List<Point> graphPoints2 = new ArrayList<>();
        for (int i = 0; i < data[0].length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - data[1][i]) * yScale + padding);
            graphPoints2.add(new Point(x1, y1));
        }
        
        List<Point> graphPoints3 = new ArrayList<>();
        for (int i = 0; i < data[0].length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - data[2][i]) * yScale + padding);
            graphPoints3.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (data[0].length > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < data[0].length; i++) {
            if (data[0].length > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (data[0].length - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % 5) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints1.size() - 1; i++) {
            int x1 = graphPoints1.get(i).x;
            int y1 = graphPoints1.get(i).y;
            int x2 = graphPoints1.get(i + 1).x;
            int y2 = graphPoints1.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints1.size(); i++) {
            int x = graphPoints1.get(i).x - pointWidth / 2;
            int y = graphPoints1.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        
        Stroke oldStroke2 = g2.getStroke();
        g2.setColor(lineColor2);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints2.size() - 1; i++) {
            int x1 = graphPoints2.get(i).x;
            int y1 = graphPoints2.get(i).y;
            int x2 = graphPoints2.get(i + 1).x;
            int y2 = graphPoints2.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke2);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints2.size(); i++) {
            int x = graphPoints2.get(i).x - pointWidth / 2;
            int y = graphPoints2.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        
        Stroke oldStroke3 = g2.getStroke();
        g2.setColor(lineColor3);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints2.size() - 1; i++) {
            int x1 = graphPoints3.get(i).x;
            int y1 = graphPoints3.get(i).y;
            int x2 = graphPoints3.get(i + 1).x;
            int y2 = graphPoints3.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke3);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints2.size(); i++) {
            int x = graphPoints3.get(i).x - pointWidth / 2;
            int y = graphPoints3.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
    private double getMinScore() {
        double minScore = Integer.MAX_VALUE;
        for (int i=0; i<data[0].length; i++){
        	if(minScore > data[0][i])
        		minScore = data[0][i];
        	if(minScore > data[1][i])
        		minScore = data[1][i];
        	if(minScore > data[2][i])
        		minScore = data[2][i];
        }
        return minScore;
    }
    
    private double getMaxScore() {
    	double maxScore = 0;
        for (int i=0; i<data[0].length; i++){
        	if(maxScore < data[0][i])
        		maxScore = data[0][i];
        	if(maxScore < data[1][i])
        		maxScore = data[1][i];
        	if(maxScore < data[2][i])
        		maxScore = data[2][i];
        }
        return maxScore;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores1 = scores;
        invalidate();
        this.repaint();
    }

    public ArrayList<Integer> getScores() {
        return scores1;
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
    static class MainPanel extends JPanel {

        public MainPanel(int[][] data) {

            setLayout(new BorderLayout());

            JLabel title = new JLabel("Variation of Distance with time");
            title.setFont(new Font("Arial", Font.BOLD, 25));
            title.setHorizontalAlignment(JLabel.CENTER);

            JPanel graphPanel = new GraphPanel2(data);

            VerticalPanel vertPanel = new VerticalPanel();

            HorizontalPanel horiPanel = new HorizontalPanel();

            add(title, BorderLayout.NORTH);
            add(horiPanel, BorderLayout.SOUTH);
            add(vertPanel, BorderLayout.WEST);
            add(graphPanel, BorderLayout.CENTER);

        }

        class VerticalPanel extends JPanel {

            public VerticalPanel() {
                setPreferredSize(new Dimension(25, 0));
            }

            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D gg = (Graphics2D) g;
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font("Arial", Font.PLAIN, 15);

                String string = "Time (s)";

                FontMetrics metrics = g.getFontMetrics(font);
                int width = metrics.stringWidth(string);
                int height = metrics.getHeight();

                gg.setFont(font);

                drawRotate(gg, getWidth(), (getHeight() + width) / 2, 270, string);
            }

            public void drawRotate(Graphics2D gg, double x, double y, int angle, String text) {
                gg.translate((float) x, (float) y);
                gg.rotate(Math.toRadians(angle));
                gg.drawString(text, 0, 0);
                gg.rotate(-Math.toRadians(angle));
                gg.translate(-(float) x, -(float) y);
            }

        }

        class HorizontalPanel extends JPanel {

            public HorizontalPanel() {
                setPreferredSize(new Dimension(0, 25));
            }

            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D gg = (Graphics2D) g;
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font("Arial", Font.PLAIN, 15);

                String string = "Distance (m)";

                FontMetrics metrics = g.getFontMetrics(font);
                int width = metrics.stringWidth(string);
                int height = metrics.getHeight();

                gg.setFont(font);

                gg.drawString(string, (getWidth() - width) / 2, 11);
            }

        }

    }
}