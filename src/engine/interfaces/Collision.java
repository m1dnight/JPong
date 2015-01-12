package engine.interfaces;

public interface Collision
{
	boolean collidesWith(Collision object);
	
	boolean inHitbox(int x, int y);
}
