import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class TetrisGame extends Shell {

	private static TetrisCanvas tetris;

	public static void main(String args[]) {
	
		Display display = new Display();
		TetrisGame shell = new TetrisGame(display, SWT.TITLE | SWT.CLOSE | SWT.MIN );//�������� ��������, �������
		
		//--------------------------------------//
		// ������� ����
		Menu menuBar = new Menu(shell, SWT.BAR);// ������� �������� ��� ����
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);// ������� ������ ���� �� ������� ������
		MenuItem cascadeViewMenu = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
	    cascadeFileMenu.setText("&����");
	    cascadeViewMenu.setText("&���");
	    cascadeHelpMenu.setText("&�������");
	    
	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);// ������ ���������� �������
	    Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
	    Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
	    cascadeFileMenu.setMenu(fileMenu);// ����������� � ������� �� ������
	    cascadeViewMenu.setMenu(viewMenu);
	    cascadeHelpMenu.setMenu(helpMenu);
	    // ����
	    MenuItem newGameItem = new MenuItem(fileMenu, SWT.PUSH);// ������ ������ ���������� ����
	    MenuItem statItem = new MenuItem(fileMenu, SWT.PUSH);
	    MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	    newGameItem.setText("&����� ����");
	    statItem.setText("&����������");
	    exitItem.setText("&�����");
	    
	    // ���
	    MenuItem themecolorItem = new MenuItem(viewMenu, SWT.CASCADE);
	    themecolorItem.setText("&�����");
	    Menu selectionMenu = new Menu(shell, SWT.DROP_DOWN);// ������ ������� � ������� "����"
	    themecolorItem.setMenu(selectionMenu);
	    
	    MenuItem visibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
	    visibleGridItem.setText("&��������");
	    MenuItem invisibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
	    invisibleGridItem.setText("&������");
	    
	   	// �������    
	    MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
	    MenuItem infoItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpItem.setText("&����������� �������");
	    infoItem.setText("&� ���������");
	   
	    shell.setMenuBar(menuBar);// ���������� ���� � ����
	    
	    // ��������� ��������,��������� � ����
	    
	    	newGameItem.addListener(SWT.Selection, event-> {
	    		tetris.initializeGame();	
	    	});
	    
	   	    exitItem.addListener(SWT.Selection, event-> {
	   	    	shell.getDisplay().dispose();
	   	    	System.exit(0);
	    });
	   	    
	   	    visibleGridItem.addListener(SWT.Selection, event-> {
	   	    	tetris.grid_color = SWT.COLOR_GRAY;
		    });
	   	 
	   	    invisibleGridItem.addListener(SWT.Selection, event-> {
	   	    	tetris.grid_color = SWT.COLOR_WHITE;
		    });
	   	 
	    //----------------------------------------//
		
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
		//tetris.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
	}
	
	@Override
	protected void checkSubclass() {
		// �������������� �����  (��������� �������� �� ���������)
	}
}