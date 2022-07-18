import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Button extends Rectangle {


	private static final long serialVersionUID = 1L;
	private Font font;
	private Color color, bgColor;
	private boolean selected;
	private boolean available = true;
	private boolean visible = true;
	private String text;
	private boolean clearBackground;
	
	public Button(String text, int x, int y, int width, int height, Font font, Color color, Color bgColor, boolean clearBackground) {
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
	
	public Button(String text, int x, int y, Font font, Color color, Color bgColor, boolean clearBackground) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.font = font;
		this.color = color;
		this.bgColor = bgColor;
		this.clearBackground = clearBackground;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void render(Graphics g) {
		if (width==0 || height==0) {
			FontMetrics temp = g.getFontMetrics();
			width = temp.stringWidth(text);
			height = temp.getHeight();
		}
		
		if (visible) {
			if (available) {
				if(selected) {
					if (!clearBackground) {
						float brightenFactor = 0.40f;
						int re = (int)(Math.min(255, bgColor.getRed() + 255 * brightenFactor));
						int gr = (int)(Math.min(255, bgColor.getGreen() + 255 * brightenFactor));
						int bl = (int)(Math.min(255, bgColor.getBlue() + 255 * brightenFactor));
						g.setColor(new Color(re, gr, bl));
						g.fillRoundRect(x, y, width, height, 20, 20);
						g.setColor(color);
						g.setFont(font);
						FontMetrics fm = g.getFontMetrics();
						g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
					} else {
						g.setColor(color);
						g.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize()+2));
						FontMetrics fm = g.getFontMetrics();
						g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
					}

				} else {
					if (!clearBackground) {
						g.setColor(bgColor);
						g.fillRoundRect(x, y, width, height, 20, 20);
					}
					g.setColor(color);
					g.setFont(font);
					FontMetrics fm = g.getFontMetrics();
					g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
				}

			} else { // Draw a grayed out button
				if (!clearBackground) {
					float brightenFactor = 0.50f;
					int re = (int)(Math.min(255, bgColor.getRed() + 255 * brightenFactor));
					int gr = (int)(Math.min(255, bgColor.getGreen() + 255 * brightenFactor));
					int bl = (int)(Math.min(255, bgColor.getBlue() + 255 * brightenFactor));
					g.setColor(new Color(re, gr, bl));
					g.fillRoundRect(x, y, width, height, 20, 20);
					g.setColor(Color.GRAY);
					g.setFont(font);
					FontMetrics fm = g.getFontMetrics();
					g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
				} else {
					g.setColor(Color.GRAY);
					g.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize()+2));
					FontMetrics fm = g.getFontMetrics();
					g.drawString(text, x + (width-fm.stringWidth(text))/2, y + height - (height-fm.getAscent())/2 - 3 );
				}
			}
		}
	}
	
	
}
