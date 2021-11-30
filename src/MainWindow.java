
import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setTitle("Snake");
        //крестик в правом углу - прекращение программы
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //размер окна 320 - ширина
        setSize(336, 359);
        //позиция
        setLocation(400,400);

        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();

    }
}
