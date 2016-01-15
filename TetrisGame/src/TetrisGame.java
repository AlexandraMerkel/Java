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
		TetrisGame shell = new TetrisGame(display, SWT.TITLE | SWT.CLOSE | SWT.MIN );//�������� ��������, �������
		
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())//������ ������� �� ������� ���������
				display.sleep();
		}
		display.dispose();
}
		public TetrisGame(Display display, int style) { // �����������
		super(display, style); // ����� ������������ �����������
		create�ontentsofWindow();
	}
		// ������ ������� ��� ���� ����
		protected void create�ontentsofWindow() {
		setBounds(250, 50, 335, 595);//��������� � ������� ����
		setText("Tetris Game!");
				
		setLayout(new GridLayout());

		tetris = new TetrisCanvas(this);
		tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));//���������� �� ����������� � ��������� 
<<<<<<< HEAD
=======
		//tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
>>>>>>> origin/master
		menu = new TetrisMenu(this);
		menu.createMenu(this, tetris);
	}
	
	@Override
	protected void checkSubclass() {
		// �������������� �����  (��������� �������� �� ���������)
	}
}