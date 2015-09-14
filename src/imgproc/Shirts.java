package imgproc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class Shirts {
	static String inputFileName = "kate";
	static String inputFile = "kate.png";
	File path = new File("/home/karthik/k/imageprocessing/");

	static double TOLERANCE = 0.1;
	static int CLUSTERS = 1;

	public static void main(String[] args) throws IOException {
		Shirts shirts = new Shirts();
		shirts.toGrayScale(inputFileName);
//		shirts.clusterIntoGroups(inputFile);
		shirts.detectEdges(inputFile, inputFileName + "_edges.png");

	}

	public void toGrayScale(String infile) throws IOException {
		BufferedImage img = ImageIO.read(new File(path, inputFileName + ".jpg"));

		for (int x = 0; x < img.getWidth(); ++x)
			for (int y = 0; y < img.getHeight(); ++y) {
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				int grayLevel = (r + g + b) / 3;
				int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
				img.setRGB(x, y, gray);
			}
		ImageIO.write(img, "PNG", new File(path, infile + ".png"));

	}

	public void clusterIntoGroups(String infile) throws IOException {
		List<Point> rgb = getRGBValuesFromImage(infile);
		KMeans k = new KMeans(rgb, CLUSTERS);
		k.cluster();
		Map<Point, Group> groups = k.getGroups();

		int i = 0;
		for (Group g : groups.values()) {
			transparentize( g, infile, inputFileName + "_transparent" + i + ".png");
			overlayOverRed( inputFileName + "_transparent" + i + ".png");
			i++;
		}
	}

	private void transparentize(Group g, String inputFile, String outputFile) throws IOException {
		BufferedImage a = ImageIO.read(new File(path, inputFile));
		for (Point p : g.getPoints()) {
			Color c = new Color((int) p.r, (int) p.g, (int) p.b, 0);
//			Color c = new Color(100,100,100,0);
			a.setRGB(p.x, p.y, c.getRGB());
		}
		ImageIO.write(a, "PNG", new File(path, outputFile));
	}

	public List<Point> getRGBValuesFromImage(String inputFile) throws IOException {

		List<int[]> ret = new ArrayList<int[]>();

		BufferedImage a = ImageIO.read(new File(path, inputFile));
		int h = a.getHeight();
		int w = a.getWidth();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				try {
					int b = a.getRGB(i, j);
					Color c = new Color(b);
					ret.add(new int[] { i, j, c.getRed(), c.getGreen(), c.getBlue() });
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(i + "," + j);
					throw e;
				} catch (Exception e) {
					System.out.println("mutton ");
					throw e;
				}
			}
		}
		return convertToKmeanPoints(ret);

	}

	private List<Point> convertToKmeanPoints(List<int[]> points) {
		List<Point> ret = new ArrayList<Point>();
		for (int[] k : points) {
			Point p = new Point(k[2], k[3], k[4]);
			p.setXY(k[0], k[1]);
			ret.add(p);
		}
		return ret;
	}

	// dunno how to work this. for a rainy day
	public void detectEdges(String infile, String outfile) throws IOException {
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(0.9f);
		detector.setHighThreshold(1f);
		// apply it to an image
		detector.setSourceImage(ImageIO.read(new File(path, infile)));
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
		ImageIO.write(edges, "PNG", new File(path, outfile));
		System.out.println("edges detected");
	}

	public void overlayOverRed(String infile) throws IOException {

		// load source image
		File f = new File(path, infile);
		// System.out.println( f.getAbsolutePath());
		BufferedImage overlay = ImageIO.read(f);

		// create the background/backpanel/underlying image as 100,100,100
		BufferedImage image = new BufferedImage(overlay.getWidth(), overlay.getWidth(), BufferedImage.TYPE_INT_RGB);

		// create the new image, canvas size is the max. of both image sizes
		// (in this case, we don't really need this as we make both images of
		// the same size)
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < w; j++) {
				image.setRGB(i, j, new Color(00, 0, 200, 255).getRGB());
			}
		}
		ImageIO.write(image, "PNG", new File(path,  "background.png"));
		// create the combo image
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		// Save as new image
		ImageIO.write(combined, "PNG", new File(path, infile+"_overlaid.png"));
	}

}
