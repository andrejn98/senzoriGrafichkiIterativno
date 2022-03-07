package com.company;
import javax.swing.*;
import java.awt.*;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;


class AnchorNode{
    private int id;
    private double x;
    private double y;
    private boolean flag;
    private double x1;
    private double y1;
    private int radioRange;
    private int inRangeOf;

    public AnchorNode(int id, int x, int y, boolean flag, int x1, int y1, int radioRange, int inRangeOf) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.flag = flag;
        this.x1 = x1;
        this.y1 = y1;
        this.radioRange = radioRange;
        this.inRangeOf = inRangeOf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public int getRadioRange() {
        return radioRange;
    }

    public void setRadioRange(int radioRange) {
        this.radioRange = radioRange;
    }

    public int getInRangeOf() {
        return inRangeOf;
    }

    public void setInRangeOf(int inRangeOf) {
        this.inRangeOf = inRangeOf;
    }
}

public class Main extends JFrame {

    int width = 960;
    int height = 960;
    int numberOfUnknownsFound = 0;

    public Main(){
        setTitle("Senzori");
        setSize(width,height);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public static int getRandomInteger(int minimum, int maximum){
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }

    public static void drawCircle(Graphics g, int x, int y, int radius) {

        int diameter = radius * 2;

        //shift x and y by the radius of the circle in order to correctly center it
        g.drawOval(x - radius, y - radius, diameter, diameter);
    }

    public static void drawRect(Graphics g, int x, int y, int radius, int id) {

        int diameter = radius * 2;

        //shift x and y by the radius of the circle in order to correctly center it
        g.fillRect(x - radius, y - radius, diameter, diameter);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(id), x-5, y+5);
        g.drawRect(x - radius, y - radius, diameter, diameter);
    }

    public static void drawFinishedRect(Graphics g, int x, int y, int radius, int id) {

        int diameter = radius * 2;

        //shift x and y by the radius of the circle in order to correctly center it
        g.setColor(Color.YELLOW);
        g.fillRect(x - radius, y - radius, diameter, diameter);

        g.setColor(Color.BLACK);
        g.drawRect(x - radius, y - radius, diameter, diameter);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(id), x-5, y+5);
    }

    public int getNumberOfUnknownsFound() {
        return numberOfUnknownsFound;
    }

    public void setNumberOfUnknownsFound(int numberOfUnknownsFound) {
        this.numberOfUnknownsFound = numberOfUnknownsFound;
    }

    public void paint(Graphics g){
        int shirina = 800;
        int visina = 800;

        g.setColor(Color.BLACK);
        g.drawRect(width-shirina, height-visina, width-(2*(width-shirina)), height-(2*(height-visina)));

        int brojNaJazli = 20;
        int frakcijaNaAnchorJazli = 50;//vo procenti
        int radioRange = 200;

        int goleminaNaSenzor = 10;
        int goleminaNaPredvidenSenzor = 8;
        int brojNaAnchorJazli = (int)(brojNaJazli * (frakcijaNaAnchorJazli/100f));

        int brojNaNepoznatiJazli = brojNaJazli - brojNaAnchorJazli;

        AnchorNode nizaJazli[] = new AnchorNode[brojNaAnchorJazli+brojNaNepoznatiJazli];

        for(int i=0;i<brojNaAnchorJazli;i++) {
            AnchorNode anchorNode = new AnchorNode(i, getRandomInteger((width-shirina)+(goleminaNaSenzor*2), shirina-goleminaNaSenzor*2),
                    getRandomInteger((height-visina)+(goleminaNaSenzor*2),visina-goleminaNaSenzor*2),true, -1, -1, radioRange, 0);
            nizaJazli[i] = anchorNode;
        }

        for(int i=brojNaAnchorJazli;i<brojNaNepoznatiJazli+brojNaAnchorJazli;i++) {
            AnchorNode anchorNode = new AnchorNode(i, -1, -1,false, getRandomInteger((width-shirina)+(goleminaNaSenzor*2), shirina-goleminaNaSenzor*2),
                    getRandomInteger((height-visina)+(goleminaNaSenzor*2),visina-goleminaNaSenzor*2), radioRange, 0);
            nizaJazli[i] = anchorNode;
        }

        for(int i=0;i<brojNaAnchorJazli;i++){
            String x[] = String.valueOf(nizaJazli[i].getX()).split("\\.");
            String y[] = String.valueOf(nizaJazli[i].getY()).split("\\.");
            g.setColor(Color.blue);
            drawCircle(g, Integer.parseInt(x[0]), Integer.parseInt(y[0]), radioRange);
            g.setColor(Color.green);
            drawRect(g, Integer.parseInt(x[0]), Integer.parseInt(y[0]), goleminaNaSenzor, nizaJazli[i].getId());
        }

        for(int i=brojNaAnchorJazli;i<brojNaNepoznatiJazli+brojNaAnchorJazli;i++){
            String x[] = String.valueOf(nizaJazli[i].getX1()).split("\\.");
            String y[] = String.valueOf(nizaJazli[i].getY1()).split("\\.");
            g.setColor(Color.red);
            drawRect(g, Integer.parseInt(x[0]), Integer.parseInt(y[0]), goleminaNaSenzor, nizaJazli[i].getId());
        }

        double evklidovoRastojanie = 0;

        double greskaNaLokalizacija = 0;

        for(int p=0;p<brojNaNepoznatiJazli;p++) {

            for(int i=0;i<nizaJazli.length;i++){
                for(int j=0;j<nizaJazli.length;j++){
                    if (nizaJazli[j].isFlag() == false && nizaJazli[i].isFlag() == true) {
                        evklidovoRastojanie = Math.sqrt(Math.pow(nizaJazli[i].getX()-nizaJazli[j].getX1(), 2)+Math.pow(nizaJazli[i].getY()-nizaJazli[j].getY1(), 2));
                        if(nizaJazli[j].getRadioRange()>evklidovoRastojanie) {
                            nizaJazli[j].setInRangeOf(nizaJazli[j].getInRangeOf()+1);
                            if(nizaJazli[j].getInRangeOf()==1)
                                System.out.println("ID: " + nizaJazli[j].getId() + ", Evklidovo rastojanie: " + evklidovoRastojanie + ", Vo opseg na senzor so ID: " + nizaJazli[i].getId() + ", Vkupen opseg od " + nizaJazli[j].getInRangeOf() + " senzor.");
                            else
                                System.out.println("ID: " + nizaJazli[j].getId() + ", Evklidovo rastojanie: " + evklidovoRastojanie + ", Vo opseg na senzor so ID: " + nizaJazli[i].getId() + ", Vkupen opseg od " + nizaJazli[j].getInRangeOf() + " senzora.");
                        }
                    }
                }
            }

            for (int i = 0; i < nizaJazli.length; i++) {
                int brojSenzori = 0;
                int id = 0;

                for (int j = 0; j < nizaJazli.length; j++) {
                    if (nizaJazli[i].getInRangeOf() > 2) {
                        if ((nizaJazli[i].isFlag() == true || nizaJazli[j].isFlag() == true) &&
                                ((nizaJazli[i].isFlag() == true && nizaJazli[j].isFlag() == false) || (nizaJazli[i].isFlag() == false && nizaJazli[j].isFlag() == true))) {
                            evklidovoRastojanie = Math.sqrt(Math.pow(nizaJazli[i].getX1() - nizaJazli[j].getX(), 2) + Math.pow(nizaJazli[i].getY1() - nizaJazli[j].getY(), 2));
                            if (nizaJazli[j].getRadioRange() > evklidovoRastojanie) {

                                brojSenzori++;

                            }
                        }
                    }
                }

                double positions[][] = new double[brojSenzori][brojSenzori];
                double distances[] = new double[brojSenzori];

                int counter = 0;
                for (int j = 0; j < nizaJazli.length; j++) {
                    if (nizaJazli[i].getInRangeOf() > 2) {
                        if ((nizaJazli[i].isFlag() == true || nizaJazli[j].isFlag() == true) &&
                                ((nizaJazli[i].isFlag() == true && nizaJazli[j].isFlag() == false) || (nizaJazli[i].isFlag() == false && nizaJazli[j].isFlag() == true))) {
                            evklidovoRastojanie = Math.sqrt(Math.pow(nizaJazli[i].getX1() - nizaJazli[j].getX(), 2) + Math.pow(nizaJazli[i].getY1() - nizaJazli[j].getY(), 2));
                            if (nizaJazli[j].getRadioRange() > evklidovoRastojanie) {
                                System.out.println("Lokaliziran od senzor: " + nizaJazli[j].getId());
                                positions[counter][0] = nizaJazli[j].getX();
                                positions[counter][1] = nizaJazli[j].getY();
                                distances[counter] = evklidovoRastojanie;
                                counter++;

                                id = nizaJazli[i].getId();

                            }
                        }
                    }
                }

                if (positions.length > 2) {
                    NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());

                    Optimum optimum = solver.solve();

                    double[] answer = optimum.getPoint().toArray();

                    System.out.println("X na senzor so ID: " + nizaJazli[i].getId() + " X: " + nizaJazli[i].getX1());
                    System.out.println("Y na senzor so ID: " + nizaJazli[i].getId() + " Y: " + nizaJazli[i].getY1());
                    System.out.println("Presmetan X na senzor so ID: " + nizaJazli[i].getId() + " X: " + answer[0]);
                    nizaJazli[i].setX(answer[0]);
                    System.out.println("Presmetan Y na senzor so ID: " + nizaJazli[i].getId() + " Y: " + answer[1]);
                    nizaJazli[i].setY(answer[1]);
                    String x[] = String.valueOf(answer[0]).split("\\.");
                    String y[] = String.valueOf(answer[1]).split("\\.");
                    g.setColor(Color.YELLOW);
                    drawFinishedRect(g, Integer.parseInt(x[0]), Integer.parseInt(y[0]), goleminaNaPredvidenSenzor, id);
                    setNumberOfUnknownsFound(getNumberOfUnknownsFound() + 1);

                    drawCircle(g, Integer.parseInt(x[0]), Integer.parseInt(y[0]), radioRange);
                    nizaJazli[i].setFlag(true);
                    nizaJazli[i].setInRangeOf(0);

                    System.out.println("=====================================>                             Lokaliziran " + nizaJazli[i].getId() + " senzor");
                    System.out.println("Lokalizirano od " + p + " red.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
                }

            }

            for(int i=0;i<nizaJazli.length;i++){
                nizaJazli[i].setInRangeOf(0);
            }
        }

        for (int i = 0; i < nizaJazli.length; i++) {
            if (nizaJazli[i].getX1() > 0 && nizaJazli[i].getY1() > 0) {
                if (nizaJazli[i].getX() > 0 && nizaJazli[i].getY() > 0) {
                    if (nizaJazli[i].getX() != nizaJazli[i].getX1() || nizaJazli[i].getY() != nizaJazli[i].getY1()) {
                        greskaNaLokalizacija = greskaNaLokalizacija + Math.sqrt(Math.pow(nizaJazli[i].getX1() - nizaJazli[i].getX(), 2) + Math.pow(nizaJazli[i].getY1() - nizaJazli[i].getY(), 2));
                    }
                }
            }
        }

        System.out.println("Sredna vrednost na greska na lokalizacija: " + (greskaNaLokalizacija / (brojNaJazli - brojNaAnchorJazli)) * (100.0 / radioRange));

    }

    public static void main(String[] args){
        Main m = new Main();
        try {
            m.paint(null);
        }catch (Exception e) {}
    }
}