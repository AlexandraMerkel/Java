package ru.game.client;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;

public class TetrisMenu 
{
	private static TetrisCanvas tetris;
	public static TetrisGame shell;
	private static Display display;
	private Text textInfo;
	
	public TetrisMenu(TetrisGame shell) 
	{
		createMenu(shell, tetris);
	}
	
	public void createMenu(TetrisGame shell, TetrisCanvas tetris)
	{
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
	    MenuItem joinGameItem = new MenuItem(fileMenu, SWT.PUSH);
	    MenuItem statItem = new MenuItem(fileMenu, SWT.PUSH);
	    MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	    newGameItem.setText("&����� ����");
	    joinGameItem.setText("&�������������� � ����");
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
	    
	    newGameItem.addListener(SWT.Selection, event-> 
	    	{
	    		try {
					tetris.drawWaiting();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	    	}
	    );
	    
	    joinGameItem.addListener(SWT.Selection, event-> 
    		{
    			try {
					tetris.drawSessionList();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
    		}
	    );
	    
	   	exitItem.addListener(SWT.Selection, event-> 
	   	   	{
	   	    	shell.getDisplay().dispose();
	   	    	System.exit(0);
	   	    }
	   	);
	   	    
	   	visibleGridItem.addListener(SWT.Selection, event-> 
	   		{
	   			tetris.isGridEnabled(true);
		    }
	   	);
	   	 
	   	invisibleGridItem.addListener(SWT.Selection, event-> 
	   		{
	   	    	tetris.isGridEnabled(false);
		    }
	   	);
	   	
	   	helpItem.addListener(SWT.Selection, event->
   		{
   			Shell shell_help = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
	        FillLayout layout = new FillLayout();
	        shell_help.setLayout(layout);
	        shell_help.setBounds(300, 50, 500, 600); // ��������� � ������� ����
	        shell_help.setText("�������");
	        textInfo = new Text(shell_help, SWT.NONE);
   			textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
   			textInfo.setText("��� ����� ������� ����");
	        shell_help.open();
    	
    });
   	
   	infoItem.addListener(SWT.Selection, event->
		{
			Shell shell_help = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
        FillLayout layout = new FillLayout();
        shell_help.setLayout(layout);
        shell_help.setBounds(300, 50, 400, 400); // ��������� � ������� ����
        shell_help.setText("Tetris x 2: ��������");
        textInfo = new Text(shell_help, SWT.NONE);
			textInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
			textInfo.setText("��� ����� ���� � ���������");
        shell_help.open();	   
		});
	}
}