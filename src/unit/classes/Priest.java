package unit.classes;

import unit.interfaces.Healer;

/**
 *  Класс священника
 *  наследует базовый класс юнита и интерфейс лекаря
 */
public class Priest extends Unit implements Healer
{
    // Сила исцеления
    final private int healForce = 50;
    // В защите или нет
    public boolean isDefend = false;

    public Priest (String name)
    {
        super(name, 100);
    }

    /** Переопределённый метод получения повреждения класса unit.classes.Unit
     *
     * @param damage - значение повреждения
     */
    @Override
    public void getDamaged(int damage)
    {
        super.getDamaged(isDefend ? damage / 2: damage); // если в защите, то урон снижен вдвое
    }

    /** Переопределённый метод исцеления интрефейса лекаря
     *
     * @param unit - целевой юнит
     */
    @Override
    public void heal(Unit unit)
    {
        System.out.println(String.format("%s heals %s", getName(), unit.getName()));
        unit.addHp(healForce);
    }

    /**
     * Переопределённый метод защиты интрефейса лекаря
     */
    @Override
    public void defense()
    {
        System.out.println(String.format("%s defends", getName()));
        isDefend = true;
    }
}
