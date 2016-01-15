import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TetrisGame extends Shell {

	private static TetrisCanvas tetris;
	private static TetrisMenu menu;

	public static void main(String args[]) {
	
		Display display = new Display();
		TetrisGame shell = new TetrisGame(display, SWT.TITLE | SWT.CLOSE | SWT.MIN );//êíîïî÷êè ñâåðíóòü, çàêðûòü
		
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())//÷èòàåò ñîáûòèÿ èç î÷åðåäè ñîîáùåíèé
				display.sleep();
		}
		display.dispose();
}
		public TetrisGame(Display display, int style) { // êîíñòðóêòîð
		super(display, style); // âûçîâ êîíñòðóêòîðà ñóïåðêëàññà
		createÑontentsofWindow();
	}
		// çàäàåì âíåøíèé âèä îêíà èãðû
		protected void createÑontentsofWindow() {
		setBounds(250, 50, 335, 595);//ïîëîæåíèå è ðàçìåðû îêíà
		setText("Tetris Game!");
				
		setLayout(new GridLayout());

		tetris = new TetrisCanvas(this);
		tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));//çàïîëíåíèå ïî ãîðèçîíòàëè è âåðòàêàëè 

		menu = new TetrisMenu(this);
		menu.createMenu(this, tetris);
	}
	
	@Override
	protected void checkSubclass() {
		// ïåðåîïðåäåëÿåì ìåòîä  (îòêëþ÷àåì ïðîâåðêó íà ïîäêëàññû)
	}
}
