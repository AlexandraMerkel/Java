package ru.game.client;
import org.eclipse.swt.graphics.Color;

public class Block { // ����� ������ ���������� ����� �������� ����

	private Color color; // ����
	private boolean filled; // ������������� (0-����, 1-� ������ ����� ���� ������/������� ����)

	Block(Color newColor, boolean newFilled) {//�����������
		color = newColor;
		filled = newFilled;
	}
	
	public boolean getFilled() { // ����� � ����� ������ � ������
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

