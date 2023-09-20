import java.util.Scanner;

public class Seabattle {
    private final int RANGE = 10;
    private final int MISSED_HIT = 1000;
    private final int  FINAL_SCORE = 20;
    private final String COORD_LINE = "   |0|1|2|3 |4|5|6| 7|8|9";

    private String player1;
    private String player2;

    private final int[][] battleFieldPlayer1 = new int[RANGE][RANGE];
    private final int[][] battleFieldPlayer2 = new int[RANGE][RANGE];

    private final Scanner s = new Scanner(System.in);

    private void setPlayer1() {
        System.out.print("Введите имя первого игрока: ");
        this.player1 = s.nextLine();
    }

    private void setPlayer2() {
        System.out.print("Введите имя второго игрока: ");
        player2 = s.nextLine();
    }

    public void start() {
        System.out.println("Добро пожаловать в игру МОРСКОЙ БОЙ! \nДавайте познакомимся!");
        setPlayer1();
        setPlayer2();
        System.out.println("""
                Теперь игрокам необходимо заполнить свои поля кораблями.\s
                При указании координат необходимо исходить из правил:\s
                - координаты определены от 0 до 9,\s
                - отсчет ведется от верхнего левого угла.\s
                Число кораблей:\s
                4-палубный - 1,\s
                3-палубные - 2,\s
                2-палубные - 3,\s
                1-палубные - 4.\s
                Для контроля, после размещения каждого корабля отобразится текущее состояние поля.""");
        System.out.println("\nПоле заполняет игрок 1.");
        fillFieldPlayer(battleFieldPlayer1);
        System.out.println("\nПоле заполняет игрок 2.");
        fillFieldPlayer(battleFieldPlayer2);
        System.out.println("Внимание, игроки! Бой начинается! \n" +
                "Сражение будет идти до тех пор, пока кто-нибудь из игроков не уничтожит все корабли противника!");

        while (true) shooting();
    }

    // метод заполнения поля игрока
    private void fillFieldPlayer(int[][] battleField) {
        for (int h = 4; h > 0; h--) {
            for (int i = 0; i < 5 - h; i++) {
                Ship tmpShip = null;
                while (tmpShip == null) {
                    tmpShip = getShip(h);
                }
                if (h != 4) {
                    if (!isPlaceAvailable(tmpShip, battleField)) {
                        System.out.println("Корабль поставить на указанное место нельзя! Попробуйте заново выбрать место.");
                        i--;
                        continue;
                    }
                }
                fillShip(tmpShip, battleField);
                displayOwnerBattleField(battleField);
            }
        }
    }

    // получаем у пользователя координаты корабля
    private Ship getShip(int n) {
        System.out.printf("Введите через пробел координаты первой ячейки, занимаемой %d-палубным кораблем: ", n);
        int y = s.nextInt();
        int x = s.nextInt();
        int kurs = 1;
        if (n != 1) {
            System.out.print("Выберите направление корабля: расположен вправо (1) или книзу (2) от первой ячейки: ");
            kurs = s.nextInt();
        }
        if (y < 0 || y > 9 || x < 0 || x > 9 || kurs > 2 || kurs < 1) {
            System.out.println("Параметры введены некорректно! Проверьте вводимые значения");
            return null;
        }
        return new Ship(x, y, n, kurs);
    }

    // ставим корабль на поле
    private void fillShip(Ship ship, int[][] battleField) {
        if (ship.getKurs() == 2) {
            for (int i = ship.getX(); i < ship.getX() + ship.getN(); i++) {
                battleField[i][ship.getY()] = ship.getN();
            }
        } else {
            for (int i = ship.getY(); i < ship.getY() + ship.getN(); i++) {
                battleField[ship.getX()][i] = ship.getN();
            }
        }
    }

    // проверяем - пустое ли место
    private boolean isPlaceAvailable(Ship ship, int[][] battleField) {
        int x1 = ship.getX() - 1 < 0 ? 0 : ship.getX() - 1;
        int y1 = ship.getY() - 1 < 0 ? 0 : ship.getY() - 1;
        int x2 = 0;
        int y2 = 0;
        if (ship.getKurs() == 2) {
            x2 = ship.getX() + ship.getN() + 1 > battleField.length ? battleField.length - 1 : ship.getX() + ship.getN() + 1;
            y2 = ship.getY() + 1 > battleField.length ? battleField.length - 1 : ship.getY() + 1;
        } else if (ship.getKurs() == 1) {
            x2 = ship.getX() + 1 > battleField.length ? battleField.length - 1 : ship.getX() + 1;
            y2 = ship.getY() + ship.getN() + 1 > battleField.length ? battleField.length - 1 : ship.getY() + ship.getN() + 1;
        }
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (battleField[i][j] != 0) return false;
            }
        }
        return true;
    }

    // отображаем поле оппонента (скрыты корабли)
    private void displayOpponentBattleField(int[][] battleField) {
        System.out.println("--------------------------");
        System.out.println(COORD_LINE);
        for (int i = 0; i < battleField.length; i++) {
            System.out.printf(" %d ", i);
            for (int j = 0; j < battleField.length; j++) {
                System.out.print(battleField[i][j] == MISSED_HIT ? "🔲" :
                        battleField[i][j] < 0 ? "❌" : "⬜");
            }
            System.out.println();
        }
        System.out.println(COORD_LINE);
        System.out.println("--------------------------");
    }

    // отображаем свое поле (видно всё)
    private void displayOwnerBattleField(int[][] battleField) {
        System.out.println("--------------------------");
        System.out.println(COORD_LINE);
        for (int i = 0; i < battleField.length; i++) {
            System.out.printf(" %d ", i);
            for (int j = 0; j < battleField.length; j++) {
                System.out.print(battleField[i][j] == 0 ? "⬜" : battleField[i][j] == MISSED_HIT ? "🔲" :
                        battleField[i][j] > 0 ? "⬛" : "❌");
            }
            System.out.println();
        }
        System.out.println(COORD_LINE);
        System.out.println("--------------------------");
    }

    // промежуточный метод шутинга - реализация повтора удара при успехе предыдущего
    private void shooting() {
        int result = 1;
        while (result == 1) {
            result = shoot(player1, battleFieldPlayer2);
            displayAndCheckAfterShooting(player1, battleFieldPlayer2);
        }
        result = 1;
        while (result == 1) {
            result = shoot(player2, battleFieldPlayer1);
            displayAndCheckAfterShooting(player2, battleFieldPlayer1);
        }
    }

    // отображение поля после удара и проверка счета
    private void displayAndCheckAfterShooting(String player, int[][] battleField) {
        displayOpponentBattleField(battleField);
        if (getScore(battleField) == FINAL_SCORE) {
            System.out.printf("\n---------------------\nИгрок %s ПОБЕДИЛ!\n---------------------\n", player);
            System.exit(0);
        }
    }

    // тело действия - удара по цели
    private int shoot(String player, int[][] battleField) {
        System.out.printf("%s, введи координаты через пробел, куда бить: ", player);
        int y = s.nextInt();
        int x = s.nextInt();
        while (y < 0 || y > 9 || x < 0 || x > 9) {
            System.out.println("Координаты заданы неверно! Повторите ввод!");
            y = s.nextInt();
            x = s.nextInt();
        }
        if (battleField[x][y] > 0) {
            System.out.println("ПОРАЖЕНИЕ!");
            battleField[x][y] = -battleField[x][y];
            return 1;
        } else if (battleField[x][y] < 0) {
            System.out.println("ЧТО МЕРТВО, НЕ МОЖЕТ УМЕРЕТЬ ДВАЖДЫ!");
            return 0;
        } else {
            System.out.println("МИМО!");
            battleField[x][y] = MISSED_HIT;
            return -1;
        }
    }

    // получение текущего счета
    private int getScore(int[][] battleField) {
        int count = 0;
        for (int[] ints : battleField) {
            for (int j = 0; j < battleField.length; j++) {
                if (ints[j] < 0) count++;
            }
        }
        return count;
    }
}