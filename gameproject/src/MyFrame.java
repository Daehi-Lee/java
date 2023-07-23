import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;







public class MyFrame extends JFrame implements MouseListener{

	
	final int WIDTH = 1300;
	final int HEIGHT = 800;
	Timer t;
	JButton sB;
	JButton rB;
	JButton qB;
	JButton ruleB;
	int timeForEGen = 0;
	int pOfMeX;
	int pOfMeY;
	int pOfBulletX;
	int pOfBulletY;
	int fireB;
	int shape = 3;
	int score = 0;
	double amountOfE = 5.0;
	int fireRange = 100;
	int level = 1;
	int eYSpeed;
	int eXSpeed;
	int inkScore = 0;
	int timeForStarD = 0;
	boolean maxSpeed = true;
	boolean endGame = true;
	boolean startGame = true;
	boolean gotStar = true;
	
	
	PosImageIcon bGround = new PosImageIcon("하늘.png",0,0,1300,800);
	PosImageIcon gameOver = new PosImageIcon("게임오버.jpg",0,0,1300,800);
	PosImageIcon startBGround = new PosImageIcon("Paper_Airplane.png",0,0,1300,800);
	ArrayList<PosImageIcon> sList = new ArrayList<>();
	ArrayList<PosImageIcon> list = new ArrayList<>();
	ArrayList<PosImageIcon> bombList = new ArrayList<>();
	shooter s = new shooter("종이비행기.png", 0, 0, 200, 200);
	bullet b = new bullet();
	enemyGen eG;
	levelUpStar itemS;
	bomb itemB;
	MainPanel panel;
	JPanel bPanel;
	
	public static void main(String[] args) {									//메인함수
		MyFrame m = new MyFrame();
		
	}
	
	public MyFrame() {															//프레임
			setTitle("Paper Shooting");
			
			
			panel = new MainPanel();
			bPanel = new JPanel();
			
			mouseAction mMove = new mouseAction();
			panel.addMouseListener(this);
			panel.addMouseMotionListener(mMove);
			
			sB = new JButton("시작");
			rB = new JButton("계속");
			qB = new JButton("중지");
			
			this.setLayout(new BorderLayout());
			bPanel.add(sB);
			bPanel.add(rB);
			bPanel.add(qB);
			this.add(BorderLayout.CENTER, panel);
			this.getContentPane().add(BorderLayout.SOUTH, bPanel);
			setSize(WIDTH, HEIGHT);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			
			ButtonListener bL = new ButtonListener();
			sB.addActionListener(bL);
			rB.addActionListener(bL);
			qB.addActionListener(bL);
			t = new Timer(10, new myClass());

	}
	
	private class myClass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			timeForEGen += 10;
			repaint();					// 포함하는 프레임이 다시 그려지게 함.
		}	
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == sB) {
				list.removeAll(list);
				sList.removeAll(sList);
				score = 0;
				timeForEGen = 0;
				b.bulletSpeed = 5;
				endGame = true;
				eYSpeed = -1;
				eXSpeed = -1;
				t.restart();
				t.start();
				amountOfE = 5.0;
				eG = new enemyGen();
				startGame = false;
				timeForEGen = 0;
				
				
			}
			else if (e.getSource() == qB) {
				t.stop();				
			}
			else if (e.getSource() == rB) {
				t.restart();
			}
		}	
	}
	class MainPanel extends JPanel {
		public void paintComponent(Graphics g) { 						//페인트 컴포넌트: 동작화면
			
			if(startGame) {
				startBGround.draw(g);
			}
			
			g.drawLine(pOfMeX+100, pOfMeY, pOfMeX+100, pOfMeY+1000);
			int w = this.getWidth();
			int h= this.getHeight();				
			g.setColor(Color.white);				
			g.fillRect(0,0,w,h);
			g.setColor(Color.black);
			g.drawLine(0, 0, w, 0);	
			
			
			bGround.draw(g);
			
			for (PosImageIcon pi : list) {								//적 생성 및 게임오버
				pi.move(pi.moveX, pi.moveY);
				if (pi.pX <= 50) {
					panel.removeAll();
					list.clear();
					sList.clear();
					bombList.clear();
					endGame = false;
					gameOver.draw(g);
					g.setColor(Color.RED);					
					g.setFont(new Font("굴림체", Font.BOLD, 100));
					g.drawString("<게임오버>", 420, 200);
					g.setColor(Color.BLACK);	
					g.setFont(new Font("맑은 고딕", Font.ITALIC, 30));
					g.drawString("< 점수 = " + score + " >", 520, 340);
					g.drawString("< 최고탄속 = " + b.bulletSpeed + " >", 520, 400);
					g.drawString("< 피한 잉크자국 = " + inkScore + " >", 500, 460);
					t.stop();
					
					break;
				}
				

				if (pi.pY <= 0 || pi.pY >= h-55)
					pi.moveY = pi.moveY * -1;
				pi.draw(g);							
			}
			if(endGame) {
				g.setFont(new Font("굴림체", Font.ITALIC, 25));
				g.drawString("점수 = " + score, 1100, 30);
			}
			
			
			for (PosImageIcon piS : sList) {
				piS.move(piS.moveX, piS.moveY);
				if (piS.pX <= 30) {
					b.bulletSpeed += level;
					sList.remove(piS);

					
				}
				if (piS.pY <= 0 || piS.pY >= h-55)
					piS.moveY = piS.moveY * -1;
				piS.draw(g);			
			}
			
			for (PosImageIcon piB : bombList) {
				piB.move(piB.moveX-1, piB.moveY);
				if (piB.pX <= -200) {
					bombList.remove(piB);
					inkScore++;
				}
				if (piB.pY <= 0 || piB.pY >= h-200)
					piB.moveY = piB.moveY * -1;
				piB.draw(g);			
			}
			
			if(timeForEGen >= 2000) {
				timeForEGen = 0;
				eG = new enemyGen();
			}
			if(score >= 3000) {
				amountOfE += 0.001;
			}
			if(score % 3000 == 0 && score >= 3000) {
				eYSpeed -= 0.5;
				eXSpeed -= 0.5;
			}
			
			s.draw(g);
			
			score++;
			int levelIS = (int)(Math.random()*(400)+1);
			if(score >= 2000) {
				if( levelIS > 399) {
					itemS = new levelUpStar();
				}
			}
			int levelIB = (int)(Math.random()*(500)+1);
			if(score >= 5000) {
				if( levelIB > 499) {
					itemB = new bomb();
				}
			}
			
			for(PosImageIcon i:list) {										// 적기 격추
				if(b.pOfBulletX+55 > i.pX-5 && b.pOfBulletX+55 < i.pX+40 && b.pOfBulletY > i.pY && b.pOfBulletY-5 < i.pY+50) {
					list.remove(i);
					break;
				}
			}
			for(PosImageIcon i:sList) {										// 레벨업
				
				if(b.bulletSpeed >= 5) {
					//g.drawRect(i.pX, i.pY, 100, 100);						히트박스
					if(b.pOfBulletX+55 > i.pX && b.pOfBulletX+55 < i.pX+50 && b.pOfBulletY > i.pY && b.pOfBulletY-5 < i.pY+50) {
						sList.remove(i);
						if(b.bulletSpeed == 15) {
							g.setFont(new Font("굴림체", Font.ITALIC, 25));
							g.drawString("SCORE 감소 ▼ ", 1100, 50);
							score -= 100;
						}
						break;
					}
				}
			}
			
			if(fireB == 1) {								//탄속, 최대탄속 제한, 벽에 부딪히면 자동으로 돌아오기
				
				b.pOfBulletX += b.bulletSpeed;
				b.drawBullet(g,10,10);
				if(b.bulletSpeed >= 15) {
					b.bulletSpeed = 15;
				}
			}
			else if(fireB == 0 && b.pOfBulletX > -10) {
				b.pOfBulletX -= b.bulletSpeed-1;
				b.drawBullet(g,10,10);
				if(b.bulletSpeed >= 15) {
					b.bulletSpeed = 15;
				}
			}
			if(b.pOfBulletX > 1218) {
				fireB = 0;
			}
			if(startGame) {
				startBGround.draw(g);
			}

		}
	}
	private class PosImageIcon extends ImageIcon {
		int pX;				// ImageIcon의 X좌표
		int pY;				// ImageIcon의 y좌표
		int width;			// ImageIcon의 넓이
		int height;			// ImageIcon의 높이
		
		int moveX = eXSpeed;		// 적기 속도
		int moveY = eYSpeed;
		
		public int getPX() {
			return pX;
		}
		
		public PosImageIcon(String img, int x, int y, int width, int height) {
			super(img);	
			pX=x;
			pY=y;
			this.width = width;
			this.height = height;
		}
		

		public void move(int x, int y) {
			pX += x;
			pY += y;
		}
		
 
		public void draw(Graphics g) {
			g.drawImage(this.getImage(), pX, pY, width, height, null);
		}
	}

	
	public class enemyGen {												//적기 생성
		public void enemyClear() {
			list.clear();
		}
		public enemyGen() {										
			for(int i = 0; i < (int)amountOfE; i++) {
				int y = (int) (Math.random()*(HEIGHT-180));
				int x = (int) (Math.random()*50);
				list.add(new PosImageIcon("삼각형.png", 1250+x, y, 200, 200));
			}
		}
		
		
	}
	
	
	public class shooter extends PosImageIcon {							// 유저 생성
		int myW;
		int myH;
		
		
		
		public shooter(String img, int x, int y, int width, int height) {
			super(img, x, y, width, height);		
			pOfMeX=x;
			pOfMeY=y;
			this.myW = width;
			this.myH = height;
		}
		
		public void draw(Graphics g) {
			if(endGame) {
				g.drawImage(this.getImage(), 0, pOfMeY, myW, myH, null);
				g.setColor(Color.red);
				g.drawLine(30, pOfMeY+22, 1300, pOfMeY+22);
				g.setColor(Color.BLACK);
				g.drawLine(50, 0, 50, 800);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(b.bulletSpeed);
		b.boomerang(e.getY());
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("중지");

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("계속");
	}
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(b.bulletSpeed);
		b.boomerang(e.getY());
		fireB = 1;

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		fireB = 0;
	}
	
	public class mouseAction implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			pOfMeX = e.getX()-22;
			pOfMeY = e.getY()-22;
			b.boomerang(e.getY());
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			pOfMeX = e.getX()-22;
			pOfMeY = e.getY()-22;
			b.boomerang(e.getY());
		}
	}
	public class bullet {
		int pOfBulletX;
		int pOfBulletY;
		int bulletSpeed = 5;
		
		public void drawBullet(Graphics g,int x, int y) {
			if(endGame) {
				g.fillOval(pOfBulletX+55, pOfBulletY-5, 10, 10);
			}
		}
		
		public void getMouseP(int eX, int eY) {
			pOfBulletX = eX;
			pOfBulletY = eY;
		}
		public void boomerang(int y) {
			pOfBulletY = y;
		}
	}
	
	public class levelUpStar{											//레벨업 스타 생성
		public void starClear() {
			sList.clear();
		}
		public levelUpStar() {
			int y = (int) (Math.random()*(HEIGHT-180));
			sList.add(new PosImageIcon("별.png", 1160, y, 200, 200));
		}
				
	}
	
	public class bomb{													//잉크 얼룩 생성
		public void bombClear() {
			sList.clear();
		}
		public bomb() {
			int y = (int) (Math.random()*(HEIGHT-180));
			bombList.add(new PosImageIcon("얼룩.png", 1160, y, 200, 200));
		}
				
	}
	
	
}
