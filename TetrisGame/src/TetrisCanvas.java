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
	private boolean gameStarted; //идёт ли игра
	private int slow = 500; // замедление 
	private int level; // уровень (влияет на замедление, т.е. на скорость падения фигур)
	private int x_size; // размеры поля в блоках
	private int y_size; 
	private int score;// очки
	public int grid_color; // цвет сетки 
	private Image backimage;// объект класса изображений
	private GC gc; // для рисования на изображении
	private Block[][] blocks;
	private Text textInfo; //информация о текущем уровне и счёте

	public TetrisCanvas(Composite parent) {
		super(parent, SWT.CENTER); //вызов конструктора суперкласса
		gameStarted = false; 
		addPaintListener(this); // лисенер для рисования
		setLayout(new GridLayout());
		
		textInfo = new Text(this, SWT.NONE);
		textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));//çàïîëíåíèå ïî ãîðèçîíòàëè, ñâåðõó
				
		addKeyListener(this);// для клавиш
		addDisposeListener(this);// для очищения экрана
		x_size = 15;
		y_size = 24;
		grid_color = SWT.COLOR_GRAY;// по умолчанию сетка есть
		
		backimage = new Image(getDisplay(), x_size * 22, y_size * 23);// создаём объект класса изображений
		gc = new GC(backimage); // будем рисовать на нём
	}

	// функция начала игры
	public void initializeGame() {
		gameStarted = true;
		score = 0;
		blocks = new Block[x_size][y_size];// создание массива для поля

		// создание поля из пустых блоков
		for (int i = 0; i < x_size; i++) {
			for (int j = 0; j < y_size - 1; j++) {
				blocks[i][j] = new Block( getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
			}
		}
		// создание нижней границы поля
		for (int i = 0; i < x_size; i++) {
			blocks[i][y_size - 1] = new Block(getDisplay().getSystemColor(grid_color), true);
		}
		// создание боковых границ поля
		for (int i = 0; i < y_size; i++) {
			blocks[0][i] = new Block(getDisplay().getSystemColor(grid_color), true);
			blocks[x_size - 1][i] = new Block(getDisplay().getSystemColor(grid_color), true);
		}
		// создание первой фигуры
		createTetramino();
		// запуск игрового цикла
		getDisplay().timerExec(0, this); //вызов метода run
	}
	
	// конец игры
		private void gameOver() {
			getDisplay().timerExec(-1, this); //отмена выполнения
			gameStarted = false;
		}
		
	// создание новых фигур сверху посередине экрана
	private void createTetramino() {
		tetramino = new Tetramino(this, 7, 0);
	}

	// функция преобразования фигуры в блоки
	private void transformTetraminoToBlocks() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					blocks[tetramino.x + x][tetramino.y + y].setColor(tetramino.color);//блок становится цвета фигуры
					blocks[tetramino.x + x][tetramino.y + y].setFilled(true);//и заполненный
				}
			}
		}
	}
	
	// функция сброса фигуры
		private void fall() {
			while (stepDown()) {
			}
		}
	
	// проверка, достигла ли фигура заполненных блоков (границ поля или уже упавших фигур)
		private boolean isTouching(int x, int y) {
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

	private boolean stepDown() { //движение вниз, пока не достигнуто дно (или другие фигуры)
		if (!isTouching(0, 1)) {
			tetramino.y++;
			return true;
		} else {
			if (tetramino.y <= 3) {// game over, если осталось меньше 3х свободных полос
				gameOver();
				return false;
			}
			transformTetraminoToBlocks(); // преобразуем фигуру в неактивные блоки, если достигли дна
			createTetramino(); // вызываем новую фигуру
			return false;
		}
	}
	private void stepLeft() {// движение влево до достижения стены или других фигур
		if (!isTouching(-1, 0)) {
			tetramino.x--;
		}
	}
	private void stepRight() {// вправо
		if (!isTouching(1, 0)) {
			tetramino.x++;
		}
	}
	
	// очищение заполненной линии и смещение остальных блоков на 1 вниз
	private void clearFullLine(int current) {
		for (int i = current; i > 0; i--) {
			for (int j = 0; j < x_size; j++) {
				blocks[j][i].setColor(blocks[j][i - 1].getColor());// смещаем цвет на блок вниз
				blocks[j][i].setFilled(blocks[j][i - 1].getFilled());// заполненность
			}
		}
	}

	// проверка на заполненность линии
	private boolean checkLine() {
		for (int i = 0; i < y_size - 1; i++) {
			boolean gap = false;// пробел в линии 
			for (int j = 1; j < x_size - 1; j++) {
				if (!blocks[j][i].getFilled()) {
					gap = true; // если нашли пробел
				}
			}
			if (!gap) { // если пробелов нет, очистить текущую линию и увеличить счёт на 1
				clearFullLine(i);
				score++;
				if (score % 10 == 0) {
					level++;// увеличивать уровень каждые 10 очков
					slow -= 30;// уменьшить задержку
					if (slow < 1) slow = 1;
				}
			}
		}
		return false;
	}

	public void run() {
		stepDown(); // движение вниз
		redraw(); //перерисовка
		if (checkLine()) {
			redraw();//удаление линии
		}
		getDisplay().timerExec(slow, this);// вызов с замедлением
	}
	
	// рисование фигур
	private void drawTetramino(GC gc, Tetramino tetramino) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) 
				{
					gc.setBackground(tetramino.color); // цвет фигуры
					gc.fillRectangle(// заливает прямоугольники (блоки) указанным в предыдущей строке цветом
						(tetramino.x + x) * 21 + 3,(tetramino.y + y) * 21 + 31,//координаты по x и y
						19,19); // ширина и высота
					}
			}
		}
	}

	// события, связанные с нажатием клавиш
	public void keyPressed(KeyEvent e) {
			if (e.character == 'н' || e.character == 'y') {// если 'н' или 'y', то начинаем новую игру
				initializeGame();
			}
			if (e.character == ' ') { // если пробел, то сброс фигуры
				fall();
				redraw();
			} else if (e.keyCode == SWT.ARROW_UP) { // если вверх, то вращаем фигуру
				tetramino.rotate();
				while (isTouching(0, 0)) {//чтобы фигура не застревала в правой границе поля
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

	public void keyReleased(KeyEvent e) {//события, связанные с отпусканием клавиш
		//
	}

	// освобождение ресурсов
	public void widgetDisposed(DisposeEvent e) {
		getDisplay().timerExec(-1, this);
		gc.dispose();
		backimage.dispose();
	}

	public void paintControl(PaintEvent e) {
		if (gameStarted) {

			gc.setBackground(getDisplay().getSystemColor(grid_color));
			gc.fillRectangle(getClientArea()); // заливка
			
			// рисование блоков
			for (int i = 0; i < x_size; i++) {
				for (int j = 0; j < y_size; j++) {
					gc.setBackground(blocks[i][j].getColor());
					gc.fillRectangle(i * 21 + 3, j * 21 + + 31, 19, 19);
					}
			}
						
			textInfo.setText("Óðîâåíü: " + level + " Ñ÷¸ò: " + score + ""); // информация о текущем уровне и счёте

			// рисование фигур 
			drawTetramino(gc, tetramino);
		} else {// если игра окончена
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
			gc.fillRectangle(getClientArea());
			textInfo.setText("Èòîãîâûé ñ÷åò: " + score);
		}
		
		if (e != null)
		e.gc.drawImage(backimage, 0, 0); //отрисовываем 
}
