package unit.interfaces;
import unit.classes.Unit;

/**
 *  Интерфейс лекаря
 */
public interface Healer
{
    /** Метод лечить
     *
     * @param unit - целевой юнит
     */
    void heal(Unit unit);

    /**
     *  Метод защищаться
     */
    void defense();

    /** Получить возможные действия для лекаря
     *
     * @return строку списка действий
     */
    default String getAvailableActions()
    {
        return "1-heal, 2-defense";
    }
}
