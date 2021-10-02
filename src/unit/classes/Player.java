package unit.classes;

import unit.interfaces.Healer;
import unit.interfaces.Warmachine;
import unit.interfaces.Warrior;

import java.lang.reflect.Array;

/**
 * Класс игрока
 * наследует базовый класс игрока
 */
public class Player extends BasePlayer
{
    // Предельное число юнитов
    final private int MAX_UNITS = 5;

    // Контроллер игрока
    private Controller controller;

    // Ссылка на объект противоположного игрока
    private Player enemyPlayer;

    public Player(String name)
    {
        this(name, false);
    }

    public Player(String name, boolean isAI)
    {
        super(name, isAI);
        units = isAI ? buildEnemyUnits() : buildPlayerUnits(); // создание юнитов игроков
        controller = new Controller(); // создание контроллера
        enemyPlayer = null;
    }

    /** Получение контроллера игрока
     *
     * @return - контроллер игрока
     */
    public Controller getController()
    {
        return controller;
    }

    /** Назначение вражеского игрока
     *
     * @param enemyPlayer
     */
    public void setEnemyPlayer(Player enemyPlayer)
    {
        this.enemyPlayer = enemyPlayer;
    }

    /** Проверка игрок компьютер или нет
     *
     * @return
     */
    public boolean isAI()
    {
        return isAI;
    }

    // Создание юнита игрока человека
    private Unit[] buildPlayerUnits()
    {
        Unit units[] = new Unit[MAX_UNITS];
        units[0]  = new Knight("Knight_1");
        units[1]  = new Knight("Knight_2");
        units[2]  = new Knight("Knight_3");
        units[3]  = new Priest("Healer");
        units[4]  = new Trebuchet();
        return units;
    }

    // Создание юнита игрока компьютера
    private Unit[] buildEnemyUnits()
    {
        Unit units[] = new Unit[MAX_UNITS];
        units[0]  = new DarkKnight("DarkKnight_1");
        units[1]  = new DarkKnight("DarkKnight_2");
        units[2]  = new DarkKnight("DarkKnight_3");
        units[3]  = new DarkKnight("DarkKnight_4");
        units[4]  = new DarkKnight("DarkKnight_5");
        return units;
    }

    /**
     *  Внетренний класс контроллер
     *  осуществляет управление юнитами
     */
    public class Controller
    {
        // Номер текущего действия
        private int currentActionNum;
        // Порядковый номер целевого юнита
        private int targetUnitNum;
        // Текущий подконтрольный юнит
        private Unit ctrlUnit;

        /** Взять юнит под контроль
         *
         * @param unitNum - порядковый номер юнита из возможных
         * @param control - управлять или просто получить указатель
         * @return
         */
        public Unit takeAvailableUnit(int unitNum, boolean control)
        {
            Unit findUnit = null;
            if(unitNum > 0 && unitNum <= getAvailibleUnitsCount()) // проверка корректности номера юнита
            {
                for (int i = 0, j = 1; i < Array.getLength(units); i++)
                {
                    if (!units[i].isDestroyed())
                    {
                        if(control && units[i] instanceof Warmachine)
                            ((Warmachine) units[i]).reloading();

                        if (j == unitNum)
                            findUnit = units[i];
                        j++;
                    }
                }
                if (control && findUnit != null) // если юнит найден и мы хотим им управлять
                {
                    currentActionNum = 0;
                    targetUnitNum = 0;

                    if(findUnit instanceof Warmachine) // если это катапульта проверяем её возможность использования
                    {
                        if (((Warmachine) findUnit).isReloading()) { // катапульта на перезарядке
                            System.out.println(String.format("%s is reloading", findUnit.getName()));
                            return null;
                        }
                    }
                    ctrlUnit = findUnit; // юнит которым хотим управлять
                    System.out.println(String.format("%s is under control", findUnit.getName()));
                }
            }
            return findUnit;
        }

        /** Получаем доступные целевые юниты для текущего подконтрольного
         *
         * @return - список доступных юнитов
         */
        public String getTargetUnits()
        {
            if(ctrlUnit != null)
            {
                if(ctrlUnit instanceof Healer)
                    return Player.this.toString();
                else
                    return enemyPlayer.toString();
            }
            return "";
        }

        /** Выполнить действие подконтрольным юнитом
         *
         * @return - результат действия
         */
        public boolean makeAction()
        {
            // Проверка корректности действия
            if(!testAction(currentActionNum)) return false;

            switch (currentActionNum)
            {
                case 1: // атака или лечение
                    if(!testTargetUnitNum(targetUnitNum)) return false; // проверка корректности целевого юнита

                    if(ctrlUnit instanceof Healer) // если лекарь, то лечить своего
                        ((Healer) ctrlUnit).heal(takeAvailableUnit(targetUnitNum, false));
                    if(ctrlUnit instanceof Warrior && enemyPlayer != null) // если воин, то атаковать врага
                        ((Warrior) ctrlUnit).attack(enemyPlayer.getController().takeAvailableUnit(targetUnitNum, false));
                    if(ctrlUnit instanceof Warmachine && enemyPlayer != null) // если катапульта, то атаковать врага
                        ((Warmachine) ctrlUnit).attack(enemyPlayer.getController().takeAvailableUnit(targetUnitNum, false));
                    break;
                case 2: // уйти в защиту для воина или лекаря
                    if(ctrlUnit instanceof Warrior)
                        ((Warrior) ctrlUnit).defense();
                    if(ctrlUnit instanceof Healer)
                        ((Healer) ctrlUnit).defense();
                    break;
                default:
                    return false;
            }

            return true;
        }

        /** Получить доступные действия для подконтрольного юнита
         *
         * @return - список действий
         */
        public String getAvailibleActions()
        {
            if(ctrlUnit == null) return "";

            if(ctrlUnit instanceof Warrior)
                return ((Warrior)ctrlUnit).getAvailableActions();
            if(ctrlUnit instanceof Healer)
                return ((Healer)ctrlUnit).getAvailableActions();
            if(ctrlUnit instanceof Warmachine)
                return ((Warmachine)ctrlUnit).getAvailableActions();
            return "";
        }

        /** Задать действие для подконтрольного юнита
         *
         * @param act - действие
         */
        public void setAction(int act)
        {
            currentActionNum = act;
        }
        public boolean testAction(int actNum)
        {
            if(getAvailibleActions().isEmpty()) return false;
            if(actNum < 0 || actNum > getAvailibleActions().split(",").length) return false;
            return true;
        }

        /** Задать порядковый номер целевого юнита для подконтрольного юнита
         *
         * @param unitNum - порядковый номер юнита
         */
        public void setTargetUnitNum(int unitNum)
        {
            targetUnitNum = unitNum;
        }

        /** Проверка корректности номера целевого юнита
         *
         * @param unitNum - порядковый номер юнита
         * @return - результат проверки
         */
        public boolean testTargetUnitNum(int unitNum)
        {
            if(ctrlUnit == null) return false;

            if(ctrlUnit instanceof Healer)
            {
                if(unitNum <= 0 || unitNum > getAvailibleUnitsCount()) return false;
                return true;
            }
            else
            {
                if(unitNum <= 0 || unitNum > enemyPlayer.getAvailibleUnitsCount()) return false;
                return true;
            }
        }
    }
}
