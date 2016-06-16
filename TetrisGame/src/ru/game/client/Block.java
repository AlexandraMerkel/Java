package ru.game.client;
import org.eclipse.swt.graphics.Color;

public class Block { // класс одного отдельного блока игрового поля

	private Color color; // цвет
	private boolean filled; // заполненность (0-пуст, 1-в данном блоке есть фигура/границы поля)

	Block(Color newColor, boolean newFilled) {//конструктор
		color = newColor;
		filled = newFilled;
	}
	
	public boolean getFilled() { // здесь и далее гетеры и сетеры
		return filled;
	}
	
	public void setFilled(boolean newFilled) {
		this.filled = newFilled;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color newColor) {
		this.color = newColor;
	}
}

