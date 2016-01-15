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
		TetrisGame shell = new TetrisGame(display, SWT.TITLE | SWT.CLOSE | SWT.MIN );//кнопочки свернуть, закрыть
		
		//--------------------------------------//
		// создаем меню
		Menu menuBar = new Menu(shell, SWT.BAR);// создаем панельку для меню
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);// создаем пункты меню на верхней панели
		MenuItem cascadeViewMenu = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
	    cascadeFileMenu.setText("&Файл");
	    cascadeViewMenu.setText("&Вид");
	    cascadeHelpMenu.setText("&Справка");
	    
	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);// делаем выпадающие менюшки
	    Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
	    Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
	    cascadeFileMenu.setMenu(fileMenu);// прикрепляем к кнопкам на панели
	    cascadeViewMenu.setMenu(viewMenu);
	    cascadeHelpMenu.setMenu(helpMenu);
	    // файл
	    MenuItem newGameItem = new MenuItem(fileMenu, SWT.PUSH);// делаем пункты выпадающих меню
	    MenuItem statItem = new MenuItem(fileMenu, SWT.PUSH);
	    MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	    newGameItem.setText("&Новая игра");
	    statItem.setText("&Статистика");
	    exitItem.setText("&Выход");
	    
	    // вид
	    MenuItem themecolorItem = new MenuItem(viewMenu, SWT.CASCADE);
	    themecolorItem.setText("&Сетка");
	    Menu selectionMenu = new Menu(shell, SWT.DROP_DOWN);// делаем подменю с выбором "темы"
	    themecolorItem.setMenu(selectionMenu);
	    
	    MenuItem visibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
	    visibleGridItem.setText("&Показать");
	    MenuItem invisibleGridItem = new MenuItem(selectionMenu, SWT.PUSH);
	    invisibleGridItem.setText("&Скрыть");
	    
	   	// справка    
	    MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
	    MenuItem infoItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpItem.setText("&Просмотреть справку");
	    infoItem.setText("&О программе");
	   
	    shell.setMenuBar(menuBar);// прикрепили меню к окну
	    
	    // обработка действий,связанных с меню
	    
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
		setBounds(250, 50, 335, 595);//положение и размеры окна
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