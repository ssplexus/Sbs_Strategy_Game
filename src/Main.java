import unit.classes.Player;

import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // Параметр значения команды пользователя
        // значение 2 означает replay, то есть переход к выбору режима игры
        int command = 2;

        // Параметр хранения порядкового номера целевого югита (из не уничтоженных)
        int targetUnitNum = 0;
        // Номер действия
        int actionNum;

        // Текущий игрок
        Player currentPlayer = null;
        Player player = null;
        Player enemy = null;

        // Основной цикл игры
        while (true)
        {
            if(currentPlayer != null && player != null && enemy != null)
            {
                // Вывод доступных юнитов игроков
                System.out.println("====================================================");
                System.out.println(player);
                System.out.println(enemy);
                System.out.println("====================================================");
                System.out.println("Enter any key to continue");
                scanner.nextInt();

                System.out.println(String.format("%s's turn", currentPlayer.getName()));
            }

            if(currentPlayer != null) command = getPlayerCommand(currentPlayer); // Получить команду от текущего игрока
            if(command == 0) break; // если 0 то выход из игры
            if (command == 2) // если команда на перезапуск
            {
                // Запуск нового цикла игры

                do
                {
                    System.out.println("""
                                   Game mode:
                                   0 - 'exit game'
                                   1 - 'player vs player'
                                   2 - 'player vs cpu'
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

                if(command == 0) break; // если 0 то выход из игры
                /*
                Создание игроков и назначение им друг друга в качестве соперников
                */

                player = new Player(command == 2 ? "Player" : "Player_1");
                enemy = new Player(command == 2 ? "Enemy Player" : "Player_2", command == 2 ? true : false);
                player.setEnemyPlayer(enemy);
                enemy.setEnemyPlayer(player);

                currentPlayer = player;
                continue;
            }

            System.out.println();

            // Взять под контроль юнит текущим игроком
            if(takeControlUnit(currentPlayer) != 0) // если 0 пропуск хода
            {
                // Получить действие для выбранного юнита от текущего игрока
                actionNum = getPlayerAction(currentPlayer);
                if(actionNum != 0) // если 0 пропуск хода
                {
                    // Если атака или лечение то необходимо получить целевой юнит
                    if(actionNum == 1) targetUnitNum = getPlayerTarget(currentPlayer); // получить целевой юнит

                    if(actionNum == 1 && targetUnitNum == 0)
                        System.out.println("Skip turn");
                    else
                    {
                        // Передаём контроллеру действие и цель и выполняем действие

                        currentPlayer.getController().setAction(actionNum);
                        currentPlayer.getController().setTargetUnitNum(targetUnitNum);
                        currentPlayer.getController().makeAction();
                    }
                }
                else System.out.println("Skip turn");
            }
            else System.out.println("Skip turn");

            currentPlayer = currentPlayer == player ? enemy : player; // Меняем текущего игрока

            // Проверка победителя игры, если у игрока не осталось действующих юнитов то он проиграл
            if(currentPlayer.getAvailibleUnitsCount() == 0)
            {
                System.out.println();
                System.out.println(String.format("%s win the game!", (currentPlayer == player ? enemy : player).getName()));
                System.out.println("GAME OVER");

                //Запуск нового цикла игры

                player = null;
                enemy = null;
                currentPlayer = null;

                command = 2;
            }
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
                System.out.println("Select unit (0 for skip turn):");
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

                System.out.println("Select action (0 for skip turn):");
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
                System.out.println("Select target unit (0 for skip turn):");
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
            targetUnitNum = random.nextInt(player.getController().getTargetUnits().split(",").length) + 1;
        }
        return targetUnitNum;
    }
}
