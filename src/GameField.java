import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

//Jpanel - панель, на которой будет происходить основное действие игры
public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16; //то, сколько пикселей будет занимать одна секция змейки и яблока
    private final int ALL_DOTS = 400; //сколько всего единиц будет на поле

    private Image dot; //картинка под игровую ячейку
    private Image apple; //картинка под яблоко
    private int appleX; //х - позиция яблока
    private int appleY; //y - позиция яблока

    //массив для хранения положения змейки, где находится змейка, в каких ячейках
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int [ALL_DOTS];

    //размер ячеек
    private int dots;

    private Timer timer;

    //булевские поля, отвечающие за текущее направление движения змейки
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true; //если мы уже в игре true или проиграли false

    //конструктор
    public GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        //нажатие клавиш
        addKeyListener(new FieldKeyListener());
        //чтобы фокус был на игровом поле
        setFocusable(true);
    }
    //инициализация начала игры
    public void initGame(){
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();

    }

    //создание яблока
    public void createApple(){
        //рандомная позиция Х. до 20 квадратив
        appleX = new Random().nextInt(20)*DOT_SIZE;
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    //загрузка картинок
    public void loadImages(){
        ImageIcon iia = new ImageIcon("src/apple.png");
        apple = iia.getImage(); //инициализация
        ImageIcon iid = new ImageIcon("src/dot.png");
        dot = iid.getImage();
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            //сдвигаем все точки на предыдущие позиции, отличие будет только у головы змейки
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //for head
        if (left){
            x[0] -=DOT_SIZE;
        }
        if (right){
            x[0] +=DOT_SIZE;
        }
        if (up){
            y[0] -=DOT_SIZE;
        }
        if (down){
            y[0] +=DOT_SIZE;
        }
    }

    public void checkApple(){
        if (x[0] ==appleX && y[0] ==appleY){
            dots ++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i > 0 ; i--) {
            if(i>4 && x[0] ==x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if (x[0]>=SIZE){
            inGame = false;
        }
        if (x[0]<0){
            inGame = false;
        }
        if (y[0]>=SIZE){
            inGame = false;
        }
        if (y[0]<0){
            inGame = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //перерисовка на уровне компонентов (не надо понимать)
        super.paintComponent(g);
        //перерисовка на уровне игры
        if(inGame){
            checkApple();
            checkCollisions();
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i], y[i], this);
            }
        } //если мы не в игре, то надпись гейм овер
        else{
            String str = "Game Over!";
            //шрифт
            //Font f = new Font ("Arial", 14, Font.BOLD);
            g.setColor(Color.white);
            //g.setFont(f);
            g.drawString(str, 125, SIZE/2); //позиция

            //MainWindow mw2 = new MainWindow();

        }
    }

    //реализовываем метод из интерфейса
    @Override
    public void actionPerformed(ActionEvent e) {
        //если я в игре, надо проверить нет ли стен, не встретили ли мы яблоко и в любом случае надо перерисовывать поел
        if (inGame){
            move();
        }
        //перерисовка поля
        repaint();
    }

    //класс для обработки клавиш
    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            //если была нажата левая кнопка клавитуры и мы не двигаемся в право, мы не можем координально
            //повернуться в лево, если мы смотрим в право, только вверх или вниз
            if (key == KeyEvent.VK_LEFT&& ! right){
                left = true; //выбираем направление влево
                up = false; //так как мы смотрим вверх или вниз, то делаем эти значения ложью
                down = false; // потому что мы меняем направление
            }
            if (key == KeyEvent.VK_RIGHT&& ! left){
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP&& ! down){
                right = false;
                up = true;
                left = false;
            }
            if (key == KeyEvent.VK_DOWN&& ! up){
                right = false;
                down = true;
                left = false;
            }
        }
    }
}
