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
	
	Menu menuBar = new Menu(shell, SWT.BAR); // панель для меню
	MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);// пункты для меню в верхней панели
	MenuItem cascadeViewMenu = new MenuItem(menuBar, SWT.CASCADE);
	MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
    cascadeFileMenu.setText("&Файл");
    cascadeViewMenu.setText("&Вид");
    cascadeHelpMenu.setText("&Справка");
    
    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);// выпадающие меню
    Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
    Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
    cascadeFileMenu.setMenu(fileMenu);// прикрепление к пунктам меню на верхней панели
    cascadeViewMenu.setMenu(viewMenu);
    cascadeHelpMenu.setMenu(helpMenu);
    // файл
    MenuItem newGameItem = new MenuItem(fileMenu, SWT.PUSH);// пункты выпадающих меню
    MenuItem statItem = new MenuItem(fileMenu, SWT.PUSH);
    MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
    newGameItem.setText("&Новая игра");
    statItem.setText("&Статистика");
    exitItem.setText("&Выход");
    
    // вид
    MenuItem themecolorItem = new MenuItem(viewMenu, SWT.CASCADE);
    themecolorItem.setText("&Сетка");
    Menu selectionMenu = new Menu(shell, SWT.DROP_DOWN); // подменю с выбором отображения сетки
    themecolorItem.setMenu(selectionMenu);
    
    MenuItem visibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
    visibleGridItem.setText("&Показать");
    MenuItem invisibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
    invisibleGridItem.setText("&Скрыть");
    
   // справка 
    MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
    MenuItem infoItem = new MenuItem(helpMenu, SWT.PUSH);
    helpItem.setText("&Ïðîñìîòðåòü ñïðàâêó");
    infoItem.setText("&Î ïðîãðàììå");
   
    shell.setMenuBar(menuBar);// прикрепили меню к окну
    
    // обработка действий, связанных с меню
    
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
