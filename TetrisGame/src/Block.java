import org.eclipse.swt.graphics.Color;

public class Block { // ����� ������ ���������� ����� �������� ����

	private Color color; // ����
	private boolean filled; // ������������� (0-����, 1-� ������ ����� ���� ������/������� ����)

	Block(Color new_color, boolean new_filled) {//�����������
		color = new_color;
		filled = new_filled;
	}
	
	public boolean getFilled() { // ����� � ����� ������ � ������
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

