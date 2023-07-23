import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HappyFacesButtons extends JFrame {
	// 세개의 버튼
	JButton startButton;
	JButton stopButton;
	JButton resumeButton;
	Timer t;					// 애니메이션을 위한 타이머
	final int SIZE = 50;		// 그림의 크기
	ArrayList<NewPosImageIcon> list = new ArrayList<NewPosImageIcon>();	// 그림객체가 들어가는 리스트	

	// 생성자에서 GUI구성등 초기화 진행
	public HappyFacesButtons() {
		DrawPanel panel = new DrawPanel();			// 그림이 그려질 패널
		JPanel buttonsPanel = new JPanel();			// 버튼이 들어갈 패널
		startButton = new JButton("시작");
		stopButton = new JButton("중지");
		resumeButton = new JButton("계속");
		buttonsPanel.add(startButton);
		buttonsPanel.add(resumeButton);
		buttonsPanel.add(stopButton);

		this.add(BorderLayout.CENTER, panel);
		this.getContentPane().add(BorderLayout.SOUTH, buttonsPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);

		// 움직임을 위한 타이머 생성, 핸들러 장착, 및 시작. 10밀리초마다 MyClass에 있는 actionPerformed() 메소드가 실행됨
		t = new Timer(10, new MyClass());
		// t.start();						// 시작 버튼을 눌러야만 시작되도록 함

		// 버튼을 눌렀을 때, 작동하는 핸들러 장착 
		ButtonListener bl = new ButtonListener();
		startButton.addActionListener(bl);
		resumeButton.addActionListener(bl);
		stopButton.addActionListener(bl);
		
	}

	// 여기서부터 프로그램 실행
	public static void main(String[] args) {
		new HappyFacesButtons();
	}

	// Timer와 연관된 핸들러
	private class MyClass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			repaint();					// 포함하는 프레임이 다시 그려지게 함.
		}								// 이에 의해 포함된 DrawPanel의 paintComponent 메소드가 실행됨
	}

	// 버튼들과 연관된 핸들러
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == startButton) {
				listInit();				// Happyface 그림 리스트에 초기화
				t.start();				// 타이머 시작			
			}
			if (e.getSource() == stopButton)
				t.stop();				// 타이머를 멈춤
			else if (e.getSource() == resumeButton)
				t.restart();			// 타이머 다시시작
		}	
	}

	// ArrayList에 초기 5개의 Happyface 그림을 넣어주는 메소드. 기존에 있는 것은 모두 없애고 시작함
	private void listInit() {
		list.clear();  					// 기존의 리스트는 전부 없앰
		for (int i=0; i<5; i++) {
			int x = (int) (Math.random()*400);
			int y = (int) (Math.random()*400);		
			list.add(new NewPosImageIcon("HappyFace.gif", x, y, SIZE, SIZE));
		}		
	}
	// 중앙위치 패널
	class DrawPanel extends JPanel {
		public void paintComponent(Graphics g) {	// 이 메소드는 화면이 리프레쉬될때마다 실행됨
			int w = this.getWidth();				// 현재의 패널 넓이 획득
			int h= this.getHeight();				// 현재의 패널 높이 획득
			g.setColor(Color.white);				
			g.fillRect(0,0,w,h);					// 흰색으로 칠해 줌
			g.setColor(Color.black);
			g.drawLine(0, 0, w, 0);					// 구분선도 칠해 줌

			// 좌표를 조정하고 그림을 그려줌
			for (NewPosImageIcon pi : list) {
				pi.move(pi.moveX, pi.moveY);
				if (pi.pX <= 0 || pi.pX >= w-SIZE)
					pi.moveX = pi.moveX * -1;
				if (pi.pY <= 0 || pi.pY >= h-SIZE)
					pi.moveY = pi.moveY * -1;
				pi.draw(g);							// 그림을 그림
			}
		}
	}
	private class NewPosImageIcon extends ImageIcon implements KeyListener{
		int pX;				// 그림의 X좌표
		int pY;				// 그림의 y좌표
		int width;			// 그림의 넓이
		int height;			// 그림의 높이
		int moveX=1, moveY=1; // 그림의 이동방향 및 보폭

		@Override
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		
		// 생성자
		public NewPosImageIcon(String img, int x, int y, int width, int height) {
			super(img);
			pX=x;
			pY=y;
			this.width = width;
			this.height = height;
		}
		// 그림의 이동
		public void move(int x, int y) {
			pX += x;
			pY += y;
		}
		// 그림그리기
		public void draw(Graphics g) {
			g.fillOval(pX, pY, width, height);

		}
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP: 
				pX -= 100; System.out.println("위"); break;
			case KeyEvent.VK_DOWN:
				pY += 100; System.out.println("아래"); break;
			}
			
		}
	}
}
