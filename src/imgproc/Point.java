package imgproc;

public class Point {
	
	
	double r,g,b;
	int x,y;
	
 	public Point(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
 	
 	public int getX() {
		return x;
	}

	public void setXY( int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double distanceTo( Point p){
 		return Math.sqrt( Math.pow( (p.r -r ), 2) 
 							+ Math.pow( (p.g -g ), 2) 
 							+ Math.pow( (p.b - b), 2) 
// 							+ Math.pow( (302 -p.x), 2)
// 							+ Math.pow( (219 -p.y), 2)
 							);
 	}
 	
 	public String toString() {
 		return "["+x+","+y+"]("+r+","+g+","+b+")";
 	}
 	
 	public Point closestTo( Point[] centers) {
 		Point c = centers[0];
 		double distance = distanceTo(c);
 		for( int i = 1 ; i < centers.length ; i++ ) {
 			double d0 = distanceTo(centers[i]);
 			if( d0 < distance ){
 				distance = d0;
 				c = centers[i];
 			}
 		}
 		return c;
 	}
 	
 	public boolean closeEnough(Point p, double TOLERANCE){
 		return distanceTo(p) < TOLERANCE;
 	}
  	
}