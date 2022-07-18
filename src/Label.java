import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Label {

	private Font font;
	private Color color, bgColor;
	private boolean selected;
	private boolean visible = true;
	private String text;
	private int x, y;
	private int width = 0, height = 0;
	private boolean clearBackground;
	
	public Label(String text, int x, int y, int width, int height, Font font, Color color, Color bgColor, boolean clearBackground) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.color = color;
		this.bgColor = bgColor;
		this.clearBackground = clearBackground;
	}
	
	public Label(String text, int x, int y, Font font, Color color, Color bgColor, boolean clearBackground) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.font = font;
		this.color = color;
		this.bgColor = bgColor;
		this.clearBackground = clearBackground;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void render(Graphics g) {
		if (width==0 || height==0) {
			FontMetrics temp = g.getFontMetrics();
			width = temp.stringWidth(text);
			height = temp.getHeight();
		}
		
		if (visible) {
			if(selected) {
				if (!clearBackground) {
					g.setColor(bgColor);
					g.fillRect(x, y, width, height);
				}
				g.setColor(color);
				g.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()+2));
				FontMetrics fm = g.getFontMetrics();
				g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );

			} else {
				if (!clearBackground) {
					g.setColor(bgColor);
					g.fillRect(x, y, width, height);
				}
				g.setColor(color);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
			}
		}
	}
	
}
