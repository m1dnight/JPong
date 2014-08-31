package engine;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Particle
{
	private static final int D = 16;
	public static final int R = 8;
	private static final double MIN_SPEED = 1.0D;
	private static final double MAX_SPEED = 8.0D;
	public double mass;
	public double direction;
	public double speed;
	private static double acceleration = 1.02D;
	public Point2D.Double location;
	public Point center;
	private int cWidth;
	private int cHeight;
	private long startTime;
	private AudioClip sound1;
	private AudioClip sound2;
	public boolean doUpdateColor = true;
	public Color color;
	public boolean enabled = true;

	public Particle(int cW, int cH)
	{
		this.cWidth = cW;
		this.cHeight = cH;
		this.center = new Point(cW / 2, cH / 2);
		this.location = new Point2D.Double(cW / 2, cH / 2);
		this.mass = 1.0D;
		randomDirection();
		randomSpeed();
		updateColor(this.doUpdateColor);
		this.sound2 = Applet.newAudioClip(getClass().getResource("c.wav"));
		this.sound1 = Applet.newAudioClip(getClass().getResource("b.wav"));
	}

	public void updateColor(boolean tf)
	{
		if (!tf)
		{
			return;
		}
		int r = (int) (this.speed * 255.0D / 8.0D);
		r = Math.min(r, 255);
		this.color = new Color(r, 10, 10);
	}

	public boolean isTouching(Particle p)
	{
		if (this.location.distanceSq(p.location) <= 256.0D)
		{
			return true;
		}
		return false;
	}

	private void randomDirection()
	{
		this.direction = (Math.random() * 2.0D * 3.141592653589793D);
	}

	private void randomSpeed()
	{
		this.speed = (1.0D + Math.random());
	}

	public void move(boolean canAccelerate)
	{
		double dx = this.speed * Math.cos(this.direction);
		double dy = this.speed * Math.sin(this.direction);
		// Hitting the side. Reflect on 90° surface.
		if ((this.location.x + dx > this.cWidth - 8)
				|| (this.location.x + dx < 8.0D))
		{
			reflect(1.570796326794897D, true, canAccelerate);
			dx = -dx;
		}
		// Hitting the top or bottom. Reflecting on 0° angle.
		if ((this.location.y + dy > this.cHeight - 8)
				|| (this.location.y + dy < 8.0D))
		{
			reflect(0.0D, true, canAccelerate);
			dy = -dy;
		}
		this.location.x += dx;
		this.location.y += dy;

		this.center.x = ((int) this.location.x);
		this.center.y = ((int) this.location.y);
	}

	public Point2D.Double getVelocity()
	{
		return new Point2D.Double(this.speed * Math.cos(this.direction),
				this.speed * Math.sin(this.direction));
	}

	public void draw(Graphics g)
	{
		g.setColor(this.color);
		g.fillOval(this.center.x - 8, this.center.y - 8, 16, 16);
	}

	public void reflect(double surfaceAngle, boolean doAddError,
			boolean doAccelerate)
	{
		this.sound1.play();
		this.direction = (2.0D * surfaceAngle - this.direction + (doAddError ? 0.001D
				: 0.0D)
				* Math.random());
		this.direction %= 6.283185307179586D;
		if ((doAccelerate) && (this.speed < 8.0D))
		{
			this.speed *= acceleration;
			updateColor(this.doUpdateColor);
		}
	}

	public void collideWith(Particle p)
	{
		double sx = this.location.x - p.location.x;
		double sy = this.location.y - p.location.y;

		double tx = p.location.y - this.location.y;
		double ty = this.location.x - p.location.x;

		double sux = sx / Math.sqrt(sx * sx + sy * sy);
		double suy = sy / Math.sqrt(sx * sx + sy * sy);

		double tux = tx / Math.sqrt(tx * tx + ty * ty);
		double tuy = ty / Math.sqrt(tx * tx + ty * ty);

		Point2D.Double v1a = getVelocity();
		Point2D.Double v2a = p.getVelocity();

		double v1s = v1a.x * sux + v1a.y * suy;

		double v1t = v1a.x * tux + v1a.y * tuy;

		double v2s = v2a.x * sux + v2a.y * suy;

		double v2t = v2a.x * tux + v2a.y * tuy;

		double v1sb = (2.0D * p.mass * v2s + this.mass * v1s - p.mass * v1s)
				/ (this.mass + p.mass);
		double v2sb = (2.0D * this.mass * v1s + p.mass * v2s - this.mass * v2s)
				/ (this.mass + p.mass);

		double nsx = v1sb * sux;
		double nsy = v1sb * suy;
		double ntx = v1t * tux;
		double nty = v1t * tuy;

		double v1bx = nsx + ntx;
		double v1by = nsy + nty;
		this.speed = Math.sqrt(v1bx * v1bx + v1by * v1by);
		updateColor(this.doUpdateColor);
		this.direction = Math.atan2(v1by, v1bx);

		nsx = v2sb * sux;
		nsy = v2sb * suy;
		ntx = v2t * tux;
		nty = v2t * tuy;

		double v2bx = nsx + ntx;
		double v2by = nsy + nty;
		p.speed = Math.sqrt(v2bx * v2bx + v2by * v2by);
		p.updateColor(p.doUpdateColor);
		p.direction = Math.atan2(v2by, v2bx);
		if (Math.abs(v1s) + Math.abs(v2s) < 4.0D)
		{
			this.sound1.play();
		} else
		{
			this.sound2.play();
		}
		while (isTouching(p))
		{
			move(true);
			p.move(true);
		}
	}

	public void dampBump(Particle p)
	{
		double sx = this.location.x - p.location.x;
		double sy = this.location.y - p.location.y;

		double tx = p.location.y - this.location.y;
		double ty = this.location.x - p.location.x;

		double sux = sx / Math.sqrt(sx * sx + sy * sy);
		double suy = sy / Math.sqrt(sx * sx + sy * sy);

		double tux = tx / Math.sqrt(tx * tx + ty * ty);
		double tuy = ty / Math.sqrt(tx * tx + ty * ty);

		Point2D.Double v1a = getVelocity();
		Point2D.Double v2a = p.getVelocity();

		double v1s = v1a.x * sux + v1a.y * suy;

		double v1t = v1a.x * tux + v1a.y * tuy;

		double v2s = v2a.x * sux + v2a.y * suy;

		double v2t = v2a.x * tux + v2a.y * tuy;
		double v1sb = 0.0D;
		double v2sb = 0.0D;
		if (Math.abs(v1s * this.mass - v2s * p.mass) <= 8.0D * (p.mass + this.mass) / 2.0D)
		{
			collideWith(p);
			return;
		}
		v1sb = (2.0D * p.mass * v2s + this.mass * v1s - p.mass * v1s)
				/ (this.mass + p.mass);
		v2sb = (2.0D * this.mass * v1s + p.mass * v2s - this.mass * v2s)
				/ (this.mass + p.mass);
		v1sb = v1sb * this.mass / (this.mass + p.mass);
		v2sb = v2sb * p.mass / (this.mass + p.mass);

		double nsx = v1sb * sux;
		double nsy = v1sb * suy;
		double ntx = v1t * tux;
		double nty = v1t * tuy;
		double v1bx = nsx + ntx;
		double v1by = nsy + nty;
		this.speed = Math.sqrt(v1bx * v1bx + v1by * v1by);
		updateColor(this.doUpdateColor);
		this.direction = Math.atan2(v1by, v1bx);

		nsx = v2sb * sux;
		nsy = v2sb * suy;
		ntx = v2t * tux;
		nty = v2t * tuy;

		double v2bx = nsx + ntx;
		double v2by = nsy + nty;
		p.speed = Math.sqrt(v2bx * v2bx + v2by * v2by);
		p.updateColor(p.doUpdateColor);
		p.direction = Math.atan2(v2by, v2bx);
		if (Math.abs(v1s) + Math.abs(v2s) < 4.0D)
		{
			this.sound1.play();
		} else
		{
			this.sound2.play();
		}
		while (isTouching(p))
		{
			move(true);
			p.move(true);
		}
	}
}
