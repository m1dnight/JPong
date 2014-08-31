package engine;

public interface Collision
{
	boolean collidesWith(Collision object);
	
	boolean inHitbox(int x, int y);
}
