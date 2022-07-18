import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TextField extends Rectangle implements KeyListener {

	private static final long serialVersionUID = 1L;
	MainGame game;
	private char lastChar;
	private Font font;
	private Color color, bgColor;
	private boolean selected; // Mouse over
	private boolean active; // Clicked on
	private boolean visible = true;
	private String text = null;
	
	public TextField(MainGame game, int x, int y, int width, int height, Font font, Color color, Color bgColor) {
		this.game = game;
		game.addKeyListener(this);
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.color = color;
		this.bgColor = bgColor;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	
	public void keyPressed(KeyEvent e) {
		// Do nothing
	}
	
	public void keyReleased(KeyEvent e) {
		// Do nothing
	}
	
	public void keyTyped(KeyEvent e) {
		if (active) {
			lastChar = e.getKeyChar();
			if (lastChar != '\n') {
				if ((text == null) && (lastChar != '\b'))
					text = Character.toString(lastChar);
				else if ((text.length()<14) && (lastChar != '\b'))
					text = text + Character.toString(lastChar);
				else if ((text != null) && (lastChar == '\b'))
					text = text.substring(0, text.length()-1);
			}
		}
		
	}
	
	public String getText() {
		return text;
	}
	
	public void resetText() {
		text = null;
	}
	
	
	public void render(Graphics g) {
		
		if (visible) {
			if(active) {
				float brightenFactor = 0.40f;
				int re = (int)(Math.min(255, bgColor.getRed() + 255 * brightenFactor));
				int gr = (int)(Math.min(255, bgColor.getGreen() + 255 * brightenFactor));
				int bl = (int)(Math.min(255, bgColor.getBlue() + 255 * brightenFactor));
				g.setColor(new Color(re, gr, bl));
				g.fillRect(x, y, width, height);
				g.setColor(color);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				if (text == null)
					g.drawString(Character.toString('|'), x + 1, y + height - (height-fm.getAscent())/2 - 3 );
				else
					g.drawString(text + '|', x + 1, y + height - (height-fm.getAscent())/2 - 3 );

			} else if (selected) { // Mouse over
				float brightenFactor = 0.40f;
				int re = (int)(Math.min(255, bgColor.getRed() + 255 * brightenFactor));
				int gr = (int)(Math.min(255, bgColor.getGreen() + 255 * brightenFactor));
				int bl = (int)(Math.min(255, bgColor.getBlue() + 255 * brightenFactor));
				g.setColor(new Color(re, gr, bl));
				g.fillRect(x, y, width, height);
				g.setColor(color);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				if (text != null)
					g.drawString(text, x + 1, y + height - (height-fm.getAscent())/2 - 3 );
				
			} else { // Inactive
				g.setColor(bgColor);
				g.fillRect(x, y, width, height);
				g.setColor(color);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				if (text != null)
					g.drawString(text, x + 1, y + height - (height-fm.getAscent())/2 - 3 );
			}
		}
		
	}
	
}
