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
	private boolean gameStarted;//идет ли игра
	private int slow = 500;// замедление
	private int level;// уровень (вляет на замедление, т.е. на скорость падения фигур)
	private int x_size; // размеры поля в блоках
	private int y_size;
	private int score;//очки
	private Image backimage;//объект класса изображений
	private GC gc; // для рисования на изображении
	private Block[][] blocks;
	private Text textInfo;//инфа о текущем уровне и счете

	public TetrisCanvas(Composite parent) {
		super(parent, SWT.CENTER);//вызываем конструктор суперкласса
		gameStarted = false;
		addPaintListener(this); // лисенер для рисования
		setLayout(new GridLayout());
		
		textInfo = new Text(this, SWT.NONE);
		textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));//заполнение по горизонтали, сверху
				
		addKeyListener(this);// лисенер для клавиш
		addDisposeListener(this);// для очищения
		x_size = 15;// размеры поля (в блоках)
		y_size = 24;
		
		backimage = new Image(getDisplay(), x_size * 22, y_size * 23);// создаем объект класса изображений
		gc = new GC(backimage); // будем рисовать на нем
		//initializationGame();
	}

	// начинаем игру
	public void initializationGame() {
		gameStarted = true;
		score = 0;
		blocks = new Block[x_size][y_size];// создаем массив для поля

		// создаем само поле из пустых блоков 
		for (int i = 0; i < x_size; i++) {
			for (int j = 0; j < y_size - 1; j++) {
				blocks[i][j] = new Block( getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
			}
		}
		// создаем нижнюю границу поля (блоки считаются заполненными)
		for (int i = 0; i < x_size; i++) {
			blocks[i][y_size - 1] = new Block(getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
		}
		// создаем боковые границы поля (блоки также считаются заполненными)
		for (int i = 0; i < y_size; i++) {
			blocks[0][i] = new Block( getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
			blocks[x_size - 1][i] = new Block( getDisplay().getSystemColor(SWT.COLOR_GRAY), true);
		}
		// создаем первую фигуру
		createTetramino();
		// запускаем игровой цикл
		getDisplay().timerExec(0, this);//вызывает метод run
	}
	
	//конец игры
		private void gameOver() {
			getDisplay().timerExec(-1, this);//отменяем выполнение
			gameStarted = false;
		}
		
	// создаем новые фигуры сверху посередине экрана
	private void createTetramino() {
		tetramino = new Tetramino(this, 7, 0);
	}

	// ф-я, преобразующая фигуру в блоки
	private void transformTetraminoToBlocks() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					blocks[tetramino.x + x][tetramino.y + y].setColor(tetramino.color);//блок становится цвета фигуры
					blocks[tetramino.x + x][tetramino.y + y].setFilled(true);//теперь блок не пустой
				}
			}
		}
	}
	
	// сброс фигуры вниз
		private void falling() {
			while (stepDown()) {
			}
		}
	
	// проверка, достигла ли фигура заполненных блоков (границ поля или "старых" фигур)
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

	private boolean stepDown() {//двигайся вниз, пока не достиг дна (или других фигур)
		if (!isDocking(0, 1)) {
			tetramino.y++;
			return true;
		} else {
			if (tetramino.y <= 3) {// game over, если осталось меньше 3-х свободных полос
				gameOver();
				return false;
			}
			transformTetraminoToBlocks();// преобразуем фигуру в "неактивные" блоки, если достигли дна
			createTetramino();// вызываем новую фигуру
			return false;
		}
	}
	private void stepLeft() {//двигайся влево, пока не достиг стены (или других фигур)
		if (!isDocking(-1, 0)) {
			tetramino.x--;
		}
	}
	private void stepRight() {//аналогично вправо
		if (!isDocking(1, 0)) {
			tetramino.x++;
		}
	}
	
	// очищение заполненной линии и смещение остальных на один блок вниз
	private void clearFullLine(int current) {
		for (int i = current; i > 0; i--) {
			for (int j = 0; j < x_size; j++) {
				blocks[j][i].setColor(blocks[j][i - 1].getColor());//смещаем цвет на блок вниз
				blocks[j][i].setFilled(blocks[j][i - 1].getFilled());// аналогично с заполненностью
			}
		}
	}

	// проверка линии (заполнилась ли?)
	private boolean checkingLine() {
		for (int i = 0; i < y_size - 1; i++) {
			boolean gap = false;// пробел
			for (int j = 1; j < x_size - 1; j++) {
				if (!blocks[j][i].getFilled()) {
					gap = true;//если нашли пробел в линии
				}
			}
			if (!gap) { // если пробелов нет, очистить текущую линию и увеличить счет на 1
				clearFullLine(i);
				score++;
				if (score % 10 == 0) {
					level++;// увеличивать уровень каждые 10 очков
					slow -= 30;// уменьшить задержку (увеличить скорость падения фигур)
					if (slow < 1) slow = 1;
				}
			}
		}
		return false;
	}

	public void run() {
		stepDown();// движение вниз
		redraw();//перерисовка фигуры
		if (checkingLine()) {
			redraw();//удаление линии
		}
		getDisplay().timerExec(slow, this);// вызов с замедлением (оно постепенно уменьшается, в зависимости от уровня)
	}
	
	// рисование фигур
	private void drawTetramino(GC gc, Tetramino tetramino) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) 
				{
					gc.setBackground(tetramino.color);// цвет фигуры
					gc.fillRectangle(// заливает прямоугольники (блоки) указанным в пред.строке цветом
						(tetramino.x + x) * 21 + 3,(tetramino.y + y) * 21 + 31,//координаты по иксу и по игреку
						19,19); // ширина и высота
					}
			}
		}
	}

	// события, связанные с нажатием клавиш
	public void keyPressed(KeyEvent e) {
			if (e.character == 'н' || e.character == 'y') {// если н/y, то начинаем новую игру (теперь не зависит от раскладки)
				initializationGame();
			}
			if (e.character == ' ') { // если пробел, то сбрасываем фигуру вниз
				falling();
				redraw();
			} else if (e.keyCode == SWT.ARROW_UP) { // если вверх, то вращаем фигуру
				tetramino.rotate();
				while (isDocking(0, 0)) {//чтобы фигура не застревала в правой границе поля
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

	public void keyReleased(KeyEvent e) {//действия, связанные с отпусканием клавиш
		//
	}

	//освобождаем ресурсы
	public void widgetDisposed(DisposeEvent e) {
		getDisplay().timerExec(-1, this);
		gc.dispose();
		backimage.dispose();
	}

	public void paintControl(PaintEvent e) {
		if (gameStarted) {
			// заполнение фона, если идет игра
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));//темно-серый цвет
			gc.fillRectangle(getClientArea());//заливаем
			
			// рисование блоков
			for (int i = 0; i < x_size; i++) {
				for (int j = 0; j < y_size; j++) {
					gc.setBackground(blocks[i][j].getColor());
					gc.fillRectangle(i * 21 + 3, j * 21 + + 31, 19, 19);
					}
			}
						
			textInfo.setText("Уровень: " + level + " Счёт: " + score + "");//ин-ция о текущем уровне и счете

			// фигуры
			drawTetramino(gc, tetramino);
		} else {// если игра окончена
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.fillRectangle(getClientArea());
			textInfo.setText("Итоговый счет: " + score);
		}
		
		if (e != null)
		e.gc.drawImage(backimage, 0, 0);//отрисовываем
	}

}