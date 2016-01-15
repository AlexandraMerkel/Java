import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public class TetrisCanvas
	extends Canvas
	implements PaintListener, Runnable, KeyListener, DisposeListener {

	private Tetramino tetramino;
	private boolean gameStarted;//���� �� ����
	private int slow = 500;// ����������
	private int level;// ������� (����� �� ����������, �.�. �� �������� ������� �����)
	private int x_size; // ������� ���� � ������
	private int y_size;
	private int score;//����
	private Image backimage;//������ ������ �����������
	private GC gc; // ��� ��������� �� �����������
	private Block[][] blocks;
	private Text textInfo;//���� � ������� ������ � �����

	public TetrisCanvas(Composite parent) {
		super(parent, SWT.CENTER);//�������� ����������� �����������
		gameStarted = false;
		addPaintListener(this); // ������� ��� ���������
		setLayout(new GridLayout());
		
		textInfo = new Text(this, SWT.NONE);
		textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));//���������� �� �����������, ������
				
		addKeyListener(this);// ������� ��� ������
		addDisposeListener(this);// ��� ��������
		x_size = 15;// ������� ���� (� ������)
		y_size = 24;
		
		backimage = new Image(getDisplay(), x_size * 22, y_size * 23);// ������� ������ ������ �����������
		gc = new GC(backimage); // ����� �������� �� ���
		//initializationGame();
	}

	// �������� ����
	public void initializationGame() {
		gameStarted = true;
		score = 0;
		blocks = new Block[x_size][y_size];// ������� ������ ��� ����

		// ������� ���� ���� �� ������ ������ 
		for (int i = 0; i < x_size; i++) {
			for (int j = 0; j < y_size - 1; j++) {
				blocks[i][j] = new Block( getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
			}
		}
		// ������� ������ ������� ���� (����� ��������� ������������)
		for (int i = 0; i < x_size; i++) {
			blocks[i][y_size - 1] = new Block(getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
		}
		// ������� ������� ������� ���� (����� ����� ��������� ������������)
		for (int i = 0; i < y_size; i++) {
			blocks[0][i] = new Block( getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
			blocks[x_size - 1][i] = new Block( getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
		}
		// ������� ������ ������
		createTetramino();
		// ��������� ������� ����
		getDisplay().timerExec(0, this);//�������� ����� run
	}
	
	//����� ����
		private void gameOver() {
			getDisplay().timerExec(-1, this);//�������� ����������
			gameStarted = false;
		}
		
	// ������� ����� ������ ������ ���������� ������
	private void createTetramino() {
		tetramino = new Tetramino(this, 7, 0);
	}

	// �-�, ������������� ������ � �����
	private void transformTetraminoToBlocks() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					blocks[tetramino.x + x][tetramino.y + y].setColor(tetramino.color);//���� ���������� ����� ������
					blocks[tetramino.x + x][tetramino.y + y].setFilled(true);//������ ���� �� ������
				}
			}
		}
	}
	
	// ����� ������ ����
		private void falling() {
			while (stepDown()) {
			}
		}
	
	// ��������, �������� �� ������ ����������� ������ (������ ���� ��� "������" �����)
		private boolean isDocking(int x, int y) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (tetramino.field[i][j] == true
						&& tetramino.x + i + x >= 0 
						&& blocks[tetramino.x + i + x][tetramino.y + j + y].getFilled()
							== true) {
						return true;
					}
				}
			}
			return false;
		}

	private boolean stepDown() {//�������� ����, ���� �� ������ ��� (��� ������ �����)
		if (!isDocking(0, 1)) {
			tetramino.y++;
			return true;
		} else {
			if (tetramino.y <= 3) {// game over, ���� �������� ������ 3-� ��������� �����
				gameOver();
				return false;
			}
			transformTetraminoToBlocks();// ����������� ������ � "����������" �����, ���� �������� ���
			createTetramino();// �������� ����� ������
			return false;
		}
	}
	private void stepLeft() {//�������� �����, ���� �� ������ ����� (��� ������ �����)
		if (!isDocking(-1, 0)) {
			tetramino.x--;
		}
	}
	private void stepRight() {//���������� ������
		if (!isDocking(1, 0)) {
			tetramino.x++;
		}
	}
	
	// �������� ����������� ����� � �������� ��������� �� ���� ���� ����
	private void clearFullLine(int current) {
		for (int i = current; i > 0; i--) {
			for (int j = 0; j < x_size; j++) {
				blocks[j][i].setColor(blocks[j][i - 1].getColor());//������� ���� �� ���� ����
				blocks[j][i].setFilled(blocks[j][i - 1].getFilled());// ���������� � ��������������
			}
		}
	}

	// �������� ����� (����������� ��?)
	private boolean checkingLine() {
		for (int i = 0; i < y_size - 1; i++) {
			boolean gap = false;// ������
			for (int j = 1; j < x_size - 1; j++) {
				if (!blocks[j][i].getFilled()) {
					gap = true;//���� ����� ������ � �����
				}
			}
			if (!gap) { // ���� �������� ���, �������� ������� ����� � ��������� ���� �� 1
				clearFullLine(i);
				score++;
				if (score % 10 == 0) {
					level++;// ����������� ������� ������ 10 �����
					slow -= 30;// ��������� �������� (��������� �������� ������� �����)
					if (slow < 1) slow = 1;
				}
			}
		}
		return false;
	}

	public void run() {
		stepDown();// �������� ����
		redraw();//����������� ������
		if (checkingLine()) {
			redraw();//�������� �����
		}
		getDisplay().timerExec(slow, this);// ����� � ����������� (��� ���������� �����������, � ����������� �� ������)
	}
	
	// ��������� �����
	private void drawTetramino(GC gc, Tetramino tetramino) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) 
				{
					gc.setBackground(tetramino.color);// ���� ������
					gc.fillRectangle(// �������� �������������� (�����) ��������� � ����.������ ������
						(tetramino.x + x) * 21 + 3,(tetramino.y + y) * 21 + 31,//���������� �� ���� � �� ������
						19,19); // ������ � ������
					}
			}
		}
	}

	// �������, ��������� � �������� ������
	public void keyPressed(KeyEvent e) {
			if (e.character == '�' || e.character == 'y') {// ���� �/y, �� �������� ����� ���� (������ �� ������� �� ���������)
				initializationGame();
			}
			if (e.character == ' ') { // ���� ������, �� ���������� ������ ����
				falling();
				redraw();
			} else if (e.keyCode == SWT.ARROW_UP) { // ���� �����, �� ������� ������
				tetramino.rotate();
				while (isDocking(0, 0)) {//����� ������ �� ���������� � ������ ������� ����
					tetramino.x--;
				}
				redraw();
			} else if (e.keyCode == SWT.ARROW_DOWN) {
				stepDown();
				redraw();
			} else if (e.keyCode == SWT.ARROW_LEFT) {
				stepLeft();
				redraw();
			} else if (e.keyCode == SWT.ARROW_RIGHT) {
				stepRight();
				redraw();
			}
	
	}

	public void keyReleased(KeyEvent e) {//��������, ��������� � ����������� ������
		//
	}

	//����������� �������
	public void widgetDisposed(DisposeEvent e) {
		getDisplay().timerExec(-1, this);
		gc.dispose();
		backimage.dispose();
	}

	public void paintControl(PaintEvent e) {
		if (gameStarted) {
			// ���������� ����, ���� ���� ����
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));//�����-����� ����
			gc.fillRectangle(getClientArea());//��������
			
			// ��������� ������
			for (int i = 0; i < x_size; i++) {
				for (int j = 0; j < y_size; j++) {
					gc.setBackground(blocks[i][j].getColor());
					gc.fillRectangle(i * 21 + 3, j * 21 + + 31, 19, 19);
					}
			}
						
			textInfo.setText("�������: " + level + " ����: " + score + "");//��-��� � ������� ������ � �����

			// ������
			drawTetramino(gc, tetramino);
		} else {// ���� ���� ��������
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.fillRectangle(getClientArea());
			textInfo.setText("�������� ����: " + score);
		}
		
		if (e != null)
		e.gc.drawImage(backimage, 0, 0);//������������
	}

}