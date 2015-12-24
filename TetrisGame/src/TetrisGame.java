import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TetrisGame extends Shell {

	private TetrisCanvas tetris;

	public static void main(String args[]) {
	
		Display display = new Display();
		TetrisGame shell = new TetrisGame(display, SWT.TITLE | SWT.CLOSE | SWT.MIN );//кнопочки свернуть, закрыть
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())//читает события из очереди сообщение
				display.sleep();
		}
		display.dispose();
}
		public TetrisGame(Display display, int style) { // конструктор
		super(display, style); // вызов конструктора суперкласса
		createСontentsofWindow();
	}
		// задаем внешний вид окна игры
		protected void createСontentsofWindow() {
		setBounds(250, 50, 335, 575);//положение и размеры окна
		setText("Tetris Game!");
				
		setLayout(new GridLayout());

		tetris = new TetrisCanvas(this);
		tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));//заполнение по горизонтали и вертакали 
		//tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
	}
	
	@Override
	protected void checkSubclass() {
		// переопределяем метод  (отключаем проверку на подклассы)
	}
}