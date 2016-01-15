Приложение «Tetris Game» (рабочее название) является аналогом общеизвестной компьютерной игры «Тетрис» с некоторыми модификациями. Данное приложение подразумевает участие 2-х пользователей.
Описание функционала
Приложение можно условно разделить на две области: игровое поле и меню выбора фигуры. Игровое поле имеет размер 15х24 клеток, включая границы. Всего имеется 7 разных классических фигур тетрамино, каждая окрашена в определенный цвет.
1-й игрок (активная область – меню выбора фигуры):
- может видеть игровое поле, с которым параллельно в это же время взаимодействует 2-й игрок;
- имеет возможность выбирать среди предоставленного ему набора фигур (любые различные три фигуры из имеющихся семи) одну, которая впоследствии будет передана 2-му игроку (если выбор не был осуществлен до момента установки 2-м пользователем предыдущей фигуры, приложение само «выбирает» любую фигуру);
2-й игрок (активная область – игровое поле):
- может производить манипуляции с поданной ему фигурой на игровом поле, а именно: вращать фигуру, передвигать  фигуру по горизонтали вправо/влево,  сбрасывать фигуру (т.е. ускорять движение фигуры вниз).
Фигура движется вниз, пока не наткнётся на любую другую фигуру, либо пока не достигнет нижней линии игрового поля. Если при этом заполнился горизонтальный ряд из 13-и клеток, он пропадает и все линии, что были выше него, опускаются на одну клетку вниз. 
Игра заканчивается, когда новая фигурка не может поместиться в игровое поле. Игроки получают очки за каждый заполненный ряд, поэтому цель игры — заполнять ряды, не заполняя само игровое поле (по вертикали) как можно дольше, чтобы таким образом получить как можно больше очков.
Чем больше набрано очков в процессе текущей игры, тем больше скорость падения фигур
Замечание: в данном семестре реализуется игровое поле и действия 2-го игрока.
Разработка
При создании приложения в качестве среды разработки используется ИСР Eclipse; так же используется библиотека SWT.
Структура приложения подразумевает несколько классов, каждый из которых специализируется на выполнении определенных задач. На данный момент это классы:
	Block – класс одного блока (клетки) поля. 
Содержит поля Color color – цвет блока и boolean filled – обозначающее, заполнен блок или пуст.
Содержит методы getFilled, setFilled, getColor, setColor , позволяющие получить и установить вышеперечисленные значения.
	Tetramino – класс цельной фигуры тетрамино. 
Содержит поля int x, y – координаты фигуры, Color color – цвет фигуры и Composite  figure, служащий для применения к фигуре определенных методов библиотеки SWT; 
Содержит методы createTetramino - создание новой фигуры и rotate - вращение фигуры.
	TetrisGame – основной класс приложения, наследник Shell. 
Содержит основной метод main, создающий корневой объект display и окно приложения, реализующий цикл обработки событий ; 
также метод createСontentsofWindow, задающий расположение, размеры, название окна приложения.
	TetrisCanvas – (находится в разработке) класс, отвечающий за отрисовку, наследник Canvas, реализующий интерфейсы PaintListener, Runnable, KeyListener, DisposeListener. 
Содержит поля: boolean gameStarted, обозначающее, запущен ли игровой цикл; int x_size, y_size – размеры поля в блоках; Text textInfo – строка, показывающая текущий счет и уровень;  Image backimage, GC gc – отвечающие за отрисовку компонентов;
Содержит методы:
initializationGame – вызываемый при инициализации игры, создающий игровое поле; 

gameOver – вызываемый при завершении игры, останавливающий метод run; 

createTetramino – вызывающий конструктор из класса Tetramino (создание фигуры); 

transformTetraminoToBlocks – преобразующий фигуру в неактивные блоки (т.е. делающий фигуру фоном); 

isDocking – проверяющий, достигла ли фигура границ игрового поля/линий «старых» фигур;

stepDown/ stepLeft/stepRight – отвечающие за движение фигуры вниз/влево/вправо; 

falling – отвечающий за сброс фигуры; 

checkingLine – проверяющий, заполнена ли линия;

clearFullLine – удаляющий полную линию;

run – запускающий игровой цикл;

drawTetramino – описывающий отрисовку фигуры;

keyPressed – обрабатывающий события, связанные с нажатием на клавиши;

widgetDisposed – освобождающий ресурсы;

paintControl – отрисовывающий компоненты окна (фон, фигуры и блоки игрового поля).


Планируется добавление классов для реализации выбора фигуры 1-м пользователем, для создания меню приложения.