import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class MondrianArtGenerator{
	
	private static final int MAX_DEPTH = 5;
	private static final int MAX_COLOR_DIFF = 60;
	private static final int MAX_THICKNESS = 4;
	private static final Random random = new Random();
	
	
	public static void main(String[] args) {
		int width = 800;
		int height = 600;
		Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW};
		MondrianArtGenerator generator = new MondrianArtGenerator();
		generator.generate(width, height, colors);
	}
	
	public void generate(int width, int height, Color[] colors){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		drawRectangle(g, 0, 0, width, height, Color.WHITE);
		divide(g, 0, 0, width, height, colors, 0);
		saveImage(image);
	}
	
	private void saveImage(BufferedImage image) {
		try{
			File output = new File("MondrianArt.png");
			ImageIO.write(image, "png", output);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void drawRectangle(Graphics g, int x, int y, int width, int height, Color color){
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	private void drawLine(Graphics g, int x, int y, int length, int thickness, Color color, double angle){
		g.setColor(color);
		int endX = (int) (x + length * Math.cos(angle));
		int endY = (int) (y + length * Math.sin(angle));
		Graphics graphics = g.create();
		graphics.translate(x, y);
		graphics.rotate(angle);
		graphics.fillRect(0, -thickness / 2, length, thickness);
		graphics.dispose();
	}
	
	private void divide(Graphics g, int x, int y, int width, int height, Color[] colors, int depth){
		if (depth >= MAX_DEPTH) {
			return;
		}
		int colorIndex = random.nextInt(colors.length);
		Color color = colors[colorIndex];
		drawRectangle(g, x, y, width, height, color);
		int colorDiff = random.nextInt(MAX_COLOR_DIFF);
		
		Color newColor1 = new Color(Math.min(color.getRed() + colorDiff, 255),
				Math.min(color.getGreen() + colorDiff, 255),
				Math.min(color.getBlue() + colorDiff, 255));
		
		Color newColor2 = new Color(Math.max(color.getRed() - colorDiff, 0),
				Math.max(color.getGreen() - colorDiff, 0),
				Math.max(color.getBlue() - colorDiff, 0));
		
		double angle = random.nextDouble() * Math.PI / 2;
		int thickness = random.nextInt(MAX_THICKNESS) + 1;
		
		if (width > height){
			int split = (int) (width * random.nextDouble());
			divide(g, x, y, split, height, colors, depth + 1);
			divide(g, x + split, y, width - split, height, colors, depth + 1);
			drawLine(g, x + split, y, height, thickness, newColor1, angle);
			drawLine(g, x + split, y + height, height, thickness, newColor2, angle);
		}
		else{
			int split = (int) (height * random.nextDouble());
			divide(g, x, y, width, split, colors, depth + 1);
			divide(g, x, y + split, width, height - split, colors, depth + 1);
			drawLine(g, x, y + split, width, thickness, newColor2, angle);
			drawLine(g, x, y, width, thickness, newColor1, angle);
		}
	}
}
