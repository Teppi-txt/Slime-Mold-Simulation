import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.util.ArrayList;

public class CFrame extends JPanel implements ActionListener{

    ArrayList<Agent> agents = new ArrayList<Agent>();
    int agentCount = 5000;
    int msPerFrame = 20;
    float diminishingRate = 0.9999F;

    int panelWidth = 1450;
    int panelHeight = 800;

    BufferedImage drawing = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = drawing.createGraphics();

    public static void main(String[] args) {
        CFrame c = new CFrame();
    }

    public void paint(Graphics g) {
        //g.fillOval(10, 10, 100, 100);
        super.paintComponent(g);
        g.drawImage(drawing, 0, 0, this);
    }
    public CFrame() {
        JFrame frame = new JFrame("Window");
        createBufferImage();

        frame.setSize(panelWidth, panelHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setBackground(Color.black);
        frame.setUndecorated(true);


        for (int i = 0; i < agentCount; i++) {
            agents.add(new Agent(panelWidth, panelHeight, drawing));
        }

        Timer t = new Timer(msPerFrame, this);
        t.start();
        frame.add(this);
        frame.setVisible(true);
    }

    private void createBufferImage() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, panelWidth, panelHeight);
    }

    private void updateTexture() {
        for (Agent slime : agents) {
            slime.map = drawing;
            slime.paint(g2d);
        }
    }

    public void darkenImage() {
        RescaleOp op = new RescaleOp(diminishingRate, 0, null);
        drawing = op.filter(drawing, null);
        g2d = drawing.createGraphics();
    }

    public void blurImage() {
        float[] matrix = new float[9];
        for (int i = 0; i < 9; i++) matrix[i] = 1.0f/9.0f;
        BufferedImage blurBuffer = new BufferedImage(panelWidth + 1, panelHeight + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bbg = blurBuffer.createGraphics();
        bbg.setColor(Color.BLACK);
        bbg.drawRect(0, 0, panelWidth + 10, panelHeight + 10);
        bbg.drawImage(drawing, 1, 1, null);

        //BufferedImageOp op = new ConvolveWithEdgeOp(new Kernel(3, 3, matrix), ConvolveOp.EDGE_REFLECT, null);
        BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null );
        blurBuffer = op.filter(blurBuffer, null);
        drawing = blurBuffer.getSubimage(1, 1, drawing.getWidth(), drawing.getHeight());
        g2d = drawing.createGraphics();
//        for (int x = 0; x < drawing.getWidth(); x++) {
//            for (int y = 0; y < drawing.getHeight(); y++) {
//                int sum = 0; int count = 0;
//                for (int xoffset = -1; xoffset <= 1; xoffset++) {
//                    for (int yoffset = -1; yoffset <= 1; yoffset++) {
//                        if (x + xoffset >= 0 && x + xoffset < panelWidth && y + yoffset >= 0 && y + yoffset < panelHeight) {
//                            int pixelColor = drawing.getRGB(x + xoffset, y + yoffset);
//                            count += 1;
//                            //since its either black or white or in between, all the RGB values are the same
//                            sum += (pixelColor >> 16) & 0xff;
//                        }
//                    }
//                }
//                drawing.setRGB(x, y, new Color(sum/count, sum/count, sum/count).getRGB());
//            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateTexture();
        repaint();
        darkenImage();
        blurImage();
    }
}