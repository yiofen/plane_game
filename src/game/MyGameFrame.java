package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * �ɻ���Ϸ��������	
 * @author Mr.Yu
 *
 */
public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("Image/plane.png");
	Image bg = GameUtil.getImage("Image/bg.jpg");
	/**
	 * ͼƬ��˫����ṹ��important��
	 */
	private Image offScreenImage = null;
	
	Plane plane = new Plane(planeImg,300,500);//�ɻ���
	Shell[] shells = new Shell[50];//�ڵ����ʼ��	
	Explode exp;//��ը��
	Date startTime = new Date();
	Date endTime;
	int period;//��Ϸ������ʱ��
	
	@Override
	public void paint(Graphics g) {//�Զ�������---g�൱��һ֧����
		super.paint(g);
		
		Color c = g.getColor();
		
		g.drawImage(bg, 0, 0, null);
		plane.drawSelf(g);//���ɻ�
		for(int i=0;i<shells.length;i++){
			shells[i].draw(g);	
		
			//�ɻ����ڵ�����ײ���
			boolean bump = shells[i].getRect().intersects(plane.getRect());
			if(bump){
				plane.live = false;
				
				if(exp == null){
					exp = new Explode(plane.x, plane.y);
					endTime = new Date();
					period = (int)((endTime.getTime() - startTime.getTime())/1000);
				}
				exp.draw(g);
			}
			
			if(!plane.live){
				//��ȡ��Ϸ��ʱ����޸�����
				g.setColor(Color.red);
				Font f = new Font("����", Font.BOLD, 55);
				g.setFont(f);
				g.drawString("ʱ�䣺"+period+"��", Constant.TIME_X, Constant.TIME_Y);
			}
		}	
		g.setColor(c);
	}
	
	//�����ػ����ڡ��������ڲ���
	class PaintThread extends Thread{		
		
		@Override
		public void run(){
			while(true){
				repaint();//�ػ�����
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ���̼������ڲ���
	 */
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}		
	}
	
	/**
	 * ��ʼ������
	 */
	public void launchFrame(){
		this.setTitle("�ɻ���Ϸ����");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		this.setLocation(700,300);
		
		//���ڼ������ڲ��࣬�������ŵ�ʱ����Ӧ
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		new PaintThread().start();//�����ػ����ڵ��߳�
		addKeyListener(new KeyMonitor());//��������Ӽ��̼���
		
		//��ʼ��50���ڵ�
		for(int i=0;i<shells.length;i++){
			shells[i] = new Shell();
		}
	}
	
	public void update(Graphics g){
		if(offScreenImage == null)
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//������Ϸ���ڵĿ�Ⱥ͸߶�
		
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}
}
