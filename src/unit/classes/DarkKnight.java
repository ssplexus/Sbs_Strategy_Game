package unit.classes;

import unit.interfaces.Warrior;

import java.util.Random;

/**
 *  Класс тёмного рыцаря
 *  наследует базовый класс юнита и интерфейс воина
 */
public class DarkKnight extends Unit implements Warrior
{
    // Сила атаки
    final private int force = 50;

    public DarkKnight (String name)
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
        unit.getDamaged(force * new Random().nextInt(101) / 100);
    }

    @Override
    public void defense() {

    }
}