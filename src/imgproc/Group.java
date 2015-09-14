package imgproc;
import java.util.ArrayList;
import java.util.List;

public class Group {

	private Point center;
	private List<Point> points;
	
	
	public Group(Point centers) {
		this.center = centers;
		this.points = new ArrayList<Point>();
	}
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	public void addPoint(Point p){
		points.add(p);
	}
	
	public Point getAveragedCenter( ){
		double r = 0;
		for ( int i = 0 ; i < points.size() ; i++ ) {
			r += points.get(i).r;
		}
		r = r / points.size();
		
		double g = 0;
		for ( int i = 0 ; i < points.size() ; i++ ) {
			g += points.get(i).g;
		}
		g = g / points.size();
		
		double b = 0;
		for ( int i = 0 ; i < points.size() ; i++ ) {
			b += points.get(i).b;
		}
		b = b / points.size();
		return new Point( r,g,b);
	}

}
