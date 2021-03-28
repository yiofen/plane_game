package game;
import java.awt.Graphics;
/**
 * 爆炸类
 */
import java.awt.Image;

public class Explode {
	double x,y;
	
	/**
	 * 用static可以避免重复加载
	 */
	static Image[] imgs = new Image[16];
	static{
		for(int i=0;i<16;i++){
			imgs[i] = GameUtil.getImage("Image/explode/e"+(i+1)+".gif");
			imgs[i].getWidth(null);
		}
	}
	
	int count;//计数
	
	public void draw(Graphics g){
		if(count<=15){
			g.drawImage(imgs[count], (int)x, (int)y, null);
			count++;
		}
	}
	
	public Explode(double x,double y){
		this.x = x;
		this.y = y;
	}
}
