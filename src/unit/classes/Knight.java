package unit.classes;

import unit.interfaces.Warrior;

import java.util.Random;

/**
 *  Класс рыцаря
 *  наследует базовый класс юнита и интерфейс воина
 */
public class Knight extends Unit implements Warrior
{
    // Сила атаки
    final private int force = 50;
    // Параметр "в защите"
    public boolean isDefend = false;

    public Knight (String name)
    {
        super(name, 150);
    }

    /** Переопределённый метод атаки интрефейса воина
     *
     * @param unit - целевой юнит
     */
    @Override
    public void attack(Unit unit)
    {
        System.out.println(String.format("%s attacks %s", getName(), unit.getName()));
        unit.getDamaged(force * new Random().nextInt(101) / 100); // вычисление силы повреждения
    }

    /** Переопределённый метод получения повреждения класса unit.classes.Unit
     *
     * @param damage - значение повреждения
     */
    @Override
    public void getDamaged(int damage) {
        super.getDamaged(isDefend ? damage / 2: damage); // если в защите, то урон снижен вдвое
    }

    /**
     * Переопределённый метод защиты интрефейса воина
     */
    @Override
    public void defense()
    {
        System.out.println(String.format("%s defends", getName()));
        isDefend = true;
    }
}
