package imgproc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KMeans {
	
	int clusters;
	
	List<Point> points;
	
	Map<Point, Group> groups = new HashMap<Point, Group>();

	Random r = new Random(System.currentTimeMillis());

	public KMeans(List<Point> points, int clusters) {
		this.points = points;
		this.clusters = clusters;
	}

	public KMeans() {
	}

	public Map<Point, Group> getGroups() {
		return groups;
	}

	public void cluster() {

		Point[] centers = new Point[clusters];
		// generate random centers
		for (int i = 0; i < centers.length; i++) {
			centers[i] = points.get(r.nextInt(points.size()));
		}
		int loop = 0;
		while (true) {
			// cluster groups & generate new averaged centers
			Point[] newCenters = cluster(points, centers);

			if (stable(newCenters, centers)) {
				System.out.println(" dumps");
				break;
			}
			centers = newCenters;
			loop++;
			if (loop % 100 == 0)
				System.out.println(loop);
		}

	}

	private boolean stable(Point[] newCenters, Point[] centers) {
		boolean stable = true;
		for (int i = 0; i < centers.length; i++) {
			if (!centers[i].closeEnough( newCenters[i], Shirts.TOLERANCE)) {
				stable = false;
			}
		}
		return stable;
	}

	public Point[] cluster(List<Point> points, Point[] centers) {
		
		groups = new HashMap<Point, Group>();
		
		for (int i = 0; i < centers.length; i++) {
			groups.put(centers[i], new Group(centers[i]));
		}

		for (int i = 0; i < points.size(); i++) {
			Point closestCenter = points.get(i).closestTo(centers);
			groups.get(closestCenter).addPoint(points.get(i));
		}

		Point[] newCenters = new Point[centers.length];
		for (int i = 0; i < centers.length; i++) {
			newCenters[i] = groups.get(centers[i]).getAveragedCenter();
		}
		return newCenters;
	}

}
