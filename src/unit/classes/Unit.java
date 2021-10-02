package unit.classes;

/**
 * Базовый класс объекта игры
 */
abstract public class Unit
{
    // Максимальное значение здоровья юнита
    final private int MAX_HP;
    // Имя юнита
    private String name;
    // Параметр здоровья
    private int hp;
    // Юнита действующий или уничтожен
    private boolean isDestroyed;

    public Unit(String name, int hp)
    {
        this.name = name;
        this.hp = hp;
        MAX_HP = hp;
    }

    /** Метод обработки получения повроеждения
     *
     * @param damage - значение повреждения
     */
    public void getDamaged(int damage)
    {
        hp -= damage;
        if(hp <= 0) // Если здоровье ниже 0 то юнит уничтожен
        {
            isDestroyed = true;
            System.out.println(String.format("%s get damage %d pts", name, damage));
            System.out.println(String.format("%s destroyed", name));
        }
        else
            System.out.println(String.format("%s get damage %d pts", name, damage));
    }

    /** Добавить здоровья юниту
     *
     * @param hp - количество очков здоровья
     */
    public void addHp(int hp)
    {
        this.hp += hp;
        if(this.hp > MAX_HP)
        {
            this.hp = MAX_HP;
            System.out.println(String.format("%s has full HP", name));
        }
        else
            System.out.println(String.format("%s has restored health by %d pts", name, hp));
    }

    /** Получить значение здоровья
     *
     * @return hp
     */
    public int getHp()
    {
        return hp;
    }

    /** Проверка уничтожен ли юнит
     *
     * @return флаг isDestroyed
     */
    public boolean isDestroyed() { return isDestroyed; }

    /** Получить имя юнита
     *
     * @return имя юнита
     */
    public String getName()
    {
        return name;
    }
}
