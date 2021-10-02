package unit.classes;

import unit.interfaces.Warmachine;

import java.util.Random;

/**
 *  Класс требушета
 *  наследует базовый класс юнита и интерфейс катапульты
 */
public class Trebuchet extends Unit implements Warmachine
{
    // Сила атаки
    final private int force = 100;
    // Количество шагов игры до полной перезарядки
    private int reload;

    public Trebuchet ()
    {
        super("Trebuchet", 300);
        reload = 0;
    }

    /** Переопределённый метод атаки интрефейса катапульты
     *
     * @param unit - целевой юнит
     */
    @Override
    public void attack(Unit unit)
    {
        if(reload > 0) // если шаги до перезагрузки остались, то атака невозможна
            System.out.println("Trebuchet is reloading");
        else
        {
            System.out.println(String.format("%s attacks %s", getName(), unit.getName()));
            unit.getDamaged(force * new Random().nextInt(101) / 100); // вычисление значение повреждения
            reload = 2;
        }
    }

    /**
     *  Переопределённый метод проверки готовности катапульты интрефейса катапульты
     */
    @Override
    public void reloading()
    {
        if(reload > 0) reload--;
    }

    /** Переопределённый метод проверить статус перезарядки интерфейса катапульты
     *
     * @return перезарядка или нет
     */
    @Override
    public boolean isReloading() {
        return reload > 0;
    }
}
