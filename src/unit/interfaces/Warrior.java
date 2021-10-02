package unit.interfaces;
import unit.classes.Unit;

/**
 *  Интерфейс воина
 */
public interface Warrior
{
    /** Метод для атаки
     *
     * @param unit - целевой юнит
     */
    void attack(Unit unit);

    /**
     *  Метод защищаться
     */
    void defense();

    /** Получить возможные действия для воина
     *
     * @return строку списка действий
     */
    default String getAvailableActions()
    {
        return "1-attack, 2-defense";
    }
}
