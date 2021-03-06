package ru.game.client;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

public class Tetramino { // ����� ������� ������

	public int x;
	public int y;
	public Color color;
	private Composite figure;// ��� ����������
	public Shape shape;
		
	public enum Shape { // ������������ �����
	    SQUARE, Z, REVERSE_Z, REVERSE_T, I, G, REVERSE_G;
	}
	
		
	// ������� �������-������ ��� ������� ����� (���� ������ ������� �� 4-� ��������� ������)
	public boolean[][] field;
	
	public Tetramino(Composite newFigure, Shape shape, int newX, int newY) { // ����������� 1 (�� ���� ������)
		x = newX;
		y = newY;
		figure = newFigure;
		create(shape);
	}
	
	public Tetramino(Composite newFigure, int newX, int newY) { // ����������� 2 (��� ������ 2)
		x = newX;
		y = newY;
		figure = newFigure;
		generateTetramino();
	}
	
	private void create(Shape form) { 
		 field = new boolean[4][4];
		 switch(form){
    	case SQUARE: 
    		field[0][0] = true;
    		field[0][1] = true;
    		field[1][0] = true;
    		field[1][1] = true;
    		color = figure.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
    		shape = form;
    		break;
    	case REVERSE_T: 
    		field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			field[1][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
			shape = form;
			break;
    	case REVERSE_Z: 
    		field[0][0] = true;
			field[1][0] = true;
			field[1][1] = true;
			field[2][1] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_RED);
			shape = form;
			break;
    	case REVERSE_G: 
    		field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			field[2][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW);
			shape = form;
			break;
    	case G: 
    		field[0][0] = true;
			field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);
			shape = form;
			break;
    	case Z: 
    		field[0][1] = true;
			field[1][1] = true;
			field[1][0] = true;
			field[2][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_GREEN);
			shape = form;
			break;
	    case I:
	    	field[0][0] = true;
			field[0][1] = true;
			field[0][2] = true;
			field[0][3] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_CYAN);
			shape = form;
			break;
			}
	}
	
	// �-� �������� ������ 
	public void rotate() {
		boolean[][] newField1 = new boolean[4][4]; //���. �������
		boolean[][] newField2 = new boolean[4][4];
		// ���� ��������
		for (int i = 0; i < 4; i++) { // 
			for (int j = 0; j < 4; j++) {
				newField1[3 - j][i] = field[i][j];
			}
		}
		field = newField1;
		
		int shift = 3;// �����
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i < shift  && field[i][j]) {//���� ���� ��������,�� �������� ���������� ������
					shift = i;
				}
			}
		}
		// ����� ������ ������(�.�. ��-�� �������� ��� "������" ����)
		for (int i = shift; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				newField2[i - shift][j] = field[i][j];
			}
		}	
		field = newField2;
	}
	
	// �-� �������� ������ (��������)
	private void generateTetramino() {
		
		int form = (int) Math.ceil(Math.random() * 7); // ������ ������ ��������� ����� � ��������� [0,1)*7=(0;7], ceil ������� ���������� ������
		
		switch (form) {
			case 1:
				create(Shape.SQUARE); 
				break;
			case 2:
				create(Shape.Z); 
				break;
			case 3:
				create(Shape.REVERSE_Z);
				break;
			case 4:
				create(Shape.REVERSE_T);
				break;
			case 5:
				create(Shape.I);
				break;
			case 6:
				create(Shape.G);
				break;
			case 7:
				create(Shape.REVERSE_G);
				break;
		}
	}
}