import unit.classes.Player;

import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // Параметр значения команды пользователя
        int command;

        // Параметр хранения порядкового номера целевого югита (из не уничтоженных)
        int targetUnitNum = 0;
        // Номер действия
        int actionNum;

        // Текущий игрок
        Player currentPlayer;

        /*
        Создание игроков и назначение им друг друга в качестве соперников
         */
        Player player = new Player("Player");
        Player enemy = new Player("Enemy Player", true); // назначаем игрока enemy как CPU
        player.setEnemyPlayer(enemy);
        enemy.setEnemyPlayer(player);

        // Назначение текущего игрока
        currentPlayer = player;

        // Основной цикл игры
        while (true)
        {
            // Вывод доступных юнитов игроков
            System.out.println("====================================================");
            System.out.println(player);
            System.out.println(enemy);
            System.out.println("====================================================");
            System.out.println("Enter any key to continue");
            scanner.nextInt();
            
            System.out.println(String.format("%s's turn", currentPlayer.getName()));

            command = getPlayerCommand(currentPlayer); // Получить команду от текущего игрока
            if(command == 0) break; // если 0 то выход из игры
            else if (command == 2) // если команда на перезапуск
            {
                // Запуск нового цикла игры

                player = new Player("unit.classes.Player");
                enemy = new Player("Enemy unit.classes.Player", true);
                player.setEnemyPlayer(enemy);
                enemy.setEnemyPlayer(player);

                currentPlayer = player;
                continue;
            }

            System.out.println();

            // Взять под контроль юнит текущим игроком
            if(takeControlUnit(currentPlayer) == 0) break; // если возвращён 0, то выход

            // Получить действие для выбранного юнита от текущего игрока
            actionNum = getPlayerAction(currentPlayer);
            if(actionNum == 0) break; // если 0, то выход из игры

            if(actionNum == 1)  // Если атака или лечение то необходимо получить целевой юнит
            {
                targetUnitNum = getPlayerTarget(currentPlayer); // получить целевой юнит
                if(targetUnitNum == 0) break; // если 0, то выход из игры
            }

            // Передаём контроллеру действие и цель и выполняем действие

            currentPlayer.getController().setAction(actionNum);
            currentPlayer.getController().setTargetUnitNum(targetUnitNum);
            currentPlayer.getController().makeAction();

            // Проверка победителя игры, если у игрока не осталось действующих юнитов то он проиграл
            if(currentPlayer.getAvailibleUnitsCount() == 0)
            {
                System.out.println();
                System.out.println(String.format("%s win the game!", currentPlayer.getName()));
                System.out.println("GAME OVER");

                //Запуск нового цикла игры

                player = new Player("unit.classes.Player");
                enemy = new Player("Enemy unit.classes.Player", true);
                player.setEnemyPlayer(enemy);
                enemy.setEnemyPlayer(player);

                currentPlayer = player;
            }
            else
                currentPlayer = currentPlayer == player ? enemy : player; // Меняем текущего игрока
        }
    }

    /** Метод получения команды от пользователя
     *
     * @param player - игрок
     * @return - команда пользователя
     */
    public static int getPlayerCommand(Player player)
    {
        int command;
        // Если игрок - человек
        if(!player.isAI())
        {
            do
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("""
                                   Enter command:
                                   0 - 'exit game'
                                   1 - 'next turn'
                                   2 - 'replay'
                                   """);
                // Проверка корректности ввода команды

                while (!scanner.hasNextInt())
                {
                    System.out.println("It's not an integer!");
                    System.out.println("Enter command:");
                    scanner.next();
                }
                command = scanner.nextInt();
                if(command <= 2 && command >= 0) break;
                System.out.println("Incorrect!");
            }while (true);

            if(command == 0) return command;
        }
        else command = 1; // у компьютера команда продолжать ход

        return command;
    }

    /** Метод взятия под контроль доступного (не уничтоженного) юнита
     *
     * @param player - текущий игрок
     * @return - порядковый номер выбранного юнита
     */
    public static int takeControlUnit(Player player)
    {
        int ctrlUnitNum;
        if(!player.isAI())
        {
            do
            {
                // Проверка корректности выбора юнита

                Scanner scanner = new Scanner(System.in);
                System.out.println("Select unit (0 for exit game):");
                System.out.println(player); // дотсупные юниты игрока
                while (!scanner.hasNextInt())
                {
                    System.out.println("It's not an integer!");
                    scanner.next();
                }
                ctrlUnitNum = scanner.nextInt();
                if(ctrlUnitNum == 0) break;

                // Если указанный номер юнита не доступен, то ввод заново,
                // если доступен, то пользователь получит контроль
                if(player.getController().takeAvailableUnit(ctrlUnitNum, true) == null)
                {
                    System.out.println("Incorrect!");
                    continue;
                }
                break;
            }while (true);
        }
        else
        {
            // Компьютер получает значение целевого юнита случайным образом

            Random random = new Random();
            ctrlUnitNum = random.nextInt(player.getAvailibleUnitsCount()) + 1;
            if (player.getController().takeAvailableUnit( ctrlUnitNum,true) == null)
            {
                ctrlUnitNum = 0;
                System.out.println("Something went wrong!");
            }
        }
        return ctrlUnitNum;
    }

    /** Метод выбора действия для текущего юнита
     *
     * @param player - игрок
     * @return - номер действия
     */
    public static int getPlayerAction(Player player)
    {
        int actionNum;
        if(!player.isAI()) // если игрок человек
        {
            Scanner scanner = new Scanner(System.in);
            do
            {
                // Проверка корректности ввода

                System.out.println("Select action (0 for exit game):");
                // Вывод возможных действий для текущего юнита
                System.out.println(player.getController().getAvailibleActions());
                while (!scanner.hasNextInt())
                {
                    System.out.println("It's not an integer!");
                    System.out.println("Enter command:");
                    scanner.next();
                }
                actionNum = scanner.nextInt();
                if(actionNum == 0) break;
                // Проверить возможность выбранного действия для текущего юнита
                if(player.getController().testAction(actionNum)) break;

                System.out.println("Incorrect!");
            }while (true);
        }
        else actionNum = 1; // Компьютер только атакует
        return actionNum;
    }

    /** Метод выбора целевого юнита
     *
     * @param player - текущий игрок
     * @return - порядковый номер целевого юнита
     */
    public static int getPlayerTarget(Player player)
    {
        int targetUnitNum;
        if(!player.isAI()) // если игрок человек
        {
            do
            {
                // Проверка корректности ввода

                Scanner scanner = new Scanner(System.in);
                System.out.println("Select target unit (0 for exit game):");
                /* Вывод доступных юнитов,
                   для атаки - юниты противника
                   для лечения - юниты текущего игрока
                 */
                System.out.println(player.getController().getTargetUnits());

                while (!scanner.hasNextInt())
                {
                    System.out.println("It's not an integer!");
                    System.out.println("Enter command:");
                    scanner.next();
                }
                targetUnitNum = scanner.nextInt();
                if(targetUnitNum == 0) break;
                // Проверить возможность выбора указанного целевого юнита
                if(player.getController().testTargetUnitNum(targetUnitNum)) break;

                System.out.println("Incorrect!");
            }while (true);
        }
        else
        {
            // Компьютер выбирает цель случайным образом

            Random random = new Random();
            targetUnitNum = random.nextInt(player.getAvailibleUnitsCount()) + 1;
        }
        return targetUnitNum;
    }
}
