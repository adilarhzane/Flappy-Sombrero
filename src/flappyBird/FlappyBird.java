package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
	
	public static FlappyBird flappyBird;

	public final int WIDTH = 1000, HEIGHT = 800;

	public Renderer renderer;

	public Rectangle bird;

	public ArrayList<Rectangle> columns, columnsUpper, columnsLower;

	public int ticks, yMotion, score, highScore;

	public boolean gameOver, started, jumping = false;

	public Random rand;

	Image sombrero;
	Image sombrero1;
	Image pipe;
	Image pipe1;
	
	public FlappyBird()
	{
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Flappy Sombrero");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 80, 50);
		columns = new ArrayList<Rectangle>();
		columnsUpper = new ArrayList<Rectangle>();
		columnsLower = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		sombrero = Toolkit.getDefaultToolkit().createImage("sombrero.png");
		sombrero1 = Toolkit.getDefaultToolkit().createImage("sombrero1.png");
		pipe = Toolkit.getDefaultToolkit().createImage("pipe.png");
		pipe1 = Toolkit.getDefaultToolkit().createImage("pipe1.png");

		timer.start();
		
		
	}
	
	public void addColumn(boolean start)
	{
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start)
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * space, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * space, 0, width, HEIGHT - height - space));
		}
		else
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintUpperColumn(Graphics g, Rectangle column)
	{
		g.drawImage(pipe1, column.x, column.y, column.width, column.height, null);
	}

	public void paintLowerColumn(Graphics g, Rectangle column)
	{
		g.drawImage(pipe, column.x, column.y, column.width, column.height, null);
	}
	
	public void jump()
	{
		if (gameOver)
		{
			bird.setBounds(WIDTH / 2 - 10, HEIGHT / 2 - 10, 80, 50);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started)
		{
			started = true;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int speed = 10;

		ticks++;

		if (started)
		{
			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);

					if (column.y == 0)
					{
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;

			for (Rectangle column : columns)
			{
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
				{
					score += 1;
					highScore = Math.max(highScore, score);
				}


				if (column.intersects(bird))
				{
					gameOver = true;

					if (bird.x <= column.x)
					{
						bird.x = column.x - bird.width;

					}
					else
					{
						if (column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height)
						{
							bird.y = column.height;
						}
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0)
			{
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 150;
				gameOver = true;
			}
		}

		renderer.repaint();
	}

	public void repaint(Graphics g)
	{
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		
		if (jumping) 
		{
			g.drawImage(sombrero1, bird.x, bird.y, bird.width, bird.height, null);
		}
		else
		{
			g.drawImage(sombrero, bird.x, bird.y, bird.width, bird.height, null);
		} 
		
		for(Rectangle column : columns)
		{
			paintLowerColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (!started)
		{
			g.drawString("Click to start!", 175, HEIGHT / 2 - 50);
		}

		if (gameOver)
		{
			g.drawString("Game Over Esè!", 100, HEIGHT / 2 - 50);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Click to start", 330, HEIGHT / 2);
		}

		if (!gameOver && started)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
			g.setFont(new Font("Arial", 1, 20));
			g.drawString("Highscore: " + String.valueOf(highScore), WIDTH - 200, 50);
		}
	}
	public static void main(String[] args)
	{
		flappyBird = new FlappyBird();
		System.out.println("BAJS");
		Sound.sound1.loop();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
		jumping = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
			jumping = false;
			System.out.println(columns.size());
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		jumping = true;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		jumping = false;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

}