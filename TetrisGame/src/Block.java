import org.eclipse.swt.graphics.Color;

public class Block { // класс одного отдельного блока игрового поля

	private Color color; // цвет
	private boolean filled; // заполненность (0-пуст, 1-в данном блоке есть фигура/границы поля)

	Block(Color new_color, boolean new_filled) {//конструктор
		color = new_color;
		filled = new_filled;
	}
	
	public boolean getFilled() { // здесь и далее гетеры и сетеры
		return filled;
	}
	
	public void setFilled(boolean new_filled) {
		this.filled = new_filled;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color new_color) {
		this.color = new_color;
	}
}

