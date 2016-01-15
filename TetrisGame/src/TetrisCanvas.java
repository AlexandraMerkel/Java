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
//import org.eclipse.swt.graphics.Color;

public class TetrisCanvas
	extends Canvas
	implements PaintListener, Runnable, KeyListener, DisposeListener {

	private Tetramino tetramino;
	private boolean gameStarted;//èäåò ëè èãðà
	private int slow = 500;// çàìåäëåíèå
	private int level;// óðîâåíü (âëÿåò íà çàìåäëåíèå, ò.å. íà ñêîðîñòü ïàäåíèÿ ôèãóð)
	private int x_size; // ðàçìåðû ïîëÿ â áëîêàõ
	private int y_size;
	private int score;//î÷êè
	public int grid_color;// öâåò ôîíà (äëÿ ñåòêè)
	private Image backimage;//îáúåêò êëàññà èçîáðàæåíèé
	private GC gc; // äëÿ ðèñîâàíèÿ íà èçîáðàæåíèè
	private Block[][] blocks;
	private Text textInfo;//èíôà î òåêóùåì óðîâíå è ñ÷åòå

	public TetrisCanvas(Composite parent) {
		super(parent, SWT.CENTER);//âûçûâàåì êîíñòðóêòîð ñóïåðêëàññà
		gameStarted = false;
		addPaintListener(this); // ëèñåíåð äëÿ ðèñîâàíèÿ
		setLayout(new GridLayout());
		
		textInfo = new Text(this, SWT.NONE);
		textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));//çàïîëíåíèå ïî ãîðèçîíòàëè, ñâåðõó
				
		addKeyListener(this);// ëèñåíåð äëÿ êëàâèø
		addDisposeListener(this);// äëÿ î÷èùåíèÿ
		x_size = 15;// ðàçìåðû ïîëÿ (â áëîêàõ)
		y_size = 24;
		grid_color = SWT.COLOR_GRAY;// ïî óìîë÷àíèþ ñåòêà åñòü
		
		backimage = new Image(getDisplay(), x_size * 22, y_size * 23);// ñîçäàåì îáúåêò êëàññà èçîáðàæåíèé
		gc = new GC(backimage); // áóäåì ðèñîâàòü íà íåì
	}

	// íà÷èíàåì èãðó
	public void initializeGame() {
		gameStarted = true;
		score = 0;
		blocks = new Block[x_size][y_size];// ñîçäàåì ìàññèâ äëÿ ïîëÿ

		// ñîçäàåì ñàìî ïîëå èç ïóñòûõ áëîêîâ 
		for (int i = 0; i < x_size; i++) {
			for (int j = 0; j < y_size - 1; j++) {
				blocks[i][j] = new Block( getDisplay().getSystemColor(SWT.COLOR_WHITE), false);
			}
		}
		// ñîçäàåì íèæíþþ ãðàíèöó ïîëÿ (áëîêè ñ÷èòàþòñÿ çàïîëíåííûìè)
		for (int i = 0; i < x_size; i++) {
			blocks[i][y_size - 1] = new Block(getDisplay().getSystemColor(grid_color), true);
		}
		// ñîçäàåì áîêîâûå ãðàíèöû ïîëÿ (áëîêè òàêæå ñ÷èòàþòñÿ çàïîëíåííûìè)
		for (int i = 0; i < y_size; i++) {
			blocks[0][i] = new Block( getDisplay().getSystemColor(grid_color), true);
			blocks[x_size - 1][i] = new Block( getDisplay().getSystemColor(grid_color), true);
		}
		// ñîçäàåì ïåðâóþ ôèãóðó
		createTetramino();
		// çàïóñêàåì èãðîâîé öèêë
		getDisplay().timerExec(0, this);//âûçûâàåò ìåòîä run
	}
	
	//êîíåö èãðû
		private void gameOver() {
			getDisplay().timerExec(-1, this);//îòìåíÿåì âûïîëíåíèå
			gameStarted = false;
		}
		
	// ñîçäàåì íîâûå ôèãóðû ñâåðõó ïîñåðåäèíå ýêðàíà
	private void createTetramino() {
		tetramino = new Tetramino(this, 7, 0);
	}

	// ô-ÿ, ïðåîáðàçóþùàÿ ôèãóðó â áëîêè
	private void transformTetraminoToBlocks() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) {
					blocks[tetramino.x + x][tetramino.y + y].setColor(tetramino.color);//áëîê ñòàíîâèòñÿ öâåòà ôèãóðû
					blocks[tetramino.x + x][tetramino.y + y].setFilled(true);//òåïåðü áëîê íå ïóñòîé
				}
			}
		}
	}
	
	// ñáðîñ ôèãóðû âíèç
		private void fall() {
			while (stepDown()) {
			}
		}
	
	// ïðîâåðêà, äîñòèãëà ëè ôèãóðà çàïîëíåííûõ áëîêîâ (ãðàíèö ïîëÿ èëè "ñòàðûõ" ôèãóð)
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

	private boolean stepDown() {//äâèãàéñÿ âíèç, ïîêà íå äîñòèã äíà (èëè äðóãèõ ôèãóð)
		if (!isTouching(0, 1)) {
			tetramino.y++;
			return true;
		} else {
			if (tetramino.y <= 3) {// game over, åñëè îñòàëîñü ìåíüøå 3-õ ñâîáîäíûõ ïîëîñ
				gameOver();
				return false;
			}
			transformTetraminoToBlocks();// ïðåîáðàçóåì ôèãóðó â "íåàêòèâíûå" áëîêè, åñëè äîñòèãëè äíà
			createTetramino();// âûçûâàåì íîâóþ ôèãóðó
			return false;
		}
	}
	private void stepLeft() {//äâèãàéñÿ âëåâî, ïîêà íå äîñòèã ñòåíû (èëè äðóãèõ ôèãóð)
		if (!isTouching(-1, 0)) {
			tetramino.x--;
		}
	}
	private void stepRight() {//àíàëîãè÷íî âïðàâî
		if (!isTouching(1, 0)) {
			tetramino.x++;
		}
	}
	
	// î÷èùåíèå çàïîëíåííîé ëèíèè è ñìåùåíèå îñòàëüíûõ íà îäèí áëîê âíèç
	private void clearFullLine(int current) {
		for (int i = current; i > 0; i--) {
			for (int j = 0; j < x_size; j++) {
				blocks[j][i].setColor(blocks[j][i - 1].getColor());//ñìåùàåì öâåò íà áëîê âíèç
				blocks[j][i].setFilled(blocks[j][i - 1].getFilled());// àíàëîãè÷íî ñ çàïîëíåííîñòüþ
			}
		}
	}

	// ïðîâåðêà ëèíèè (çàïîëíèëàñü ëè?)
	private boolean checkLine() {
		for (int i = 0; i < y_size - 1; i++) {
			boolean gap = false;// ïðîáåë
			for (int j = 1; j < x_size - 1; j++) {
				if (!blocks[j][i].getFilled()) {
					gap = true;//åñëè íàøëè ïðîáåë â ëèíèè
				}
			}
			if (!gap) { // åñëè ïðîáåëîâ íåò, î÷èñòèòü òåêóùóþ ëèíèþ è óâåëè÷èòü ñ÷åò íà 1
				clearFullLine(i);
				score++;
				if (score % 10 == 0) {
					level++;// óâåëè÷èâàòü óðîâåíü êàæäûå 10 î÷êîâ
					slow -= 30;// óìåíüøèòü çàäåðæêó (óâåëè÷èòü ñêîðîñòü ïàäåíèÿ ôèãóð)
					if (slow < 1) slow = 1;
				}
			}
		}
		return false;
	}

	public void run() {
		stepDown();// äâèæåíèå âíèç
		redraw();//ïåðåðèñîâêà ôèãóðû
		if (checkLine()) {
			redraw();//óäàëåíèå ëèíèè
		}
		getDisplay().timerExec(slow, this);// âûçîâ ñ çàìåäëåíèåì (îíî ïîñòåïåííî óìåíüøàåòñÿ, â çàâèñèìîñòè îò óðîâíÿ)
	}
	
	// ðèñîâàíèå ôèãóð
	private void drawTetramino(GC gc, Tetramino tetramino) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetramino.field[x][y] == true) 
				{
					gc.setBackground(tetramino.color);// öâåò ôèãóðû
					gc.fillRectangle(// çàëèâàåò ïðÿìîóãîëüíèêè (áëîêè) óêàçàííûì â ïðåä.ñòðîêå öâåòîì
						(tetramino.x + x) * 21 + 3,(tetramino.y + y) * 21 + 31,//êîîðäèíàòû ïî èêñó è ïî èãðåêó
						19,19); // øèðèíà è âûñîòà
					}
			}
		}
	}

	// ñîáûòèÿ, ñâÿçàííûå ñ íàæàòèåì êëàâèø
	public void keyPressed(KeyEvent e) {
			if (e.character == 'í' || e.character == 'y') {// åñëè í/y, òî íà÷èíàåì íîâóþ èãðó (òåïåðü íå çàâèñèò îò ðàñêëàäêè)
				initializeGame();
			}
			if (e.character == ' ') { // åñëè ïðîáåë, òî ñáðàñûâàåì ôèãóðó âíèç
				fall();
				redraw();
			} else if (e.keyCode == SWT.ARROW_UP) { // åñëè ââåðõ, òî âðàùàåì ôèãóðó
				tetramino.rotate();
				while (isTouching(0, 0)) {//÷òîáû ôèãóðà íå çàñòðåâàëà â ïðàâîé ãðàíèöå ïîëÿ
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

	public void keyReleased(KeyEvent e) {//äåéñòâèÿ, ñâÿçàííûå ñ îòïóñêàíèåì êëàâèø
		//
	}

	//îñâîáîæäàåì ðåñóðñû
	public void widgetDisposed(DisposeEvent e) {
		getDisplay().timerExec(-1, this);
		gc.dispose();
		backimage.dispose();
	}

	public void paintControl(PaintEvent e) {
		if (gameStarted) {
			//int grid_color = SWT.COLOR_GRAY;
			// çàïîëíåíèå ôîíà, åñëè èäåò èãðà

			gc.setBackground(getDisplay().getSystemColor(grid_color));
			gc.fillRectangle(getClientArea());//çàëèâàåì
			
			// ðèñîâàíèå áëîêîâ
			for (int i = 0; i < x_size; i++) {
				for (int j = 0; j < y_size; j++) {
					gc.setBackground(blocks[i][j].getColor());
					gc.fillRectangle(i * 21 + 3, j * 21 + + 31, 19, 19);
					}
			}
						
			textInfo.setText("Óðîâåíü: " + level + " Ñ÷¸ò: " + score + "");//èí-öèÿ î òåêóùåì óðîâíå è ñ÷åòå

			// ôèãóðû
			drawTetramino(gc, tetramino);
		} else {// åñëè èãðà îêîí÷åíà
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
			gc.fillRectangle(getClientArea());
			textInfo.setText("Èòîãîâûé ñ÷åò: " + score);
		}
		
		if (e != null)
		e.gc.drawImage(backimage, 0, 0);//îòðèñîâûâàåì
	}

}
