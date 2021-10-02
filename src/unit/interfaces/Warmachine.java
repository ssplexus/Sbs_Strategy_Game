package unit.interfaces;
import unit.classes.Unit;

/**
 *  Интерфейс для катапульт
 */
public interface Warmachine
{
    /** Метод для атаки
     *
     * @param unit - целевой юнит
     */
    void attack(Unit unit);

    /**
     * Метод перезарядки
     */
    void reloading();

    /** Проверить статус перезарядки
     *
     * @return перезарядка или нет
     */
    boolean isReloading();

    /** Получить возможные действия для катапульты
     *
     * @return строку списка действий
     */
    default String getAvailableActions()
    {
        return "1-attack";
    }
}
