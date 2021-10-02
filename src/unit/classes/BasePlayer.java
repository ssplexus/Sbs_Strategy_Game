package unit.classes;

/**
 *  Баовый класс игрока
 */
abstract public class BasePlayer
{
    // Имя игрока
    final private String name;
    // Игрок компьютер или человек
    final protected boolean isAI;

    // Массив юнитов игрока
    protected Unit units[];

    protected BasePlayer (String name, boolean isAI)
    {
        this.name = name;
        this.isAI = isAI;
    }

    /**
     * Переопределение метода toString класса Object
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s units: ",name));
        int i = 1;
        for (Unit unit: units)
        {
            if(!unit.isDestroyed())
                str.append(String.format("%d-%s(%d HP) ",i++ , unit.getName(), unit.getHp()));
        }
        return str.toString().trim();
    }

    /** Полуить число доступных (не уничтоженных юнитов)
     *
     * @return - количество
     */
    public int getAvailibleUnitsCount()
    {
        int count = 0;
        for(Unit unit : units)
        {
            if(!unit.isDestroyed()) count++;
        }
        return count;
    }

    /** Получить имя игрока
     *
     * @return - имя игрока
     */
    public String getName()
    {
        return name;
    }
}
