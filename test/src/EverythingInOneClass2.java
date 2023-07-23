import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class EverythingInOneClass2 extends JFrame {
	JButton sB;
	JButton rB;
	JButton qB;
	
	Timer t;
	public EverythingInOneClass2() {
		JPanel panel = new JPanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		
		this.setSize(500,500);
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		MyDrawPanelThree panel;
	}

	private class MyDrawPanelThree extends JPanel {	// static class
		@Override
		public void paintComponent(Graphics g) {
			
			Graphics2D g2d = (Graphics2D) g;

			ImageIcon img = new ImageIcon("사각형기본.png");
			g2d.drawImage(img.getImage(), 10,10, 50, 50, null);
			
			
			/* 선 그리기	
			g2d.drawLine(10,10, 100,100);
			// 선의 색 바꾸기
			g2d.setColor(Color.black);
			g2d.drawLine(10,50, 100,100);
			
			 글씨 쓰기
			g2d.drawString("야호!", 50, 50);
			 글씨 폰트 및 크기 바꾸기 - 폰트명은 C:\WINDOWS\Fonts 에서 찾을 수 있음
			g2d.setFont(new Font("궁서", Font.ITALIC, 30));
			g2d.drawString("아싸!", 50, 200);
			g2d.drawString("오케이!", 50, 400);

			 이미지 그리기
			 */
		}
	}
}