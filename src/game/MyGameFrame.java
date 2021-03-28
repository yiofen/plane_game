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
 * 飞机游戏的主窗口	
 * @author Mr.Yu
 *
 */
public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("Image/plane.png");
	Image bg = GameUtil.getImage("Image/bg.jpg");
	/**
	 * 图片的双缓存结构（important）
	 */
	private Image offScreenImage = null;
	
	Plane plane = new Plane(planeImg,300,500);//飞机类
	Shell[] shells = new Shell[50];//炮弹类初始化	
	Explode exp;//爆炸类
	Date startTime = new Date();
	Date endTime;
	int period;//游戏持续的时间
	
	@Override
	public void paint(Graphics g) {//自动被调用---g相当于一支画笔
		super.paint(g);
		
		Color c = g.getColor();
		
		g.drawImage(bg, 0, 0, null);
		plane.drawSelf(g);//画飞机
		for(int i=0;i<shells.length;i++){
			shells[i].draw(g);	
		
			//飞机与炮弹的碰撞检测
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
				//获取游戏的时间和修改字体
				g.setColor(Color.red);
				Font f = new Font("宋体", Font.BOLD, 55);
				g.setFont(f);
				g.drawString("时间："+period+"秒", Constant.TIME_X, Constant.TIME_Y);
			}
		}	
		g.setColor(c);
	}
	
	//反复重画窗口————内部类
	class PaintThread extends Thread{		
		
		@Override
		public void run(){
			while(true){
				repaint();//重画窗口
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 键盘监听的内部类
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
	 * 初始化窗口
	 */
	public void launchFrame(){
		this.setTitle("飞机游戏窗口");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		this.setLocation(700,300);
		
		//窗口监听的内部类，当点击叉号的时候响应
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		new PaintThread().start();//启动重画窗口的线程
		addKeyListener(new KeyMonitor());//给窗口添加键盘监听
		
		//初始化50个炮弹
		for(int i=0;i<shells.length;i++){
			shells[i] = new Shell();
		}
	}
	
	public void update(Graphics g){
		if(offScreenImage == null)
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//这是游戏窗口的宽度和高度
		
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}
}
