package ru.game.client;

import java.io.IOException;
//import java.net.UnknownHostException;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

import ru.game.message.Message;
import ru.game.message.Tetramino;
import ru.game.message.Message.Type;
import ru.game.message.Tetramino.Shape;

public class TetrisCanvas extends Canvas implements PaintListener, Runnable, KeyListener, DisposeListener {

	private Tetramino tetramino;
	private Client client;
	private Message message;

	private volatile boolean gameStarted;// ���� �� ����
	private volatile boolean errorWindow = false; // ������ � ��������� ���� �
													// �������
	public int gameID; // ������������� ������ (1 - ������, 2 - ������)
	private int speed = 0; // c������� ���� (�����������)

	private int level; // ������� (����� �� ����������, �.�. �� �������� �������
						// �����)
	private final int X_SIZE = 24; // ������� ���� � ������
	private final int Y_SIZE = 24;
	private int score;// ����
	private int gridColor;// ���� ���� (��� �����)
	private Image backimage;// ������ ������ �����������
	private GC gc; // ��� ��������� �� �����������
	private Block[][] blocks;
	private Text textInfo;// ���-� � ������� ������ � �����

	private Shape buffer; // ������ ������� ����� ������� ������
	private Shape[] figuresChoose = new Shape[3]; // ������
	boolean choose; // ������ �� ����� 2-� ����� (���� ���, ������ ������������
					// ��������)

	// �����������
	public TetrisCanvas(Composite parent) {
		super(parent, SWT.CENTER);// �������� ����������� �����������
		gameStarted = false;
		addPaintListener(this); // ������� ��� ���������
		setLayout(new GridLayout());

		textInfo = new Text(this, SWT.NONE);
		textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));// ����������
																					// ��
																					// �����������,
																					// ������
		addKeyListener(this);// ������� ��� ������
		addDisposeListener(this);// ��� ��������

		gridColor = SWT.COLOR_GRAY;// �� ��������� ����� ����

		backimage = new Image(getDisplay(), X_SIZE * 22, Y_SIZE * 23);// �������
																		// ������
																		// ������
																		// �����������
		gc = new GC(backimage); // ����� �������� �� ���
	}

	public void errors(String string) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Shell shell_help = new Shell(getDisplay(), SWT.TITLE | SWT.CLOSE | SWT.MIN);
				shell_help.setText("Tetris x 2: ������");
				GridLayout layout = new GridLayout(1, true);

				shell_help.setLayout(layout);
				shell_help.setBounds(300, 50, 345, 70);

				Label label = new Label(shell_help, SWT.NONE);
				label.setText(string);

				GridData data = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
				label.setLayoutData(data);

				shell_help.open();
			}
		});
	}

	// �������� ����� ������ � ���� ������� ������
	public void waitingPlayer() {
		errorWindow = false;
		gameID = 1;

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					client = new Client("127.0.0.1", 5876);
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}

				}

				try {
					client.createSession();
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							textInfo.setText("������� ������� ������...");
						}
					});
				}

				catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}

				// ������� ����� ���� � ���� �� ������
				try {
					message = client.readMessage();
				}

				catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
				// System.out.println("income message - " +
				// message.getMessageType());
				try {
					if (message.getMessageType().equals(Type.STARTED))
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								try {
									initializeGame();
								} catch (Exception e) {
									if (errorWindow == false) {
										errors("��������� �������������� ������: ���������� ��� ���");
										errorWindow = true;
									}
								}
							}
						});
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
			}
		}).start();
	}

	// ����� ������ ��� ����������� �� ������ �������
	public void connectSession() {
		gameID = 2;
		// System.out.println("Second client");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client = new Client("localhost", 5876);
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
				
				try {
					client.joinSession(1);
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
				
				try {
					client.sendMessage(new Message(Message.Type.JOIN, ""));
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
				
				try {
					message = client.readMessage();
					// System.out.println("income message - " +
					// message.getMessageType());
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
				
				if (message.getMessageType().equals(Type.STARTED))
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							try {
								joinGame();
							} catch (Exception e) {
								if (errorWindow == false) {
									errors("��������� �������������� ������: ���������� ��� ���");
									errorWindow = true;
								}
							}
						}
					});
			}
		}).start();
	}

	// �������� ����� ����
	public void initializeGame() throws IOException {
		gameStarted = true;
		score = 0;
		drawBlocks();

		createNullTetramino();

		try {
			message = client.readMessage();
			// System.out.println("income message - " +
			// message.getMessageType());
		} catch (Exception e) {
			if (errorWindow == false) {
				errors("��������� �������������� ������: ���������� ��� ���");
				errorWindow = true;
			}
		}

		processingClient(message);
		getDisplay().timerExec(0, this);// �������� ����� run
	}

	// �������������� � ������������ ����
	public void joinGame() throws IOException {
		getShell().setSize(545, 595);
		gameStarted = true;
		score = 0;
		drawBlocks();

		// ������� 3 ������ ��� ������ 2-�� ������
		for (int i = 0; i < 3; i++) {
			createTetraminoSelection(i);
			transformTetraminoToBlocks();
		}
		// ������� ������ ������ ��� �������� ���� (��������)
		createNullTetramino();

		// ��������� ������� ����
		getDisplay().timerExec(0, this); // �������� ����� run
	}

	// �-� ��������� ����
	private void gameOver() {
		getDisplay().timerExec(-1, this); // �������� ����������
		try {
			client.closeSession();
		} catch (Exception e) {
			if (errorWindow == false) {
				errors("��������� �������������� ������: ���������� ��� ���");
				errorWindow = true;
			}
		}
		gameStarted = false;
		choose = false;
		errorWindow = false;
		speed = 0;
		message = null;
	}

	public void drawBlocks() {
		blocks = new Block[X_SIZE][Y_SIZE];// ������� ������ ��� ����

		// ������� ���� ���� �� ������ ������
		for (int i = 0; i < X_SIZE; i++) {
			for (int j = 0; j < Y_SIZE - 1; j++) {
				blocks[i][j] = new Block(getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
			}
		}
		// ������� �������������� ������� ����
		for (int i = 0; i < X_SIZE; i++) {
			blocks[i][Y_SIZE - 1] = new Block(getDisplay().getSystemColor(gridColor), true);
		}

		for (int i = 17; i < X_SIZE; i++) {
			blocks[i][7] = new Block(getDisplay().getSystemColor(gridColor), true);
			blocks[i][15] = new Block(getDisplay().getSystemColor(gridColor), true);
		}

		// ������� ������������ ������� ����
		for (int i = 0; i < Y_SIZE; i++) {
			blocks[0][i] = new Block(getDisplay().getSystemColor(gridColor), true);
			blocks[14][i] = new Block(getDisplay().getSystemColor(gridColor), true);
			blocks[15][i] = new Block(getDisplay().getSystemColor(gridColor), true);
			blocks[16][i] = new Block(getDisplay().getSystemColor(gridColor), true);
		}
	}

	// �-� ���������� ���������� �����
	public void isGridEnabled(boolean grid) {
		if (grid)
			gridColor = SWT.COLOR_GRAY;
		else
			gridColor = SWT.COLOR_WHITE;
	}

	// �-� �������� "�������" (������, ���������) ������
	private void createNullTetramino() throws IOException {
		tetramino = new Tetramino(this, Shape.Z, 7, 0);
		// buffer = tetramino.shape;
		// Message message = new Message(Type.FORMNUMBER, "");
		// message.setAdditionalData(buffer);
		// client.sendMessage(message);

	}

	// �-� �������� ����� ����� ������ ���������� �������� ���� � ����������� ��
	// shape'� (��������� ���������� �� 2-�� ������)
	private void createTetramino(Shape shape) {
		tetramino = new Tetramino(this, shape, 7, 0);
	}

	// �-� �������� ����� ������ ����� �� ����� ��� 2-�� ������ (� �����������
	// �� � ������)
	private void createTetraminoSelection(int i) {
		if (i == 0) {
			tetramino = new Tetramino(this, 19, i * 8 + 2);
			if (tetramino.shape == Shape.I) {
				Shape shape = tetramino.shape;
				tetramino = new Tetramino(this, shape, 20, i * 8 + 2);
			}
			figuresChoose[i] = tetramino.shape;
		}
		if (i == 1) {
			tetramino = new Tetramino(this, 19, i * 8 + 2);
			while (tetramino.shape == figuresChoose[0]) {
				tetramino = new Tetramino(this, 19, i * 8 + 2);
			}
			if (tetramino.shape == Shape.I) {
				Shape shape = tetramino.shape;
				tetramino = new Tetramino(this, shape, 20, i * 8 + 2);
			}
			figuresChoose[i] = tetramino.shape;
		}
		if (i == 2) {
			tetramino = new Tetramino(this, 19, i * 8 + 2);
			while (tetramino.shape == figuresChoose[0] || tetramino.shape == figuresChoose[1]) {
				tetramino = new Tetramino(this, 19, i * 8 + 2);
			}
			if (tetramino.shape == Shape.I) {
				Shape shape = tetramino.shape;
				tetramino = new Tetramino(this, shape, 20, i * 8 + 2);
			}
			figuresChoose[i] = tetramino.shape;
		}
	}

	// �������� �����, ��� ������ �����
	private void clearTetraminoSelection() {
		for (int i = 17; i < X_SIZE - 1; i++) {
			for (int j = 1; j < Y_SIZE - 1; j++) {
				if (j != 7 && j != 15 && blocks[i][j].getFilled()) {
					blocks[i][j] = new Block(getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
				}
			}
		}
	}

	// �-�, ������������� �������� ������ � ����� ����
	private void transformTetraminoToBlocks() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					blocks[tetramino.x + x][tetramino.y + y].setColor(tetramino.color);// ����
																						// ����������
																						// �����
																						// ������
					blocks[tetramino.x + x][tetramino.y + y].setFilled(true);// ������
																				// ����
																				// ��
																				// ������
				}
			}
		}
	}

	// ��������, �������� �� ������ ����������� ������ (������ ���� ��� "������"
	// �����)
	private boolean isTouching(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (tetramino.field[i][j] == true && tetramino.x + i + x >= 0
						&& blocks[tetramino.x + i + x][tetramino.y + j + y].getFilled() == true) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean stepDown() {// ������ ��������� ����, ���� �� ��������� ���
		// (��� ������ "������"
		// �����)
		if (!isTouching(0, 1)) {
			tetramino.y++;
			return true;
		} else { // ���� ������ �������� ���, ��:
			if (tetramino.y <= 3) {// game over, ���� �������� ������ 3-�
				// ��������� �����
				gameOver();
				return false;
			}
			if (gameID == 2) {
				if (choose == false) { // ���� 2-� ����� �� ������ �����, ��
										// ������ ����������� ��������
					int random = (int) Math.floor(Math.random() * 3);
					buffer = figuresChoose[random];
					message = new Message(Message.Type.FORMNUMBER, "");
					message.setAdditionalData(buffer);
					try {
						client.sendMessage(message);
					} catch (Exception e) {
						if (errorWindow == false) {
							errors("��������� �������������� ������: ���������� ��� ���");
							errorWindow = true;
						}
					}
				}
			}
			transformTetraminoToBlocks();
			choose = false;
			clearTetraminoSelection();
			for (int i = 0; i < 3; i++) {
				createTetraminoSelection(i);
				transformTetraminoToBlocks();
			}
		}
		if (gameID == 1) {
			int counter = 0;
			while (buffer == null) {
				try {
					counter++;
					if (counter > 3) {
						errors("����� 2 ����������. ���������� ����� ���������.");
						gameOver();
						getDisplay().dispose();
					}
					//if (client.hasMessage()) {
						message = client.readMessage();
						processingClient(message);
					//}
				} catch (Exception e) {
					if (errorWindow == false) {
						errors("��������� �������������� ������: ���������� ��� ���");
						errorWindow = true;
					}
				}
			}
		}
		transformTetraminoToBlocks();
		createTetramino(buffer);
		buffer = null;
		return false;
	}

	private void stepLeft() {// �������� �����, ���� �� ������ ����� (��� ������
								// �����)
		if (!isTouching(-1, 0)) {
			tetramino.x--;
		}
	}

	private void stepRight() {// ���������� ������
		if (!isTouching(1, 0)) {
			tetramino.x++;
		}
	}

	// �������� ����������� ����� � �������� ��������� �� ���� ���� ����
	private void clearFullLine(int current) {
		for (int i = current; i > 0; i--) {
			for (int j = 0; j < X_SIZE - 10; j++) {
				blocks[j][i].setColor(blocks[j][i - 1].getColor());// �������
																	// ���� ��
																	// ���� ����
				blocks[j][i].setFilled(blocks[j][i - 1].getFilled());// ����������
																		// �
																		// ��������������
			}
		}
	}

	// �������� ����� (����������� ��?)
	private void checkLine() {
		boolean gap = false;
		for (int i = 0; i < Y_SIZE - 1; i++) {
			gap = false;// ������
			for (int j = 1; j < X_SIZE - 10; j++) {
				if (!blocks[j][i].getFilled()) {
					gap = true;// ���� ����� ������ � �����
				}
			}
			if (!gap) { // ���� �������� ���, �������� ������� ����� � ���������
						// ���� �� 1
				clearFullLine(i);
				scoreUp();

			}
		}
	}

	// �-� ���������� ���-�� �����
	private void scoreUp() {
		score += 10;
		if (score % 100 == 0) {
			level++;// ����������� ������� ������ 100 �����
			speedUp();
		}
	}

	// �-� ���������� �������� ����
	private void speedUp() {
		speed += 30;
		if (speed > 499)
			speed = 499;
	}

	// ��������� ���������
	private void processingClient(Message answer) {
		if (answer.getMessageType().equals(Type.KEYNUMBER)) {
			// System.out.println("income message - " + answer.getMessageType()
			// + " + " + answer.getMessage());
			keyP(answer.getMessage());
		}
		if (answer.getMessageType().equals(Type.SUCCESS)) {
			// System.out.println("income message - " +
			// answer.getMessageType());
		} else if (answer.getMessageType().equals(Type.FORMNUMBER)) {
			// System.out.println("income message lalala - " +
			// answer.getMessageType() + " + " + answer.getMessage());
			buffer = (Shape) answer.getAdditionalData();
			choose = true;
		}

	}

	public void run() {
		if (gameStarted) {
			iteration();
		}
	}

	public void iteration() {
		if (gameID == 1) {
			try {
				if (client.hasMessage()) {
					try {
						message = client.readMessage();
					} catch (Exception e) {
						if (errorWindow == false) {
							errors("��������� �������������� ������: ���������� ��� ���");
							gameOver();
							errorWindow = true;
						}
					}
					processingClient(message);
				}
			}

			catch (Exception e) {
				if (errorWindow == false) {
					errors("��������� �������������� ������: ���������� ��� ���");
					gameOver();
					errorWindow = true;
				}
			}
			stepDown();// �������� ����
			redraw();// ����������� ������
			checkLine(); // �������� ����������� ������
			getDisplay().timerExec(500 - speed, this);
		} else if (gameID == 2) {
			try {
				if (client.hasMessage()) {
					try {
						processingClient(client.readMessage());

					} catch (Exception e) {
						if (errorWindow == false) {
							errors("��������� �������������� ������: ���������� ��� ���");
							gameOver();
							errorWindow = true;
						}
					}
				}
			} catch (Exception e) {
				if (errorWindow == false) {
					errors("��������� �������������� ������: ���������� ��� ���");
					gameOver();
					errorWindow = true;
				}
			}
			stepDown();// �������� ����
			redraw();// ����������� ������
			checkLine(); // �������� ����������� ������
			getDisplay().timerExec(500 - speed, this);
		}
	}

	// ��������� �����
	private void drawTetramino(GC gc, Tetramino tetramino) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					gc.setBackground(tetramino.color);// ���� ������
					gc.fillRectangle(// �������� �������������� (�����)
										// ��������� � ����.������ ������
							(tetramino.x + x) * 21 + 3, (tetramino.y + y) * 21 + 31, // ����������
																						// ��
																						// ����
																						// �
																						// ��
																						// ������
							19, 19); // ������ � ������
				}
			}
		}
	}

	public void keyP(String str) {

		if (str.equals("vverh")) { // ���� �����, �� �������
									// ������
			tetramino.rotate();
			while (isTouching(0, 0)) {// ����� ������ �� ���������� � ������
										// ������� ����
				tetramino.x--;
			}
			redraw();
		} else if (str.equals("vniz")) {
			stepDown();
			redraw();
		} else if (str.equals("vlevo")) {
			stepLeft();
			redraw();
		} else if (str.equals("vpravo")) {
			stepRight();
			redraw();
		}
	}

	// �������, ��������� � �������� ������ (������� �� id ������!)
	public void keyPressed(KeyEvent key) {
		if (gameID == 1) { // �������� 1-�� ������
			message = new Message(Message.Type.KEYNUMBER, "");

			if (key.keyCode == SWT.ARROW_UP) { // ���� �����, �� �������
												// ������
				tetramino.rotate();
				while (isTouching(0, 0)) {// ����� ������ �� ���������� � ������
											// ������� ����
					tetramino.x--;
				}
				redraw();
				message.setMessage("vverh");
			} else if (key.keyCode == SWT.ARROW_DOWN) {
				stepDown();
				redraw();
				message.setMessage("vniz");
			} else if (key.keyCode == SWT.ARROW_LEFT) {
				stepLeft();
				redraw();
				message.setMessage("vlevo");
			} else if (key.keyCode == SWT.ARROW_RIGHT) {
				stepRight();
				redraw();
				message.setMessage("vpravo");
			}
			try {
				client.sendMessage(message);
			} catch (Exception e) {
				if (errorWindow == false) {
					errors("��������� �������������� ������: ���������� ��� ���");
					gameOver();
					errorWindow = true;
				}
			}
		}
		if (gameID == 2) { // �������� 2-�� ������
			message = new Message(Message.Type.FORMNUMBER, "");
			if (key.keyCode == SWT.ARROW_UP) { // ���� �����, �� �������� 1-�
												// ������
				if (choose == false) {
					buffer = figuresChoose[0];
					choose = true; // ����� ������!

				}

			} else if (key.keyCode == SWT.ARROW_DOWN) { // ���� ����, ��
														// ��������
														// 3-�
														// ������
				if (choose == false) {
					buffer = figuresChoose[2];
					choose = true;
				}

			} else if (key.keyCode == SWT.ARROW_LEFT || key.keyCode == SWT.ARROW_RIGHT) { // ����
																							// ������/�����,
																							// ��
																							// ��������
																							// 2-�
																							// ������
				if (choose == false) {
					buffer = figuresChoose[1];
					choose = true;

				}
			}
			message.setAdditionalData(buffer);
			try {
				client.sendMessage(message);
			} catch (Exception e) {
				if (errorWindow == false) {
					errors("��������� �������������� ������: ���������� ��� ���");
					gameOver();
					errorWindow = true;
				}
			}
		}

	}

	public void keyReleased(KeyEvent e) {// ��������, ��������� � �����������
											// ������
		//
	}

	// ����������� �������
	public void widgetDisposed(DisposeEvent e) {
		getDisplay().timerExec(-1, this);
		gc.dispose();
		backimage.dispose();
	}

	public void paintControl(PaintEvent e) {
		if (gameStarted) {
			// ���������� ����, ���� ���� ����
			gc.setBackground(getDisplay().getSystemColor(gridColor));
			gc.fillRectangle(getClientArea());// ��������

			// ��������� ������
			for (int i = 0; i < X_SIZE; i++) {
				for (int j = 0; j < Y_SIZE; j++) {
					gc.setBackground(blocks[i][j].getColor());
					gc.fillRectangle(i * 21 + 3, j * 21 + 31, 19, 19);
				}
			}

			textInfo.setText("�������: " + level + " ����: " + score + "");// ��-���
																			// �
																			// �������
																			// ������
																			// �
																			// �����
			// ������
			drawTetramino(gc, tetramino);
		} else {// ���� ���� ��������
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
			gc.fillRectangle(getClientArea());
			textInfo.setText("�������� ����: " + score);
		}

		if (e != null)
			e.gc.drawImage(backimage, 0, 0);// ������������
	}

}