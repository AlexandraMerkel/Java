import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

public class Tetramino { // ����� ������� ������

	public int x;
	public int y;
	public Color color;
	private Composite figure;// ��� ����������

	// ������� �������-������ ��� ������� ����� (���� ������ ������� �� 4-� ��������� ������)
	boolean[][] field;

	Tetramino(Composite new_figure, int new_x, int new_y) { // �����������
		x = new_x;
		y = new_y;
		figure = new_figure;
		createTetramino();
	}

	// �-� �������� ������ 
	public void rotate() {
		boolean[][] new_field1 = new boolean[4][4];//���. �������
		boolean[][] new_field2 = new boolean[4][4];
		// ���� ��������
		for (int i = 0; i < 4; i++) { // 
			for (int j = 0; j < 4; j++) {
				new_field1[3 - j][i] = field[i][j];
			}
		}
		field = new_field1;
		
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
				new_field2[i - shift][j] = field[i][j];
			}
		}	
		field = new_field2;
	}
	
	// �-� �������� ������ (��������)
	private void createTetramino() {
		field = new boolean[4][4];
		int form = (int) Math.ceil(Math.random() * 7); // ������ ������ ��������� ����� � ��������� [0,1)*7=[0;7), ceil ������� ���������� ������
	
		// �����
		if (form == 1) {
			field[0][0] = true;
			field[0][1] = true;
			field[1][0] = true;
			field[1][1] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		} 
		// � ������������
		else if (form == 2) {
			field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			field[1][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
		} 
		// Z (������������)
		else if (form == 3) {
			field[0][0] = true;
			field[1][0] = true;
			field[1][1] = true;
			field[2][1] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_RED);
		} 
		// � ������ (������� ������)
		else if (form == 4) {
			field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			field[2][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW);
		} 
		// � ������ (������� �����)
		else if (form == 5) {
			field[0][0] = true;
			field[0][1] = true;
			field[1][1] = true;
			field[2][1] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);
		}
		// Z
		else if (form == 6) {
			field[0][1] = true;
			field[1][1] = true;
			field[1][0] = true;
			field[2][0] = true;
			color = figure.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		}
		// ����� (I)	
		else if (form == 7) {
				field[0][0] = true;
				field[0][1] = true;
				field[0][2] = true;
				field[0][3] = true;
				color = figure.getDisplay().getSystemColor(SWT.COLOR_CYAN);
		}
	}
}