import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class TetrisMenu {
	private static TetrisCanvas tetris;
	public static TetrisGame shell;

	public TetrisMenu(TetrisGame shell) {
		createMenu(shell, tetris);
	}
	
	public void createMenu(TetrisGame shell, TetrisCanvas tetris){
	
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
	}
}